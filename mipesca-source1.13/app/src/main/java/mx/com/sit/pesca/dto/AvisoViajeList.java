package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;

public class AvisoViajeList {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("avisoViaje")
    private AvisoViajeEntity avisoViaje;

    public AvisoViajeList(){

    }

    public int getCodigo() {
            return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    public AvisoViajeEntity getAvisoViaje() {
        return avisoViaje;
    }

    public void setAvisoViaje(AvisoViajeEntity avisoViaje) {
        this.avisoViaje = avisoViaje;
    }
}
