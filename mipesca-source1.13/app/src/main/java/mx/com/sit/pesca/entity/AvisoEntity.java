package mx.com.sit.pesca.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import javax.annotation.Nullable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import mx.com.sit.pesca.app.MyApp;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class AvisoEntity extends RealmObject {
    @PrimaryKey
    private int id;
    private int pescadorId;
    private int usuarioId;
    @Required
    private Date avisoFhRegistro;
    @Required
    private Date avisoFhSolicitud;
    @Required
    private Date avisoPeriodoInicio;
    @Required
    private Date avisoPeriodoFin;
    private short avisoDuracion;
    private short avisoDiasEfectivos;
    @Required
    private String avisoFolio; //32
    @SerializedName("avisoIdApp")
    private int avisoIdLocal;
    @SerializedName("avisoId")
    private int avisoIdRemoto;
    @Nullable
    private Date avisoFhSincronizacion;
    private short avisoEstatus;
    private int permisoId; //32
    @Required
    private String avisoZonaPesca;
    private int ofnapescaId;
    private byte avisoEsPesqueriaAcuacultural;
    private int sitioId;
    private short avisoEstatusSinc;

    private String sitioDescripcion;
    private String ofnapescaDescripcion;
    private String permisoNumero;
    private RealmList<AvisoViajeEntity> avisoViajes;


    public AvisoEntity(){

    }

    public AvisoEntity(int pescadorId, int usuarioId, Date avisoFhSolicitud, Date  avisoPeriodoInicio,
                       Date avisoPeriodoFin, short avisoDuracion, short avisoDiasEfectivos,int permisoId,
                       String avisoZonaPesca, int ofnapescaId, byte avisoEsPesqueriaAcuacultural, int sitioId,
                        String sitioDescripcion, String ofnapescaDescripcion, String permisoNumero
                       ){
        this.id = MyApp.avisoID.incrementAndGet();
        this.pescadorId = pescadorId;
        this.usuarioId = usuarioId;
        this.avisoFhRegistro = new Date();
        this.avisoFhSolicitud = avisoFhSolicitud;
        this.avisoPeriodoInicio = avisoPeriodoInicio;
        this.avisoPeriodoFin = avisoPeriodoFin;
        this.avisoDuracion = avisoDuracion;
        this.permisoId = permisoId;
        this.avisoDiasEfectivos= avisoDiasEfectivos;
        this.avisoFolio = Util.generarFolio(pescadorId, id); //32
        this.avisoEstatus = Constantes.AVISO_ESTATUS_INICIAL;
        this.avisoIdLocal = 0;
        this.avisoIdRemoto = 0;
        this.avisoFhSincronizacion = null;
        this.avisoEstatusSinc = Constantes.ESTATUS_NO_SINCRONIZADO;
        this.avisoZonaPesca = avisoZonaPesca;
        this.ofnapescaId = ofnapescaId;
        this.avisoEsPesqueriaAcuacultural = avisoEsPesqueriaAcuacultural;
        this.sitioId = sitioId;
        this.sitioDescripcion = sitioDescripcion;
        this.ofnapescaDescripcion = ofnapescaDescripcion;
        this.permisoNumero = permisoNumero;
        this.avisoViajes = new RealmList<AvisoViajeEntity>();
    }

    public int getAvisoIdRemoto() {
        return avisoIdRemoto;
    }

    public void setAvisoIdRemoto(int avisoIdRemoto) {
        this.avisoIdRemoto = avisoIdRemoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPescadorId() {
        return pescadorId;
    }

    public void setPescadorId(int pescadorId) {
        this.pescadorId = pescadorId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getAvisoFhRegistro() {
        return avisoFhRegistro;
    }

    public void setAvisoFhRegistro(Date avisoFhRegistro) {
        this.avisoFhRegistro = avisoFhRegistro;
    }

    public Date getAvisoFhSolicitud() {
        return avisoFhSolicitud;
    }

    public void setAvisoFhSolicitud(Date avisoFhSolicitud) {
        this.avisoFhSolicitud = avisoFhSolicitud;
    }

    public Date getAvisoPeriodoInicio() {
        return avisoPeriodoInicio;
    }

    public void setAvisoPeriodoInicio(Date avisoPeriodoInicio) {
        this.avisoPeriodoInicio = avisoPeriodoInicio;
    }

    public Date getAvisoPeriodoFin() {
        return avisoPeriodoFin;
    }

    public void setAvisoPeriodoFin(Date avisoPeriodoFin) {
        this.avisoPeriodoFin = avisoPeriodoFin;
    }

    public short getAvisoDuracion() {
        return avisoDuracion;
    }

    public void setAvisoDuracion(short avisoDuracion) {
        this.avisoDuracion = avisoDuracion;
    }

    public short getAvisoDiasEfectivos() {
        return avisoDiasEfectivos;
    }

    public void setAvisoDiasEfectivos(short avisoDiasEfectivos) {
        this.avisoDiasEfectivos = avisoDiasEfectivos;
    }

    public String getAvisoFolio() {
        return avisoFolio;
    }

    public void setAvisoFolio(String avisoFolio) {
        this.avisoFolio = avisoFolio;
    }

    public int getAvisoIdLocal() {
        return avisoIdLocal;
    }

    public void setAvisoIdLocal(int avisoIdLocal) {
        this.avisoIdLocal = avisoIdLocal;
    }

    @Nullable
    public Date getAvisoFhSincronizacion() {
        return avisoFhSincronizacion;
    }

    public void setAvisoFhSincronizacion(@Nullable Date avisoFhSincronizacion) {
        this.avisoFhSincronizacion = avisoFhSincronizacion;
    }

    public short getAvisoEstatus() {
        return avisoEstatus;
    }

    public void setAvisoEstatus(short avisoEstatus) {
        this.avisoEstatus = avisoEstatus;
    }

    public int getPermisoId() {
        return permisoId;
    }

    public void setPermisoId(int permisoId) {
        this.permisoId = permisoId;
    }

    public String getAvisoZonaPesca() {
        return avisoZonaPesca;
    }

    public void setAvisoZonaPesca(String avisoZonaPesca) {
        this.avisoZonaPesca = avisoZonaPesca;
    }

    public int getOfnapescaId() {
        return ofnapescaId;
    }

    public void setOfnapescaId(int ofnapescaId) {
        this.ofnapescaId = ofnapescaId;
    }

    public byte getAvisoEsPesqueriaAcuacultural() {
        return avisoEsPesqueriaAcuacultural;
    }

    public void setAvisoEsPesqueriaAcuacultural(byte avisoEsPesqueriaAcuacultural) {
        this.avisoEsPesqueriaAcuacultural = avisoEsPesqueriaAcuacultural;
    }

    public int getSitioId() {
        return sitioId;
    }

    public void setSitioId(int sitioId) {
        this.sitioId = sitioId;
    }

    public short getAvisoEstatusSinc() {
        return avisoEstatusSinc;
    }

    public void setAvisoEstatusSinc(short avisoEstatusSinc) {
        this.avisoEstatusSinc = avisoEstatusSinc;
    }

    public RealmList<AvisoViajeEntity> getAvisoViajes() {
        if(avisoViajes!=null){
            return avisoViajes;
        }
        else{
            return new RealmList<AvisoViajeEntity>();
        }
    }

    public void setAvisoViajes(RealmList<AvisoViajeEntity> avisoViajes) {
        this.avisoViajes = avisoViajes;
    }

    public String getSitioDescripcion() {
        return sitioDescripcion;
    }

    public void setSitioDescripcion(String sitioDescripcion) {
        this.sitioDescripcion = sitioDescripcion;
    }

    public String getOfnapescaDescripcion() {
        return ofnapescaDescripcion;
    }

    public void setOfnapescaDescripcion(String ofnapescaDescripcion) {
        this.ofnapescaDescripcion = ofnapescaDescripcion;
    }

    public String getPermisoNumero() {
        return permisoNumero;
    }

    public void setPermisoNumero(String permisoNumero) {
        this.permisoNumero = permisoNumero;
    }
}
