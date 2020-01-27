package mx.com.sit.pesca.dto;

import com.google.gson.annotations.SerializedName;

public class LoginList {
    @SerializedName("success")
    private boolean success;

    @SerializedName("usuarioLogin")
    private String usuarioLogin;

    @SerializedName("usuarioNombre")
    private String usuarioNombre;

    @SerializedName("usuarioContrasena")
    private String usuarioContrasena;


    @SerializedName("pescadorNombre")
    private String pescadorNombre;

    @SerializedName("pescadorFhNacimiento")
    private String pescadorFhNacimiento;

    @SerializedName("pescadorTelefono")
    private String pescadorTelefono;

    @SerializedName("pescadorCorreo")
    private String pescadorCorreo;

    @SerializedName("pescadorMunicipio")
    private String pescadorMunicipio;

    @SerializedName("pescadorComunidad")
    private String pescadorComunidad;

    @SerializedName("pescadorCooperativa")
    private String pescadorCooperativa;

    @SerializedName("pescadorMunicipioId")
    private String pescadorMunicipioId;

    @SerializedName("pescadorComunidadId")
    private String pescadorComunidadId;

    @SerializedName("pescadorCooperativaId")
    private String pescadorCooperativaId;

    @SerializedName("pescadorEsIndependiente")
    private boolean pescadorEsIndependiente;

    public LoginList(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioContrasena() {
        return usuarioContrasena;
    }

    public void setUsuarioContrasena(String usuarioContrasena) {
        this.usuarioContrasena = usuarioContrasena;
    }

    public String getPescadorNombre() {
        return pescadorNombre;
    }

    public void setPescadorNombre(String pescadorNombre) {
        this.pescadorNombre = pescadorNombre;
    }

    public String getPescadorFhNacimiento() {
        return pescadorFhNacimiento;
    }

    public void setPescadorFhNacimiento(String pescadorFhNacimiento) {
        this.pescadorFhNacimiento = pescadorFhNacimiento;
    }

    public String getPescadorTelefono() {
        return pescadorTelefono;
    }

    public void setPescadorTelefono(String pescadorTelefono) {
        this.pescadorTelefono = pescadorTelefono;
    }

    public String getPescadorCorreo() {
        return pescadorCorreo;
    }

    public void setPescadorCorreo(String pescadorCorreo) {
        this.pescadorCorreo = pescadorCorreo;
    }

    public String getPescadorMunicipio() {
        return pescadorMunicipio;
    }

    public void setPescadorMunicipio(String pescadorMunicipio) {
        this.pescadorMunicipio = pescadorMunicipio;
    }

    public String getPescadorComunidad() {
        return pescadorComunidad;
    }

    public void setPescadorComunidad(String pescadorComunidad) {
        this.pescadorComunidad = pescadorComunidad;
    }

    public String getPescadorCooperativa() {
        return pescadorCooperativa;
    }

    public void setPescadorCooperativa(String pescadorCooperativa) {
        this.pescadorCooperativa = pescadorCooperativa;
    }

    public String getPescadorMunicipioId() {
        return pescadorMunicipioId;
    }

    public void setPescadorMunicipioId(String pescadorMunicipioId) {
        this.pescadorMunicipioId = pescadorMunicipioId;
    }

    public String getPescadorComunidadId() {
        return pescadorComunidadId;
    }

    public void setPescadorComunidadId(String pescadorComunidadId) {
        this.pescadorComunidadId = pescadorComunidadId;
    }

    public String getPescadorCooperativaId() {
        return pescadorCooperativaId;
    }

    public void setPescadorCooperativaId(String pescadorCooperativaId) {
        this.pescadorCooperativaId = pescadorCooperativaId;
    }

    public boolean getPescadorEsIndependiente() {
        return pescadorEsIndependiente;
    }

    public void setPescadorEsIndependiente(boolean pescadorEsIndependiente) {
        this.pescadorEsIndependiente = pescadorEsIndependiente;
    }
}
