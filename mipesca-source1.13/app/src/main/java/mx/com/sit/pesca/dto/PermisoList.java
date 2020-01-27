package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mx.com.sit.pesca.entity.PermisoEntity;

public class PermisoList {
    @SerializedName("comboPermisos")
    private List<PermisoEntity> permisos;

    @SerializedName("permisoDTO")
    private PermisoEntity permiso;

    @SerializedName("codigo")
    private int codigo;

    @SerializedName("success")
    private boolean success;

    public PermisoList(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PermisoEntity getPermiso() {
        return permiso;
    }

    public void setPermiso(PermisoEntity permiso) {
        this.permiso = permiso;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public List<PermisoEntity> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<PermisoEntity> permisos) {
        this.permisos = permisos;
    }
}
