package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.ComunidadEntity;

public class ComunidadList {
    @SerializedName("comboComunidades")
    private List<ComunidadEntity> comunidades;

    public ComunidadList(){

    }

    public List<ComunidadEntity> getComunidades() {
        return comunidades;
    }

    public void setComunidades(List<ComunidadEntity> comunidades) {
        this.comunidades = comunidades;
    }
}
