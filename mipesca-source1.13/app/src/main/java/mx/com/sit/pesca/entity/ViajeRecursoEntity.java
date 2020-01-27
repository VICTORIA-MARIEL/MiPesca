package mx.com.sit.pesca.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import javax.annotation.Nullable;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import mx.com.sit.pesca.app.MyApp;
import mx.com.sit.pesca.util.Constantes;

public class ViajeRecursoEntity  extends RealmObject {
    @PrimaryKey
    private int id;
    private int viajeId;
    private int recursoId;
    private int presentacionId;
    private short veEsPrincipal;
    private int veCaptura;
    private int vePrecio;
    private int veNoPiezas;
    private short veOrden;
    @SerializedName("usuarioId")
    private int veUsrRegistro;
    @Nullable
    private Date veFhSincronizacion;
    @SerializedName("viajeIdApp")
    private int viajeIdLocal;
    @SerializedName("viajeRecursoIdApp")
    private int veIdLocal;
    private int viajeIdRemoto;
    @SerializedName("viajeRecursoId")
    private int veIdRemoto;
    private short veEstatusSinc;
    private short veEstatus;
    private String recursoDescripcion;
    private String presentacionDescripcion;


    public ViajeRecursoEntity(){

    }

    public ViajeRecursoEntity(
            int recursoId,
            int presentacionId,
            short veEsPrincipal,
            int veCaptura,
            int vePrecio,
            int veNoPiezas,
            short veOrden,
            int veUsrRegistro,
            String recursoDescripcion,
            String presentacionDescripcion,
            int viajeId
           ){

        this.id = MyApp.viajeRecursoID.incrementAndGet();
        this.viajeId = viajeId;
        this.recursoId = recursoId;
        this.presentacionId = presentacionId;
        this.veEsPrincipal = veEsPrincipal;
        this.veCaptura = veCaptura;
        this.vePrecio = vePrecio;
        this.veNoPiezas = veNoPiezas;
        this.veOrden = veOrden;
        this.veUsrRegistro = veUsrRegistro;
        this.veFhSincronizacion = null;
        this.viajeIdLocal = 0;
        this.veIdLocal = 0;
        this.viajeIdRemoto = 0;
        this.veIdRemoto = 0;
        this.veEstatusSinc = Constantes.ESTATUS_NO_SINCRONIZADO;
        this.veEstatus = (short)1;
        this.recursoDescripcion = recursoDescripcion;
        this.presentacionDescripcion = presentacionDescripcion;
    }


    public int getViajeIdRemoto() {
        return viajeIdRemoto;
    }

    public void setViajeIdRemoto(int viajeIdRemoto) {
        this.viajeIdRemoto = viajeIdRemoto;
    }

    public int getVeIdRemoto() {
        return veIdRemoto;
    }

    public void setVeIdRemoto(int veIdRemoto) {
        this.veIdRemoto = veIdRemoto;
    }

    public String getRecursoDescripcion() {
        return recursoDescripcion;
    }

    public void setRecursoDescripcion(String recursoDescripcion) {
        this.recursoDescripcion = recursoDescripcion;
    }

    public String getPresentacionDescripcion() {
        return presentacionDescripcion;
    }

    public void setPresentacionDescripcion(String presentacionDescripcion) {
        this.presentacionDescripcion = presentacionDescripcion;
    }

    public int getViajeIdLocal() {
        return viajeIdLocal;
    }

    public void setViajeIdLocal(int viajeIdLocal) {
        this.viajeIdLocal = viajeIdLocal;
    }

    public int getVeUsrRegistro() {
        return veUsrRegistro;
    }

    public void setVeUsrRegistro(int veUsrRegistro) {
        this.veUsrRegistro = veUsrRegistro;
    }

    public int getVeIdLocal() {
        return veIdLocal;
    }

    public void setVeIdLocal(int veIdLocal) {
        this.veIdLocal = veIdLocal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViajeId() {
        return viajeId;
    }

    public void setViajeId(int viajeId) {
        this.viajeId = viajeId;
    }

    public int getRecursoId() {
        return recursoId;
    }

    public void setRecursoId(int recursoId) {
        this.recursoId = recursoId;
    }

    public short getVeEsPrincipal() {
        return veEsPrincipal;
    }

    public void setVeEsPrincipal(short veEsPrincipal) {
        this.veEsPrincipal = veEsPrincipal;
    }

    public int getVeCaptura() {
        return veCaptura;
    }

    public void setVeCaptura(int veCaptura) {
        this.veCaptura = veCaptura;
    }

    public int getVePrecio() {
        return vePrecio;
    }

    public void setVePrecio(int vePrecio) {
        this.vePrecio = vePrecio;
    }

    public int getVeNoPiezas() {
        return veNoPiezas;
    }

    public void setVeNoPiezas(int veNoPiezas) {
        this.veNoPiezas = veNoPiezas;
    }

    public int getPresentacionId() {
        return presentacionId;
    }

    public void setPresentacionId(int presentacionId) {
        this.presentacionId = presentacionId;
    }

    public short getVeOrden() {
        return veOrden;
    }

    public void setVeOrden(short veOrden) {
        this.veOrden = veOrden;
    }

    public Date getVeFhSincronizacion() {
        return veFhSincronizacion;
    }

    public void setVeFhSincronizacion(Date veFhSincronizacion) {
        this.veFhSincronizacion = veFhSincronizacion;
    }

    public short getVeEstatusSinc() {
        return veEstatusSinc;
    }

    public void setVeEstatusSinc(short veEstatusSinc) {
        this.veEstatusSinc = veEstatusSinc;
    }

    public short getVeEstatus() {
        return veEstatus;
    }

    public void setVeEstatus(short veEstatus) {
        this.veEstatus = veEstatus;
    }
}
