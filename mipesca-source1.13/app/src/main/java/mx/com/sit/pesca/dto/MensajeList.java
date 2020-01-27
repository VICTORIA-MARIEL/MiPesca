package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.MensajeEntity;

public class MensajeList {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("mensaje")
    private MensajeEntity mensaje;

    public MensajeList(){

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public MensajeEntity getMensaje() {
        return mensaje;
    }

    public void setMensaje(MensajeEntity mensaje) {
        this.mensaje = mensaje;
    }

}
