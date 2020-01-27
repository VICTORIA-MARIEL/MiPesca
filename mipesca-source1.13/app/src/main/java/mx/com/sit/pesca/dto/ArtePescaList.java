package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.ArtePescaEntity;

public class ArtePescaList {
    @SerializedName("comboArtesPesca")
    private List<ArtePescaEntity> artesPesca;

    public ArtePescaList(){

    }

    public List<ArtePescaEntity> getArtesPesca() {
        return artesPesca;
    }

    public void setArtesPesca(List<ArtePescaEntity> artesPesca) {
        this.artesPesca = artesPesca;
    }
}
