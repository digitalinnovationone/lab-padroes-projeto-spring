package one.digitalinnovation.gof.model;

/**
 * Representa a categoria do cliente no sistema.
 * Utilizado para definir regras de negócio específicas no Factory Method e no cálculo de descontos.
 */
public enum TipoCliente {
    PESSOA_FISICA("Pessoa Física", 0.05),
    PESSOA_JURIDICA("Pessoa Jurídica", 0.15);

    private final String descricao;
    private final double descontoPadrao;

    TipoCliente(String descricao, double descontoPadrao) {
        this.descricao = descricao;
        this.descontoPadrao = descontoPadrao;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getDescontoPadrao() {
        return descontoPadrao;
    }
}
