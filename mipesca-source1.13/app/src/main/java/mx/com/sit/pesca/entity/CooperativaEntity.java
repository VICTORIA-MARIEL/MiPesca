package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class CooperativaEntity extends RealmObject {
    @PrimaryKey
    private int cooperativaId;
    @Required
    private String cooperativaDescripcion;

    private short cooperativaEstatus;

    private int comunidadId;

    public CooperativaEntity(){

    }

    public int getCooperativaId() {
        return cooperativaId;
    }

    public void setCooperativaId(int cooperativaId) {
        this.cooperativaId = cooperativaId;
    }

    public String getCooperativaDescripcion() {
        return cooperativaDescripcion;
    }

    public void setCooperativaDescripcion(String cooperativaDescripcion) {
        this.cooperativaDescripcion = cooperativaDescripcion;
    }

    public short getCooperativaEstatus() {
        return cooperativaEstatus;
    }

    public void setCooperativaEstatus(short cooperativaEstatus) {
        this.cooperativaEstatus = cooperativaEstatus;
    }

    public int getComunidadId() {
        return comunidadId;
    }

    public void setComunidadId(int comunidadId) {
        this.comunidadId = comunidadId;
    }
}
