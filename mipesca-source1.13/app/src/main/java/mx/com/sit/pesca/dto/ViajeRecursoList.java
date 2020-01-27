package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;

public class ViajeRecursoList {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("viajeRecurso")
    private ViajeRecursoEntity viajeRecurso;

    public ViajeRecursoList(){

    }

    public int getCodigo() {
            return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    public ViajeRecursoEntity getViajeRecurso() {
        return viajeRecurso;
    }

    public void setViajeRecurso(ViajeRecursoEntity viajeRecurso) {
        this.viajeRecurso = viajeRecurso;
    }
}
