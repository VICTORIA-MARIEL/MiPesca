package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RecursoEntity extends RealmObject {
    @PrimaryKey
    private int recursoId;
    @Required
    private String recursoDescripcion;

    private short recursoEstatus;

    private int comunidadId;


    public RecursoEntity(){

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

    public short getRecursoEstatus() {
        return recursoEstatus;
    }

    public void setRecursoEstatus(short recursoEstatus) {
        this.recursoEstatus = recursoEstatus;
    }

    public int getComunidadId() {
        return comunidadId;
    }

    public void setComunidadId(int comunidadId) {
        this.comunidadId = comunidadId;
    }
}
