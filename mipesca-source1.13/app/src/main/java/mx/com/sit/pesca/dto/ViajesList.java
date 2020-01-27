package mx.com.sit.pesca.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import mx.com.sit.pesca.entity.ViajeEntity;

public class ViajesList {
    @SerializedName("codigo")
    private int codigo;

    @SerializedName("error")
    private String error;

    @SerializedName("viaje")
    @Expose
    private ArrayList<ViajeEntity> viajes;

    public ViajesList(ArrayList<ViajeEntity> viajes) {
        this.viajes=viajes;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<ViajeEntity> getViajes() {
        return viajes;
    }

    public void setViajes(ArrayList<ViajeEntity> viajes) {
        this.viajes = viajes;
    }
}
