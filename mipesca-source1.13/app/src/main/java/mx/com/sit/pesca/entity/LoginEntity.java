package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LoginEntity extends RealmObject {
    @PrimaryKey
    private String usuario;
    private String contrasena;
    private boolean success;

    public LoginEntity(){

    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
