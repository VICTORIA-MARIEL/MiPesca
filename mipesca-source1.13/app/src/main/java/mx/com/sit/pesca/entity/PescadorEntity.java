package mx.com.sit.pesca.entity;

import android.support.annotation.Nullable;

import java.util.Date;
import mx.com.sit.pesca.util.Util;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import mx.com.sit.pesca.app.MyApp;

public class PescadorEntity  extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String pescadorNombre;

    private short pescadorEstatus;

    private int pescadorUsrRegistro;
    @Required
    private Date pescadorFhRegistro;
    @Required
    private Date pescadorFhNacimiento;
    @Required
    private String pescadorTelefono;
    @Required
    private String pescadorCorreo;

    private int comunidadId;
    private int municipioId;

    private int cooperativaId;
    @Required
    private String municipioDescripcion;
    @Required
    private String comunidadDescripcion;
    @Nullable
    private String cooperativaDescripcion;

    @Required
    private String pescadorPermiso;

    private short pescadorEsIndependiente;

    private short preguntaId;
    @Required
    private String pescadorPregunta;
    @Required
    private String pescadorRespuesta;
    @Nullable
    private Date pescadorFhSincronizacion;

    private short pescadorEstatusSinc;
    @Required
    private String pescadorClaveConfirmacion;


    public PescadorEntity(){

    }

    public PescadorEntity(
            int id,
            String pescadorNombre,
            Date pescadorFhNacimiento,
            String pescadorTelefono,
            String pescadorCorreo,
            int municipioId,
            int comunidadId,
            int cooperativaId,
            String pescadorPermiso,
            short pescadorEsIndependiente,
            String municipioDescripcion,
            String comunidadDescripion,
            String cooperativaDescripcion){
        this.id = id;
        this.pescadorNombre = pescadorNombre;
        this.pescadorFhNacimiento = pescadorFhNacimiento;
        this.pescadorTelefono = pescadorTelefono;
        this.pescadorCorreo = pescadorCorreo;
        this.municipioId = municipioId;
        this.comunidadId = comunidadId;
        this.cooperativaId = cooperativaId;
        this.pescadorPermiso = pescadorPermiso;
        this.pescadorEsIndependiente  = pescadorEsIndependiente;
        this.municipioDescripcion = municipioDescripcion;
        this.comunidadDescripcion = comunidadDescripcion;
        this.cooperativaDescripcion = cooperativaDescripcion;
    }

    public PescadorEntity(
            String pescadorNombre,
            Date pescadorFhNacimiento,
            String pescadorTelefono,
            String pescadorCorreo,
            int municipioId,
            int comunidadId,
            int cooperativaId,
            String pescadorPermiso,
            short pescadorEsIndependiente,
            String municipioDescripcion,
            String comunidadDescripcion,
            String cooperativaDescripcion
            ){
        this.id = MyApp.pescadorID.incrementAndGet();
        this.pescadorNombre = pescadorNombre;
        this.pescadorFhNacimiento = pescadorFhNacimiento;
        this.pescadorTelefono = pescadorTelefono;
        this.pescadorCorreo = pescadorCorreo;
        this.municipioId = municipioId;
        this.comunidadId = comunidadId;
        this.cooperativaId = cooperativaId;
        this.pescadorPermiso = pescadorPermiso;
        this.pescadorEsIndependiente  = pescadorEsIndependiente;
        this.pescadorEstatus = 1;
        this.pescadorUsrRegistro = 1;
        this.pescadorFhRegistro = new Date();
        this.preguntaId = 0;
        this.pescadorRespuesta = "";
        this.pescadorPregunta = "";
        this.pescadorFhSincronizacion = new Date();
        this.pescadorEstatusSinc = 0;
        this.pescadorClaveConfirmacion = Util.generarClaveConfirmacion();
        this.municipioDescripcion = municipioDescripcion;
        this.comunidadDescripcion = comunidadDescripcion;
        this.cooperativaDescripcion = cooperativaDescripcion;

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

    public String getComunidadDescripcion() {
        return comunidadDescripcion;
    }

    public void setComunidadDescripcion(String comunidadDescripcion) {
        this.comunidadDescripcion = comunidadDescripcion;
    }

    @Nullable
    public String getCooperativaDescripcion() {
        return cooperativaDescripcion;
    }

    public void setCooperativaDescripcion(@Nullable String cooperativaDescripcion) {
        this.cooperativaDescripcion = cooperativaDescripcion;
    }

    public String getPescadorPregunta() {
        return pescadorPregunta;
    }

    public void setPescadorPregunta(String pescadorPregunta) {
        this.pescadorPregunta = pescadorPregunta;
    }

    public String getPescadorClaveConfirmacion() {
        return pescadorClaveConfirmacion;
    }

    public void setPescadorClaveConfirmacion(String pescadorClaveConfirmacion) {
        this.pescadorClaveConfirmacion = pescadorClaveConfirmacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPescadorNombre() {
        return pescadorNombre;
    }

    public void setPescadorNombre(String pescadorNombre) {
        this.pescadorNombre = pescadorNombre;
    }

    public short getPescadorEstatus() {
        return pescadorEstatus;
    }

    public void setPescadorEstatus(short pescadorEstatus) {
        this.pescadorEstatus = pescadorEstatus;
    }

    public int getPescadorUsrRegistro() {
        return pescadorUsrRegistro;
    }

    public void setPescadorUsrRegistro(int pescadorUsrRegistro) {
        this.pescadorUsrRegistro = pescadorUsrRegistro;
    }

    public Date getPescadorFhRegistro() {
        return pescadorFhRegistro;
    }

    public void setPescadorFhRegistro(Date pescadorFhRegistro) {
        this.pescadorFhRegistro = pescadorFhRegistro;
    }

    public Date getPescadorFhNacimiento() {
        return pescadorFhNacimiento;
    }

    public void setPescadorFhNacimiento(Date pescadorFhNacimiento) {
        this.pescadorFhNacimiento = pescadorFhNacimiento;
    }

    public String getPescadorTelefono() {
        return pescadorTelefono;
    }

    public void setPescadorTelefono(String pescadorTelefono) {
        this.pescadorTelefono = pescadorTelefono;
    }

    public String getPescadorCorreo() {
        return pescadorCorreo;
    }

    public void setPescadorCorreo(String pescadorCorreo) {
        this.pescadorCorreo = pescadorCorreo;
    }

    public int getComunidadId() {
        return comunidadId;
    }

    public void setComunidadId(int comunidadId) {
        this.comunidadId = comunidadId;
    }

    public int getCooperativaId() {
        return cooperativaId;
    }

    public void setCooperativaId(int cooperativaId) {
        this.cooperativaId = cooperativaId;
    }

    public String getPescadorPermiso() {
        return pescadorPermiso;
    }

    public void setPescadorPermiso(String pescadorPermiso) {
        this.pescadorPermiso = pescadorPermiso;
    }

    public short getPescadorEsIndependiente() {
        return pescadorEsIndependiente;
    }

    public void setPescadorEsIndependiente(short pescadorEsIndependiente) {
        this.pescadorEsIndependiente = pescadorEsIndependiente;
    }

    public short getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(short preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getPescadorRespuesta() {
        return pescadorRespuesta;
    }

    public void setPescadorRespuesta(String pescadorRespuesta) {
        this.pescadorRespuesta = pescadorRespuesta;
    }

    public Date getPescadorFhSincronizacion() {
        return pescadorFhSincronizacion;
    }

    public void setPescadorFhSincronizacion(Date pescadorFhSincronizacion) {
        this.pescadorFhSincronizacion = pescadorFhSincronizacion;
    }

    public short getPescadorEstatusSinc() {
        return pescadorEstatusSinc;
    }

    public void setPescadorEstatusSinc(short pescadorEstatusSinc) {
        this.pescadorEstatusSinc = pescadorEstatusSinc;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id>"+this.id);
        sb.append("pescadorNombre>"+this.pescadorNombre);
        sb.append("pescadorEstatus>"+this.pescadorEstatus);
        sb.append("pescadorUsrRegistro>"+this.pescadorUsrRegistro);
        sb.append("pescadorFhRegistro>"+this.pescadorFhRegistro);
        sb.append("pescadorFhNacimiento>"+this.pescadorFhNacimiento);
        sb.append("pescadorTelefono>"+this.pescadorTelefono);
        sb.append("pescadorCorreo>"+this.pescadorCorreo);
        sb.append("comunidadId>"+this.comunidadId);
        sb.append("cooperativaId>"+this.cooperativaId);
        sb.append("pescadorPermiso>"+this.pescadorPermiso);
        sb.append("pescadorEsIndependiente>"+this.pescadorEsIndependiente);
        sb.append("preguntaId>"+this.preguntaId);
        sb.append("pescadorRespuesta>"+this.pescadorRespuesta);
        sb.append("pescadorFhSincronizacion>"+this.pescadorFhSincronizacion);
        sb.append("pescadorEstatusSinc>"+this.pescadorEstatusSinc);
        return sb.toString();
    }
}
