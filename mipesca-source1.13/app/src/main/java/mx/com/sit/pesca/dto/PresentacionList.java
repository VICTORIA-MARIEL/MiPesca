package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.PresentacionEntity;

public class PresentacionList {
    @SerializedName("comboPresentaciones")
    private List<PresentacionEntity> presentaciones;

    public PresentacionList(){

    }



    public List<PresentacionEntity> getPresentaciones() {
        return presentaciones;
    }

    public void setPresentaciones(List<PresentacionEntity> presentaciones) {
        this.presentaciones = presentaciones;
    }
}
