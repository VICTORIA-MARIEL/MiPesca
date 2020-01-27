package mx.com.sit.pesca.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import mx.com.sit.pesca.app.MyApp;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class ViajeEntity  extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private Date viajeFhRegistro;
    @Nullable
    private Date viajeFhFinalizo;
    @SerializedName("municipioId")
    private int municipioId;
    @SerializedName("comunidadId")
    private int comunidadId;
    @SerializedName("artepescaId")
    private int artepescaId;
    @SerializedName("viajeCombustible")
    private int combustible;
    @SerializedName("usuarioId")
    private int usuarioId;
    @SerializedName("pescadorId")
    private int pescadorId;
    @SerializedName("viajeIdApp")
    private int viajeIdLocal;
    @SerializedName("viajeId")
    private int viajeIdRemoto;
    @Nullable
    private Date viajeFhSincronizacion;
    private short viajeEstatus;
    private short viajeEstatusSinc;
    private String viajeFolio;

    private RealmList<ViajeRecursoEntity> viajesRecursos;
    private String comunidadDescripcion;
    private String municipioDescripcion;
    private String artepescaDescripcion;

    public ViajeEntity(){

    }


    public ViajeEntity(
            int municipioId,
            int comunidadId,
            int artepescaId,
            int combustible,
            int usuarioId,
            int pescadorId,
            String municipioDescripcion,
            String comunidadDescripcion,
            String artepescaDescripcion
    ){
        this.id = MyApp.viajeID.incrementAndGet();
        this.viajeFhRegistro = new Date();
        this.viajeFhFinalizo = null;
        this.municipioId = municipioId;
        this.comunidadId = comunidadId;
        this.artepescaId = artepescaId;
        this.combustible = combustible;
        this.usuarioId = usuarioId;
        this.pescadorId = pescadorId;
        this.viajeIdLocal = 0;
        this.viajeIdRemoto = 0;
        this.viajeEstatus = Constantes.VIAJE_ESTATUS_INICIAL;
        this.viajeFhSincronizacion = null;
        this.viajeEstatusSinc = Constantes.ESTATUS_NO_SINCRONIZADO;
        this.viajesRecursos = new RealmList<ViajeRecursoEntity>();
        this.municipioDescripcion = municipioDescripcion;
        this.comunidadDescripcion = comunidadDescripcion;
        this.artepescaDescripcion = artepescaDescripcion;

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



    public int getViajeIdRemoto() {
        return viajeIdRemoto;
    }

    public void setViajeIdRemoto(int viajeIdRemoto) {
        this.viajeIdRemoto = viajeIdRemoto;
    }

    public String getComunidadDescripcion() {
        return comunidadDescripcion;
    }

    public void setComunidadDescripcion(String comunidadDescripcion) {
        this.comunidadDescripcion = comunidadDescripcion;
    }

    public String getArtepescaDescripcion() {
        return artepescaDescripcion;
    }

    public void setArtepescaDescripcion(String artepescaDescripcion) {
        this.artepescaDescripcion = artepescaDescripcion;
    }

    public void setViajesRecursos(RealmList<ViajeRecursoEntity> viajesRecursos) {
        this.viajesRecursos = viajesRecursos;
    }

    public String getViajeFolio() {

        return Util.generarFolio(getPescadorId(),getId());
    }

    public void setViajeFolio(String viajeFolio) {
        this.viajeFolio = viajeFolio;
    }


    public int getPescadorId() {
        return pescadorId;
    }

    public void setPescadorId(int pescadorId) {
        this.pescadorId = pescadorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getViajeFhRegistro() {
        return viajeFhRegistro;
    }

    public void setViajeFhRegistro(Date viajeFhRegistro) {
        this.viajeFhRegistro = viajeFhRegistro;
    }

    public Date getViajeFhFinalizo() {
        return viajeFhFinalizo;
    }

    public void setViajeFhFinalizo(Date viajeFhFinalizo) {
        this.viajeFhFinalizo = viajeFhFinalizo;
    }

    public int getComunidadId() {
        return comunidadId;
    }

    public void setComunidadId(int comunidadId) {
        this.comunidadId = comunidadId;
    }

    public int getArtepescaId() {
        return artepescaId;
    }

    public void setArtepescaId(int artepescaId) {
        this.artepescaId = artepescaId;
    }

    public int getCombustible() {
        return combustible;
    }

    public void setCombustible(int combustible) {
        this.combustible = combustible;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getViajeIdLocal() {
        return viajeIdLocal;
    }

    public void setViajeIdLocal(int viajeIdLocal) {
        this.viajeIdLocal = viajeIdLocal;
    }

    public Date getViajeFhSincronizacion() {
        return viajeFhSincronizacion;
    }

    public void setViajeFhSincronizacion(Date viajeFhSincronizacion) {
        this.viajeFhSincronizacion = viajeFhSincronizacion;
    }

    public short getViajeEstatus() {
        return viajeEstatus;
    }

    public void setViajeEstatus(short viajeEstatus) {
        this.viajeEstatus = viajeEstatus;
    }

    public short getViajeEstatusSinc() {
        return viajeEstatusSinc;
    }

    public void setViajeEstatusSinc(short viajeEstatusSinc) {
        this.viajeEstatusSinc = viajeEstatusSinc;
    }

    public RealmList<ViajeRecursoEntity> getViajesRecursos() {
        if(viajesRecursos!=null){
            return viajesRecursos;
        }
        else{
            return new RealmList<ViajeRecursoEntity>();
        }
    }


}
