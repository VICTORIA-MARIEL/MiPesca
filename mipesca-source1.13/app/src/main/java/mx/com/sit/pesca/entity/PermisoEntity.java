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

public class PermisoEntity extends RealmObject {
    @PrimaryKey
    private int id;
    private int pescadorId;
    private int usuarioId;
    @Required
    private Date permisoFhRegistro;
    @Required
    private String permisoNumero;
    @Nullable
    private String permisoNombreEmbarcacion; //128
    @Nullable
    private String permisoRnpaEmbarcacion; //32
    @Nullable
    private String permisoMatricula;//10
    private short permisoNoEmbarcaciones;
    @Required
    private String permisoSitioDesembarque;//128
    @Required
    private String permisoSitioDesembarqueClave;//32
    @Required
    private String permisoZonaPesca; //1
    private short permisoEstatus;
    private int permisoIdLocal;
    @Nullable
    private Date permisoFhSincronizacion;
    private String permisoTonelaje;
    private String permisoMarcaMotor;
    private String permisoPotenciaHp;
    @Required
    private Date permisoFhVigenciaInicio;
    @Required
    private Date permisoFhVigenciaFin;
    private short permisoFhVigenciaDuracion;
    private String permisoFhExpedicion;
    private String permisoParaPesqueria;
    private short permisoArteCantidad;
    private int artePescaId;
    private String permisoArteCaracteristica;
    private String permisoLugarExpedicion;
    private short permisoEstatusSinc;
    @SerializedName("permisoId")
    private int permisoIdRemoto;
    private RealmList<AvisoEntity> avisos;

    @Nullable
    private String permisoTitular;

    @Nullable
    private String permisoEstado;
    private short permisoEstadoId;
    @Nullable
    private String permisoMunicipio;
    private short permisoMunicipioId;
    @Required
    private String permisoArtepesca;

    public PermisoEntity(){

    }

    public PermisoEntity(int pescadorId,
                         int usuarioId,
                         String permisoNumero,
                         String permisoNombreEmbarcacion,
                         String permisoRnpaEmbarcacion,
                         String permisoMatricula,
                         short permisoNoEmbarcaciones,
                         String permisoSitioDesembarque,
                         String permisoSitioDesembarqueClave,
                         String permisoZonaPesca,
                         String permisoTonelaje,
                         String permisoMarcaMotor,
                         String permisoPotenciaHp,
                         Date permisoFhVigenciaInicio,
                         Date permisoFhVigenciaFin,
                         short permisoFhVigenciaDuracion,
                         String permisoFhExpedicion,
                         String permisoParaPesqueria,
                         short permisoArteCantidad,
                         String permisoArtepesca,
                         String permisoArteCaracteristica,
                         String permisoLugarExpedicion,
                         String permisoTitular,
                         String permisoEstado,
                         short permisoEstadoId,
                         String permisoMunicipio,
                         short permisoMunicipioId
                       ){
        this.id = MyApp.permisoID.incrementAndGet();
        this.pescadorId = pescadorId;
        this.usuarioId = usuarioId;
        this.permisoNumero = permisoNumero;
        this.permisoNombreEmbarcacion = permisoNombreEmbarcacion;

        this.permisoRnpaEmbarcacion = permisoRnpaEmbarcacion;
        this.permisoMatricula = permisoMatricula;
        this.permisoNoEmbarcaciones = permisoNoEmbarcaciones;
        this.permisoSitioDesembarque = permisoSitioDesembarque;

        this.permisoSitioDesembarqueClave = permisoSitioDesembarqueClave;//32
        this.permisoZonaPesca = permisoZonaPesca; //1
        this.permisoTonelaje = permisoTonelaje;

        this.permisoMarcaMotor = permisoMarcaMotor;
        this.permisoPotenciaHp = permisoPotenciaHp;
        this.permisoFhVigenciaInicio = permisoFhVigenciaInicio;
        this.permisoFhVigenciaFin = permisoFhVigenciaFin;

        this.permisoFhVigenciaDuracion = permisoFhVigenciaDuracion;
        this.permisoFhExpedicion = permisoFhExpedicion;
        this.permisoParaPesqueria = permisoParaPesqueria;

        this.permisoArteCantidad = permisoArteCantidad;
        this.artePescaId = 0;
        this.permisoArtepesca = permisoArtepesca;
        this.permisoTitular = permisoTitular;
        this.permisoEstado = permisoEstado;
        this.permisoMunicipio = permisoMunicipio;
        this.permisoEstadoId = permisoEstadoId;
        this.permisoMunicipioId = permisoMunicipioId;
        this.permisoArteCaracteristica = permisoArteCaracteristica;
        this.permisoLugarExpedicion = permisoLugarExpedicion;


        this.permisoFhRegistro = new Date();
        this.permisoEstatus =1;
        this.permisoIdLocal = 0;
        this.permisoIdRemoto = 0;
        this.permisoFhSincronizacion = null;
        this.permisoEstatusSinc = 0;
        this.avisos = new RealmList<AvisoEntity>();
    }

