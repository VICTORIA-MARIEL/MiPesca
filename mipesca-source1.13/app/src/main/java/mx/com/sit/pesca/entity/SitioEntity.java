package mx.com.sit.pesca.entity;

import java.math.BigDecimal;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class SitioEntity extends RealmObject {
    @PrimaryKey
    private int sitioId;
    private short sitioNumero;
    private int regionId;
    @Required
    private String sitioDescripcion;
    @Required
    private String sitioClave;
    private short sitioEstatus;
    private int usuarioId;


    public SitioEntity(){
    }

    public int getSitioId() {
        return sitioId;
    }

    public void setSitioId(int sitioId) {
        this.sitioId = sitioId;
    }

    public short getSitioNumero() {
        return sitioNumero;
    }

    public void setSitioNumero(short sitioNumero) {
        this.sitioNumero = sitioNumero;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getSitioDescripcion() {
        return sitioDescripcion;
    }

    public void setSitioDescripcion(String sitioDescripcion) {
        this.sitioDescripcion = sitioDescripcion;
    }

    public String getSitioClave() {
        return sitioClave;
    }

    public void setSitioClave(String sitioClave) {
        this.sitioClave = sitioClave;
    }


    public short getSitioEstatus() {
        return sitioEstatus;
    }

    public void setSitioEstatus(short sitioEstatus) {
        this.sitioEstatus = sitioEstatus;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

}
