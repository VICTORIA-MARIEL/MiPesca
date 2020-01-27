package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.ViajeEntity;

public class AvisoList {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("aviso")
    private AvisoEntity aviso;

    public AvisoList(){

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public AvisoEntity getAviso() {
        return aviso;
    }

    public void setAviso(AvisoEntity aviso) {
        this.aviso = aviso;
    }

}
