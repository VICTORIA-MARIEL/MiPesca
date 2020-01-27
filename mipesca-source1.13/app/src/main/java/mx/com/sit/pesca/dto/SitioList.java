package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.SitioEntity;

public class SitioList {
    @SerializedName("comboSitios")
    private List<SitioEntity> sitios;

    public SitioList(){

    }

    public List<SitioEntity> getSitios() {
        return sitios;
    }

    public void setSitios(List<SitioEntity> sitios) {
        this.sitios = sitios;
    }
}
