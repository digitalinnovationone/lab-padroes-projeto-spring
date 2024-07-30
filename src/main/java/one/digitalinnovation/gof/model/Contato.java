package one.digitalinnovation.gof.model;

import javax.persistence.*;

@Entity
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroTelefone;
    private String tipoTelefone;
    public Long getId() {
        return id;
    }
    public String getNumeroTelefone() {
        return numeroTelefone;
    }
    public String getTipoTelefone() {
        return tipoTelefone;
    }
    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }
    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }
}
