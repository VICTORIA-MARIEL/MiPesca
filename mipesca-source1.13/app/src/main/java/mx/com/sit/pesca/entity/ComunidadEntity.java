package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ComunidadEntity extends RealmObject {
    @PrimaryKey
    private int comunidadId;
    @Required
    private String comunidadDescripcion;

    private short comunidadEstatus;

    private int municipioId;

    public ComunidadEntity(){

    }

    public int getComunidadId() {
        return comunidadId;
    }

    public void setComunidadId(int comunidadId) {
        this.comunidadId = comunidadId;
    }

    public String getComunidadDescripcion() {
        return comunidadDescripcion;
    }

    public void setComunidadDescripcion(String comunidadDescripcion) {
        this.comunidadDescripcion = comunidadDescripcion;
    }

    public short getComunidadEstatus() {
        return comunidadEstatus;
    }

    public void setComunidadEstatus(short comunidadEstatus) {
        this.comunidadEstatus = comunidadEstatus;
    }

    public int getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(int municipioId) {
        this.municipioId = municipioId;
    }
}
