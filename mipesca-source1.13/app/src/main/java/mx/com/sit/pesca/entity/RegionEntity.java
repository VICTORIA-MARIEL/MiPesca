package mx.com.sit.pesca.entity;

import java.math.BigDecimal;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RegionEntity extends RealmObject {
    @PrimaryKey
    private int regionId;
    private int estadoId;
    @Required
    private String regionDescripcion;
    private short regionEstatus;

    public RegionEntity(){
    }


    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(int estadoId) {
        this.estadoId = estadoId;
    }

    public String getRegionDescripcion() {
        return regionDescripcion;
    }

    public void setRegionDescripcion(String regionDescripcion) {
        this.regionDescripcion = regionDescripcion;
    }

    public short getRegionEstatus() {
        return regionEstatus;
    }

    public void setRegionEstatus(short regionEstatus) {
        this.regionEstatus = regionEstatus;
    }

}
