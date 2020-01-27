package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MunicipioEntity extends RealmObject {
    @PrimaryKey
    private int municipioId;
    @Required
    private String municipioDescripcion;

    private short municipioEstatus;

    private int estadoId;
    @Required
    private String estadoDescripcion;
    @Required
    private String municipioDescLarga;

    public MunicipioEntity(){

    }

    public int getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(int municipioId) {
        this.municipioId = municipioId;
    }

    public String getMunicipioDescripcion() {
        return municipioDescripcion;
    }

    public void setMunicipioDescripcion(String municipioDescripcion) {
        this.municipioDescripcion = municipioDescripcion;
    }

    public short getMunicipioEstatus() {
        return municipioEstatus;
    }

    public void setMunicipioEstatus(short municipioEstatus) {
        this.municipioEstatus = municipioEstatus;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public String getMunicipioDescLarga() {
        return municipioDescLarga;
    }

    public void setMunicipioDescLarga(String municipioDescLarga) {
        this.municipioDescLarga = municipioDescLarga;
    }
}
