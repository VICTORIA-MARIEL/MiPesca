package mx.com.sit.pesca.entity;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PresentacionEntity extends RealmObject {
    @PrimaryKey
    private int presentacionId;
    @Required
    private String presentacionDescripcion;

    private short presentacionEstatus;

    private int recursoId;

    public PresentacionEntity(){

    }

    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
    }

    public int getPresentacionId() {
        return presentacionId;
    }

    public void setPresentacionId(int presentacionId) {
        this.presentacionId = presentacionId;
    }

    public String getPresentacionDescripcion() {
        return presentacionDescripcion;
    }

    public void setPresentacionDescripcion(String presentacionDescripcion) {
        this.presentacionDescripcion = presentacionDescripcion;
    }

    public short getPresentacionEstatus() {
        return presentacionEstatus;
    }

    public void setPresentacionEstatus(short presentacionEstatus) {
        this.presentacionEstatus = presentacionEstatus;
    }
}
