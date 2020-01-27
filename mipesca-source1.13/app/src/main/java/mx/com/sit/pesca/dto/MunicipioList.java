package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;

public class MunicipioList {
    @SerializedName("comboMunicipios")
    private List<MunicipioEntity> municipios;

    public MunicipioList(){

    }

    public List<MunicipioEntity> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<MunicipioEntity> municipios) {
        this.municipios = municipios;
    }
}
