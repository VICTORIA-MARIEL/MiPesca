package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class EdoPesqueriaRecursoEntity extends RealmObject {
    private int recursoId;
    @Required
    private String recursoDescripcion;
    private short estadoId;
    @Required
    private String estadoDescripcion;
    private int pesqueriaId;
    @Required
    private String pesqueriaDescripcion;



    public EdoPesqueriaRecursoEntity(){

    }

    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
    }

    public String getRecursoDescripcion() {
        return recursoDescripcion;
    }

    public void setRecursoDescripcion(String recursoDescripcion) {
        this.recursoDescripcion = recursoDescripcion;
    }

    public short getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(short estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public int getPesqueriaId() {
        return pesqueriaId;
    }

    public void setPesqueriaId(int pesqueriaId) {
        this.pesqueriaId = pesqueriaId;
    }

    public String getPesqueriaDescripcion() {
        return pesqueriaDescripcion;
    }

    public void setPesqueriaDescripcion(String pesqueriaDescripcion) {
        this.pesqueriaDescripcion = pesqueriaDescripcion;
    }
}
