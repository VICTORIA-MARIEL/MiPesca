package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.ArtePescaEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;

public class ViajeList {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("viaje")
    private ViajeEntity viaje;


    public ViajeList(){

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public ViajeEntity getViaje() {
        return viaje;
    }

    public void setViaje(ViajeEntity viaje) {
        this.viaje = viaje;
    }

}
