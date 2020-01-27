package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PreguntaEntity  extends RealmObject {
    @PrimaryKey
    private short preguntaId;
    private String preguntaDescripcion;
    private short preguntaEstatus;

    public PreguntaEntity(){

    }

    public short getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(short preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getPreguntaDescripcion() {
        return preguntaDescripcion;
    }

    public void setPreguntaDescripcion(String preguntaDescripcion) {
        this.preguntaDescripcion = preguntaDescripcion;
    }

    public short getPreguntaEstatus() {
        return preguntaEstatus;
    }

    public void setPreguntaEstatus(short preguntaEstatus) {
        this.preguntaEstatus = preguntaEstatus;
    }
}