    @Nullable
    public String getPermisoEstado() {
        return permisoEstado;
    }

    public void setPermisoEstado(@Nullable String permisoEstado) {
        this.permisoEstado = permisoEstado;
    }

    public short getPermisoEstadoId() {
        return permisoEstadoId;
    }

    public void setPermisoEstadoId(short permisoEstadoId) {
        this.permisoEstadoId = permisoEstadoId;
    }

    @Nullable
    public String getPermisoMunicipio() {
        return permisoMunicipio;
    }

    public void setPermisoMunicipio(@Nullable String permisoMunicipio) {
        this.permisoMunicipio = permisoMunicipio;
    }

    public short getPermisoMunicipioId() {
        return permisoMunicipioId;
    }

    public void setPermisoMunicipioId(short permisoMunicipioId) {
        this.permisoMunicipioId = permisoMunicipioId;
    }

    public int getPermisoIdRemoto() {
        return permisoIdRemoto;
    }

    public void setPermisoIdRemoto(int permisoIdRemoto) {
        this.permisoIdRemoto = permisoIdRemoto;
    }

    @Nullable
    public String getPermisoTitular() {
        return permisoTitular;
    }

    public void setPermisoTitular(@Nullable String permisoTitular) {
        this.permisoTitular = permisoTitular;
    }

    public String getPermisoArtepesca() {
        return permisoArtepesca;
    }

