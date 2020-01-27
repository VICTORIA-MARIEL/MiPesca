package mx.com.sit.pesca.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import mx.com.sit.pesca.app.MyApp;
import mx.com.sit.pesca.util.Constantes;

public class SincronizacionEntity  extends RealmObject {
    @PrimaryKey
    private int id;
    private int pescadorId;
    @Required
    private String sincronizacionHora;
    private short sincronizacionLunes;
    private short sincronizacionMartes;
    private short sincronizacionMiercoles;
    private short sincronizacionJueves;
    private short sincronizacionViernes;
    private short sincronizacionSabado;
    private short sincronizacionDomingo;
    private short sincronizacionEstatus;
    @Required
    private Date sincronizacionFhRegistro;
    private int sincronizacionUsrRegistro;
    private short sincronizacionJobEstatus;

    public SincronizacionEntity(){

    }

    public SincronizacionEntity(
            int pescadorId,
            String sincronizacionHora,
            short sincronizacionLunes,
            short sincronizacionMartes,
            short sincronizacionMiercoles,
            short sincronizacionJueves,
            short sincronizacionViernes,
            short sincronizacionSabado,
            short sincronizacionDomingo,
            int sincronizacionUsrRegistro
    ){
        this.id = MyApp.sincronizacionID.incrementAndGet();
        this.pescadorId = pescadorId;
        this.sincronizacionHora = sincronizacionHora;
        this.sincronizacionLunes = sincronizacionLunes;
        this.sincronizacionMartes = sincronizacionMartes;
        this.sincronizacionMiercoles = sincronizacionMiercoles;
        this.sincronizacionJueves = sincronizacionJueves;
        this.sincronizacionViernes = sincronizacionViernes;
        this.sincronizacionSabado = sincronizacionSabado;
        this.sincronizacionDomingo = sincronizacionDomingo;
        this.sincronizacionEstatus = (short)1;
        this.sincronizacionFhRegistro = new Date();
        this.sincronizacionUsrRegistro = sincronizacionUsrRegistro;
        this.sincronizacionJobEstatus = (short) Constantes.JOB_CANCELADO;

    }

    public short getSincronizacionJobEstatus() {
        return sincronizacionJobEstatus;
    }

    public void setSincronizacionJobEstatus(short sincronizacionJobEstatus) {
        this.sincronizacionJobEstatus = sincronizacionJobEstatus;
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

    public String getSincronizacionHora() {
        return sincronizacionHora;
    }

    public void setSincronizacionHora(String sincronizacionHora) {
        this.sincronizacionHora = sincronizacionHora;
    }

    public short getSincronizacionLunes() {
        return sincronizacionLunes;
    }

    public void setSincronizacionLunes(short sincronizacionLunes) {
        this.sincronizacionLunes = sincronizacionLunes;
    }

    public short getSincronizacionMartes() {
        return sincronizacionMartes;
    }

    public void setSincronizacionMartes(short sincronizacionMartes) {
        this.sincronizacionMartes = sincronizacionMartes;
    }

    public short getSincronizacionMiercoles() {
        return sincronizacionMiercoles;
    }

    public void setSincronizacionMiercoles(short sincronizacionMiercoles) {
        this.sincronizacionMiercoles = sincronizacionMiercoles;
    }

    public short getSincronizacionJueves() {
        return sincronizacionJueves;
    }

    public void setSincronizacionJueves(short sincronizacionJueves) {
        this.sincronizacionJueves = sincronizacionJueves;
    }

    public short getSincronizacionViernes() {
        return sincronizacionViernes;
    }

    public void setSincronizacionViernes(short sincronizacionViernes) {
        this.sincronizacionViernes = sincronizacionViernes;
    }

    public short getSincronizacionSabado() {
        return sincronizacionSabado;
    }

    public void setSincronizacionSabado(short sincronizacionSabado) {
        this.sincronizacionSabado = sincronizacionSabado;
    }

    public short getSincronizacionDomingo() {
        return sincronizacionDomingo;
    }

    public void setSincronizacionDomingo(short sincronizacionDomingo) {
        this.sincronizacionDomingo = sincronizacionDomingo;
    }

    public short getSincronizacionEstatus() {
        return sincronizacionEstatus;
    }

    public void setSincronizacionEstatus(short sincronizacionEstatus) {
        this.sincronizacionEstatus = sincronizacionEstatus;
    }

    public Date getSincronizacionFhRegistro() {
        return sincronizacionFhRegistro;
    }

    public void setSincronizacionFhRegistro(Date sincronizacionFhRegistro) {
        this.sincronizacionFhRegistro = sincronizacionFhRegistro;
    }

    public int getSincronizacionUsrRegistro() {
        return sincronizacionUsrRegistro;
    }

    public void setSincronizacionUsrRegistro(int sincronizacionUsrRegistro) {
        this.sincronizacionUsrRegistro = sincronizacionUsrRegistro;
    }
}
