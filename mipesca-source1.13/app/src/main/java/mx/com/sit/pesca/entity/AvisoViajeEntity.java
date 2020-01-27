package mx.com.sit.pesca.entity;

import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import mx.com.sit.pesca.app.MyApp;
import mx.com.sit.pesca.util.Constantes;

public class AvisoViajeEntity extends RealmObject {
    @PrimaryKey
    private int id;
    private int avisoId;
    private int viajeRecursoId;
    private int avisoViajeTotal;
    @Required
    private Date avisoViajeFhRegistro;
    @SerializedName("usuarioId")
    private int avisoViajeUsrRegistro;
    @Nullable
    private Date avisoViajeFhSincronizacion;
    @SerializedName("avisoIdApp")
    private int avisoIdLocal;
    @SerializedName("avisoViajeIdApp")
    private int avisoViajeIdLocal;
    @SerializedName("avisoViajeId")
    private int avisoViajeIdRemoto;
    private int avisoIdRemoto;
    private int viajeRecursoIdRemoto;
    private short avisoViajeEstatusDeclarado;
    private short avisoViajeEstatus;
    private short avisoViajeEstatusSinc;


    private String avisoViajeRecurso;
    private String avisoViajePresentacion;
    private int avisoViajeNoPiezas;
    private int avisoViajePrecio;
    private int avisoViajeCaptura;

    public AvisoViajeEntity(){

    }

    public AvisoViajeEntity(
            int avisoId,
            int viajeRecursoId,
            int avisoViajeUsrRegistro,
            short avisoViajeEstatusDeclarado,
            String avisoViajeRecurso,
            String avisoViajePresentacion,
            int avisoViajeNoPiezas,
            int avisoViajePrecio,
            int avisoViajeCaptura){

        this.id = MyApp.avisoViajeID.incrementAndGet();
        this.avisoId = avisoId;
        this.viajeRecursoId = viajeRecursoId;
        this.avisoViajeTotal = avisoViajePrecio * avisoViajeCaptura;
        this.avisoViajeFhRegistro = new Date();
        this.avisoViajeUsrRegistro = avisoViajeUsrRegistro;
        this.avisoViajeFhSincronizacion = null;
        this.avisoIdLocal = 0;
        this.avisoViajeEstatusDeclarado = avisoViajeEstatusDeclarado;
        this.avisoViajeEstatus = 1;
        this.avisoViajeIdLocal = 0;
        this.avisoViajeEstatusSinc = Constantes.ESTATUS_NO_SINCRONIZADO;
        this.avisoViajeRecurso = avisoViajeRecurso;
        this.avisoViajePresentacion = avisoViajePresentacion;
        this.avisoViajeNoPiezas = avisoViajeNoPiezas;
        this.avisoViajePrecio = avisoViajePrecio;
        this.avisoViajeCaptura = avisoViajeCaptura;
    }

    public int getViajeRecursoIdRemoto() {
        return viajeRecursoIdRemoto;
    }

    public void setViajeRecursoIdRemoto(int viajeRecursoIdRemoto) {
        this.viajeRecursoIdRemoto = viajeRecursoIdRemoto;
    }

    public int getAvisoIdRemoto() {
        return avisoIdRemoto;
    }

    public void setAvisoIdRemoto(int avisoIdRemoto) {
        this.avisoIdRemoto = avisoIdRemoto;
    }

    public int getAvisoViajeIdRemoto() {
        return avisoViajeIdRemoto;
    }

    public void setAvisoViajeIdRemoto(int avisoViajeIdRemoto) {
        this.avisoViajeIdRemoto = avisoViajeIdRemoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvisoId() {
        return avisoId;
    }

    public void setAvisoId(int avisoId) {
        this.avisoId = avisoId;
    }

    public int getViajeRecursoId() {
        return viajeRecursoId;
    }

    public void setViajeRecursoId(int viajeRecursoId) {
        this.viajeRecursoId = viajeRecursoId;
    }

    public int getAvisoViajeTotal() {
        return avisoViajeTotal;
    }

    public void setAvisoViajeTotal(int avisoViajeTotal) {
        this.avisoViajeTotal = avisoViajeTotal;
    }

    public Date getAvisoViajeFhRegistro() {
        return avisoViajeFhRegistro;
    }

    public void setAvisoViajeFhRegistro(Date avisoViajeFhRegistro) {
        this.avisoViajeFhRegistro = avisoViajeFhRegistro;
    }

    public int getAvisoViajeUsrRegistro() {
        return avisoViajeUsrRegistro;
    }

    public void setAvisoViajeUsrRegistro(int avisoViajeUsrRegistro) {
        this.avisoViajeUsrRegistro = avisoViajeUsrRegistro;
    }

    @Nullable
    public Date getAvisoViajeFhSincronizacion() {
        return avisoViajeFhSincronizacion;
    }

    public void setAvisoViajeFhSincronizacion(@Nullable Date avisoViajeFhSincronizacion) {
        this.avisoViajeFhSincronizacion = avisoViajeFhSincronizacion;
    }

    public int getAvisoIdLocal() {
        return avisoIdLocal;
    }

    public void setAvisoIdLocal(int avisoIdLocal) {
        this.avisoIdLocal = avisoIdLocal;
    }

    public short getAvisoViajeEstatusDeclarado() {
        return avisoViajeEstatusDeclarado;
    }

    public void setAvisoViajeEstatusDeclarado(short avisoViajeEstatusDeclarado) {
        this.avisoViajeEstatusDeclarado = avisoViajeEstatusDeclarado;
    }

    public short getAvisoViajeEstatus() {
        return avisoViajeEstatus;
    }

    public void setAvisoViajeEstatus(short avisoViajeEstatus) {
        this.avisoViajeEstatus = avisoViajeEstatus;
    }

    public int getAvisoViajeIdLocal() {
        return avisoViajeIdLocal;
    }

    public void setAvisoViajeIdLocal(int avisoViajeIdLocal) {
        this.avisoViajeIdLocal = avisoViajeIdLocal;
    }

    public short getAvisoViajeEstatusSinc() {
        return avisoViajeEstatusSinc;
    }

    public void setAvisoViajeEstatusSinc(short avisoViajeEstatusSinc) {
        this.avisoViajeEstatusSinc = avisoViajeEstatusSinc;
    }

    public String getAvisoViajeRecurso() {
        return avisoViajeRecurso;
    }

    public void setAvisoViajeRecurso(String avisoViajeRecurso) {
        this.avisoViajeRecurso = avisoViajeRecurso;
    }

    public String getAvisoViajePresentacion() {
        return avisoViajePresentacion;
    }

    public void setAvisoViajePresentacion(String avisoViajePresentacion) {
        this.avisoViajePresentacion = avisoViajePresentacion;
    }

    public int getAvisoViajeNoPiezas() {
        return avisoViajeNoPiezas;
    }

    public void setAvisoViajeNoPiezas(int avisoViajeNoPiezas) {
        this.avisoViajeNoPiezas = avisoViajeNoPiezas;
    }

    public int getAvisoViajePrecio() {
        return avisoViajePrecio;
    }

    public void setAvisoViajePrecio(int avisoViajePrecio) {
        this.avisoViajePrecio = avisoViajePrecio;
    }

    public int getAvisoViajeCaptura() {
        return avisoViajeCaptura;
    }

    public void setAvisoViajeCaptura(int avisoViajeCaptura) {
        this.avisoViajeCaptura = avisoViajeCaptura;
    }
}
