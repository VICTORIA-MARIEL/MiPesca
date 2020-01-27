package mx.com.sit.pesca.entity;

public class PancartaEntity  {
    private Integer pancartaId;
    private String pancartaTitulo;
    private String pancartaDescripcion;

    public PancartaEntity(){


    }

    public Integer getPancartaId() {
        return pancartaId;
    }

    public void setPancartaId(Integer pancartaId) {
        this.pancartaId = pancartaId;
    }

    public String getPancartaTitulo() {
        return pancartaTitulo;
    }

    public void setPancartaTitulo(String pancartaTitulo) {
        this.pancartaTitulo = pancartaTitulo;
    }

    public String getPancartaDescripcion() {
        return pancartaDescripcion;
    }

    public void setPancartaDescripcion(String pancartaDescripcion) {
        this.pancartaDescripcion = pancartaDescripcion;
    }
}
