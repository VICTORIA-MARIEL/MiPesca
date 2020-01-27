package mx.com.sit.pesca.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ArtePescaEntity extends RealmObject {
    @PrimaryKey
    private int artepescaId;
    @Required
    private String artepescaDescripcion;

    private short artepescaEstatus;

    private int recursoId;


    public ArtePescaEntity(){
    }

    public int getArtepescaId() {
        return artepescaId;
    }

    public void setArtepescaId(int artepescaId) {
        this.artepescaId = artepescaId;
    }

    public String getArtepescaDescripcion() {
        return artepescaDescripcion;
    }

    public void setArtepescaDescripcion(String artepescaDescripcion) {
        this.artepescaDescripcion = artepescaDescripcion;
    }

    public short getArtepescaEstatus() {
        return artepescaEstatus;
    }

    public void setArtepescaEstatus(short artepescaEstatus) {
        this.artepescaEstatus = artepescaEstatus;
    }

    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("artepescaId>" + artepescaId + "\t");

        sb.append("artepescaDescripcion>" + artepescaDescripcion + "\t");

        sb.append("artepescaEstatus>" + artepescaEstatus + "\t");

        sb.append("recursoId>" + recursoId + "\r\n");
        return sb.toString();
    }



}
