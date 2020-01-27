package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.RegionEntity;

public class RegionList {
    @SerializedName("comboRegiones")
    private List<RegionEntity> regiones;

    public RegionList(){

    }


    public List<RegionEntity> getRegiones() {
        return regiones;
    }

    public void setRegiones(List<RegionEntity> regiones) {
        this.regiones = regiones;
    }
}
