package mx.com.sit.pesca.entity;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import mx.com.sit.pesca.app.MyApp;

public class UsuarioEntity  extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String usuarioLogin;
    @Required
    private String usuarioNombre;
    @Required
    private String usuarioContrasena;
    private int usuarioEstatus;

    public UsuarioEntity(int id,
                        String usuarioLogin,
                         String usuarioNombre,
                         String usuarioContrasena,
                         int usuarioEstatus
    ){
        this.id = id;
        this.usuarioLogin = usuarioLogin;
        this.usuarioNombre = usuarioNombre;
        this.usuarioContrasena = usuarioContrasena;
        this.usuarioEstatus = usuarioEstatus;
    }

    public UsuarioEntity(String usuarioLogin,
                         String usuarioNombre,
                         String usuarioContrasena
                         ){
        this.id = MyApp.usuarioID.incrementAndGet();
        this.usuarioLogin = usuarioLogin;
        this.usuarioNombre = usuarioNombre;
        this.usuarioContrasena = usuarioContrasena;
        this.usuarioEstatus = 0;
    }

    public UsuarioEntity(){

    }

    public int getUsuarioEstatus() {
        return usuarioEstatus;
    }

    public void setUsuarioEstatus(int usuarioEstatus) {
        this.usuarioEstatus = usuarioEstatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id>"+id+"\t");
        sb.append("usuarioLogin>"+usuarioLogin+"\t");
        sb.append("usuarioNombre>"+usuarioNombre+"\t");
        sb.append("usuarioContrasena>"+usuarioContrasena+"\t");
        sb.append("usuarioEstatus>"+usuarioEstatus+"\r\n");
        return sb.toString();
    }

}
