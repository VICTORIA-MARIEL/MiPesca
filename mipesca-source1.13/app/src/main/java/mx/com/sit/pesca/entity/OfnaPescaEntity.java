package mx.com.sit.pesca.entity;

import java.util.Date;

import javax.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class OfnaPescaEntity extends RealmObject {
    @PrimaryKey
    private int ofnapescaId;
    @Required
    private String ofnapescaDescripcion;

    private short ofnapescaEstatus;
    private short estadoId;
    private int usuarioId;
    @Nullable
    private Date ofnapescaFhRegistro;


    public OfnaPescaEntity(){
    }

    public int getOfnapescaId() {
        return ofnapescaId;
    }

    public void setOfnapescaId(int ofnapescaId) {
        this.ofnapescaId = ofnapescaId;
    }

    public String getOfnapescaDescripcion() {
        return ofnapescaDescripcion;
    }

    public void setOfnapescaDescripcion(String ofnapescaDescripcion) {
        this.ofnapescaDescripcion = ofnapescaDescripcion;
    }

    public short getOfnapescaEstatus() {
        return ofnapescaEstatus;
    }

    public void setOfnapescaEstatus(short ofnapescaEstatus) {
        this.ofnapescaEstatus = ofnapescaEstatus;
    }

    public short getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(short estadoId) {
        this.estadoId = estadoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getOfnapescaFhRegistro() {
        return ofnapescaFhRegistro;
    }

    public void setOfnapescaFhRegistro(Date ofnapescaFhRegistro) {
        this.ofnapescaFhRegistro = ofnapescaFhRegistro;
    }


}
