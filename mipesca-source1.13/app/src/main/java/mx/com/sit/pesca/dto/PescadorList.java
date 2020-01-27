package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

public class PescadorList {
    @SerializedName("success")
    private boolean success;

    @SerializedName("strPescadorId")
    private String pescadorId;

    @SerializedName("strUsuarioId")
    private String usuarioId;

    @SerializedName("usuarioNombre")
    private String usuarioNombre;



    public PescadorList(String usuarioId, boolean success, String usuarioNombre){
        this.success = success;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPescadorId() {
        return pescadorId;
    }

    public void setPescadorId(String pescadorId) {
        this.pescadorId = pescadorId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
}