    public void setPermisoArtepesca(String permisoArtepesca) {
        this.permisoArtepesca = permisoArtepesca;
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

    public Date getPermisoFhRegistro() {
        return permisoFhRegistro;
    }

    public void setPermisoFhRegistro(Date permisoFhRegistro) {
        this.permisoFhRegistro = permisoFhRegistro;
    }

    public String getPermisoNumero() {
        return permisoNumero;
    }

    public void setPermisoNumero(String permisoNumero) {
        this.permisoNumero = permisoNumero;
    }

    @Nullable
    public String getPermisoNombreEmbarcacion() {
        return permisoNombreEmbarcacion;
    }

    public void setPermisoNombreEmbarcacion(@Nullable String permisoNombreEmbarcacion) {
        this.permisoNombreEmbarcacion = permisoNombreEmbarcacion;
    }

    @Nullable
    public String getPermisoRnpaEmbarcacion() {
        return permisoRnpaEmbarcacion;
    }

    public void setPermisoRnpaEmbarcacion(@Nullable String permisoRnpaEmbarcacion) {
        this.permisoRnpaEmbarcacion = permisoRnpaEmbarcacion;
    }

    @Nullable
    public String getPermisoMatricula() {
        return permisoMatricula;
    }

    public void setPermisoMatricula(@Nullable String permisoMatricula) {
        this.permisoMatricula = permisoMatricula;
    }

    public short getPermisoNoEmbarcaciones() {
        return permisoNoEmbarcaciones;
    }

    public void setPermisoNoEmbarcaciones(short permisoNoEmbarcaciones) {
        this.permisoNoEmbarcaciones = permisoNoEmbarcaciones;
    }

    public String getPermisoSitioDesembarque() {
        return permisoSitioDesembarque;
    }

    public void setPermisoSitioDesembarque(String permisoSitioDesembarque) {
        this.permisoSitioDesembarque = permisoSitioDesembarque;
    }

    public String getPermisoSitioDesembarqueClave() {
        return permisoSitioDesembarqueClave;
    }

    public void setPermisoSitioDesembarqueClave(String permisoSitioDesembarqueClave) {
        this.permisoSitioDesembarqueClave = permisoSitioDesembarqueClave;
    }

    public String getPermisoZonaPesca() {
        return permisoZonaPesca;
    }

    public void setPermisoZonaPesca(String permisoZonaPesca) {
        this.permisoZonaPesca = permisoZonaPesca;
    }

    public short getPermisoEstatus() {
        return permisoEstatus;
    }

    public void setPermisoEstatus(short permisoEstatus) {
        this.permisoEstatus = permisoEstatus;
    }

    public int getPermisoIdLocal() {
        return permisoIdLocal;
    }

    public void setPermisoIdLocal(int permisoIdLocal) {
        this.permisoIdLocal = permisoIdLocal;
    }

    @Nullable
    public Date getPermisoFhSincronizacion() {
        return permisoFhSincronizacion;
    }

    public void setPermisoFhSincronizacion(@Nullable Date permisoFhSincronizacion) {
        this.permisoFhSincronizacion = permisoFhSincronizacion;
    }

    public String getPermisoTonelaje() {
        return permisoTonelaje;
    }

    public void setPermisoTonelaje(String permisoTonelaje) {
        this.permisoTonelaje = permisoTonelaje;
    }

    public String getPermisoMarcaMotor() {
        return permisoMarcaMotor;
    }

    public void setPermisoMarcaMotor(String permisoMarcaMotor) {
        this.permisoMarcaMotor = permisoMarcaMotor;
    }

    public String getPermisoPotenciaHp() {
        return permisoPotenciaHp;
    }

    public void setPermisoPotenciaHp(String permisoPotenciaHp) {
        this.permisoPotenciaHp = permisoPotenciaHp;
    }

    public Date getPermisoFhVigenciaInicio() {
        return permisoFhVigenciaInicio;
    }

    public void setPermisoFhVigenciaInicio(Date permisoFhVigenciaInicio) {
        this.permisoFhVigenciaInicio = permisoFhVigenciaInicio;
    }

    public Date getPermisoFhVigenciaFin() {
        return permisoFhVigenciaFin;
    }

    public void setPermisoFhVigenciaFin(Date permisoFhVigenciaFin) {
        this.permisoFhVigenciaFin = permisoFhVigenciaFin;
    }

    public short getPermisoFhVigenciaDuracion() {
        return permisoFhVigenciaDuracion;
    }

    public void setPermisoFhVigenciaDuracion(short permisoFhVigenciaDuracion) {
        this.permisoFhVigenciaDuracion = permisoFhVigenciaDuracion;
    }

    public String getPermisoFhExpedicion() {
        return permisoFhExpedicion;
    }

    public void setPermisoFhExpedicion(String permisoFhExpedicion) {
        this.permisoFhExpedicion = permisoFhExpedicion;
    }

    public String getPermisoParaPesqueria() {
        return permisoParaPesqueria;
    }

    public void setPermisoParaPesqueria(String permisoParaPesqueria) {
        this.permisoParaPesqueria = permisoParaPesqueria;
    }

    public short getPermisoArteCantidad() {
        return permisoArteCantidad;
    }

    public void setPermisoArteCantidad(short permisoArteCantidad) {
        this.permisoArteCantidad = permisoArteCantidad;
    }

    public int getArtePescaId() {
        return artePescaId;
    }

    public void setArtePescaId(int artePescaId) {
        this.artePescaId = artePescaId;
    }

    public String getPermisoArteCaracteristica() {
        return permisoArteCaracteristica;
    }

    public void setPermisoArteCaracteristica(String permisoArteCaracteristica) {
        this.permisoArteCaracteristica = permisoArteCaracteristica;
    }

    public String getPermisoLugarExpedicion() {
        return permisoLugarExpedicion;
    }

    public void setPermisoLugarExpedicion(String permisoLugarExpedicion) {
        this.permisoLugarExpedicion = permisoLugarExpedicion;
    }

    public short getPermisoEstatusSinc() {
        return permisoEstatusSinc;
    }

    public void setPermisoEstatusSinc(short permisoEstatusSinc) {
        this.permisoEstatusSinc = permisoEstatusSinc;
    }

    public RealmList<AvisoEntity> getAvisos() {
        return avisos;
    }

    public void setAvisos(RealmList<AvisoEntity> avisos) {
        this.avisos = avisos;
    }
}
