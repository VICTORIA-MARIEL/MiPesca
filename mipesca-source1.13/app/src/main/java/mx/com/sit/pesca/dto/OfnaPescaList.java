package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.OfnaPescaEntity;

public class OfnaPescaList {
    @SerializedName("comboOfnaspesca")
    private List<OfnaPescaEntity> ofnasPesca;

    public OfnaPescaList(){

    }

    public List<OfnaPescaEntity> getOfnasPesca() {
        return ofnasPesca;
    }

    public void setOfnasPesca(List<OfnaPescaEntity> ofnasPesca) {
        this.ofnasPesca = ofnasPesca;
    }
}
