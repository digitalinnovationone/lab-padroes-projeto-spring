package one.digitalinnovation.gof.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.dto.ClienteResponseDTO;
import one.digitalinnovation.gof.dto.EnderecoDTO;
import one.digitalinnovation.gof.event.ClienteCriadoEvent;
import one.digitalinnovation.gof.exception.ResourceNotFoundException;
import one.digitalinnovation.gof.factory.ClienteFactory;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteFacade;
import one.digitalinnovation.gof.service.chain.CepValidationHandler;
import one.digitalinnovation.gof.service.chain.CpfValidationHandler;
import one.digitalinnovation.gof.service.chain.EmailValidationHandler;
import one.digitalinnovation.gof.service.strategy.CepStrategy;
import one.digitalinnovation.gof.service.strategy.CepStrategyFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Padrão Facade + Singleton:
 * Orquestra as integrações complexas do sistema (Banco H2, Validações em Cadeia, Consultas de CEP via Strategy,
 * Fabrica de Objetos e Publicação de Eventos de Domínio).
 */
@Service
public class ClienteFacadeImpl implements ClienteFacade {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CepStrategyFactory cepStrategyFactory;

    @Autowired
    private ClienteFactory clienteFactory;

    @Autowired
    private CpfValidationHandler cpfValidationHandler;

    @Autowired
    private EmailValidationHandler emailValidationHandler;

    @Autowired
    private CepValidationHandler cepValidationHandler;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void setupValidationChain() {
        // Padrão Chain of Responsibility: Encadeia CPF -> E-mail -> CEP
        cpfValidationHandler.linkWith(emailValidationHandler).linkWith(cepValidationHandler);
    }

    @Override
    public Iterable<ClienteResponseDTO> buscarTodos() {
        List<ClienteResponseDTO> dtos = new ArrayList<>();
        clienteRepository.findAll().forEach(cliente -> dtos.add(toResponseDTO(cliente)));
        return dtos;
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));
        return toResponseDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO inserir(ClienteRequestDTO requestDTO) {
        // 1. Executar Chain of Responsibility para validação
        cpfValidationHandler.validate(requestDTO, null, clienteRepository);

        // 2. Executar Strategy Pattern para busca e resolução de endereço
        CepStrategy strategy = cepStrategyFactory.getStrategy(requestDTO.getCepStrategy());
        Endereco endereco = resolverEndereco(requestDTO.getCep(), strategy);

        // 3. Executar Factory Method para instanciar a entidade Cliente
        Cliente cliente = clienteFactory.criarCliente(requestDTO, endereco, strategy.getStrategyName());
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // 4. Executar Observer Pattern publicando o evento de criação
        eventPublisher.publishEvent(new ClienteCriadoEvent(this, clienteSalvo));

        return toResponseDTO(clienteSalvo);
    }

    @Override
    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO requestDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Impossível atualizar. Cliente não encontrado com o ID: " + id));

        // 1. Executar Chain of Responsibility (desconsiderando o próprio ID atual na checagem de unicidade)
        cpfValidationHandler.validate(requestDTO, id, clienteRepository);

        // 2. Executar Strategy Pattern
        CepStrategy strategy = cepStrategyFactory.getStrategy(requestDTO.getCepStrategy());
        Endereco endereco = resolverEndereco(requestDTO.getCep(), strategy);

        // 3. Atualizar dados da entidade
        clienteExistente.setNome(requestDTO.getNome().trim());
        clienteExistente.setCpf(requestDTO.getCpf().replaceAll("\\D", ""));
        clienteExistente.setEmail(requestDTO.getEmail().toLowerCase().trim());
        clienteExistente.setTipoCliente(requestDTO.getTipoCliente());
        clienteExistente.setEndereco(endereco);
        clienteExistente.setCepStrategyUtilizada(strategy.getStrategyName());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return toResponseDTO(clienteAtualizado);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossível deletar. Cliente não encontrado com o ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private Endereco resolverEndereco(String cep, CepStrategy strategy) {
        String cleanCep = cep.replaceAll("\\D", "");
        return enderecoRepository.findById(cleanCep).orElseGet(() -> {
            Endereco novoEndereco = strategy.consultarCep(cleanCep);
            if (novoEndereco != null) {
                novoEndereco.setCep(cleanCep);
                return enderecoRepository.save(novoEndereco);
            }
            return null;
        });
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setEmail(cliente.getEmail());
        dto.setTipoCliente(cliente.getTipoCliente());
        if (cliente.getTipoCliente() != null) {
            dto.setDescontoPadrao(cliente.getTipoCliente().getDescontoPadrao());
        }
        dto.setCepStrategyUtilizada(cliente.getCepStrategyUtilizada());
        dto.setDataCadastro(cliente.getDataCadastro());

        if (cliente.getEndereco() != null) {
            Endereco e = cliente.getEndereco();
            EnderecoDTO enderecoDTO = new EnderecoDTO(
                    e.getCep(), e.getLogradouro(), e.getComplemento(),
                    e.getBairro(), e.getLocalidade(), e.getUf(), e.getDdd()
            );
            dto.setEndereco(enderecoDTO);
        }
        return dto;
    }
}
