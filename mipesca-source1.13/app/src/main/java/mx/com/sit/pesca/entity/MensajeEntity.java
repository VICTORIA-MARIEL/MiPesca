package mx.com.sit.pesca.entity;


public class MensajeEntity  {
    private int mensajeId;
    private String mensajeDesc; //32
    private String mensajeTelefono; //32
    private short mensajeEstatus;

    public MensajeEntity(){

    }

    public int getMensajeId() {
        return mensajeId;
    }

    public void setMensajeId(int mensajeId) {
        this.mensajeId = mensajeId;
    }

    public String getMensajeDesc() {
        return mensajeDesc;
    }

    public void setMensajeDesc(String mensajeDesc) {
        this.mensajeDesc = mensajeDesc;
    }

    public String getMensajeTelefono() {
        return mensajeTelefono;
    }

    public void setMensajeTelefono(String mensajeTelefono) {
        this.mensajeTelefono = mensajeTelefono;
    }

    public short getMensajeEstatus() {
        return mensajeEstatus;
    }

    public void setMensajeEstatus(short mensajeEstatus) {
        this.mensajeEstatus = mensajeEstatus;
    }
}
