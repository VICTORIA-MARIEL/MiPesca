package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.CooperativaEntity;

public class CooperativaList {
    @SerializedName("comboCooperativas")
    private List<CooperativaEntity> cooperativas;

    public CooperativaList(){

    }

    public List<CooperativaEntity> getCooperativas() {
        return cooperativas;
    }

    public void setCooperativas(List<CooperativaEntity> cooperativas) {
        this.cooperativas = cooperativas;
    }
}
