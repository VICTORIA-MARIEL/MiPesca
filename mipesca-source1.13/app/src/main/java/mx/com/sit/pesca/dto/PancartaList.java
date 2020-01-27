package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.PancartaEntity;
import mx.com.sit.pesca.entity.PermisoEntity;

public class PancartaList {
    @SerializedName("comboPancartas")
    private List<PancartaEntity> pancarta;


    @SerializedName("codigo")
    private int codigo;

    @SerializedName("success")
    private boolean success;

    public PancartaList(){

    }

    public List<PancartaEntity> getPancarta() {
        return pancarta;
    }

    public void setPancarta(List<PancartaEntity> pancarta) {
        this.pancarta = pancarta;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
