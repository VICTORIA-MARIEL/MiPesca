package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.PermisoList;
import mx.com.sit.pesca.dto.PescadorList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PermisoService {
    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboPermisos.html
    @GET("obtenerComboPermisos.html")
    Call<PermisoList> getPermisos();

    @FormUrlEncoded
    @POST("buscarPermisos.html")
    public Call<PermisoList> insertPermiso(

            @Field("permiso.pescador.pescadorId") String pescadorId,
            @Field("permiso.permisoUsrRegistro.usuarioId") String usuarioId,
            //@Field("permiso.permisoFhRegistro") String permisoFhRegistro,
            @Field("permiso.permisoNumero") String permisoNumero,
            @Field("permiso.permisoNombreEmbarcacion") String permisoNombreEmbarcacion,
            @Field("permiso.permisoRnpaEmbarcacion") String permisoRnpaEmbarcacion,
            @Field("permiso.permisoMatricula") String permisoMatricula,
            @Field("permiso.permisoNoEmbarcaciones") String permisoNoEmbarcaciones,
            @Field("permiso.permisoSitioDesembarque") String permisoSitioDesembarque,
            @Field("permiso.permisoSitioDesembarqueClave") String permisoSitioDesembarqueClave,
            @Field("permiso.permisoZonaPesca") String permisoZonaPesca,
            @Field("permiso.permisoEstatus") String permisoEstatus,
            //@Field("permiso.permisoIdLocal") String permisoIdLocal,
            //@Field("permiso.permisoFhSincronizacion") String permisoFhSincronizacion,
            @Field("permiso.permisoTonelaje") String permisoTonelaje,
            @Field("permiso.permisoMarcaMotor") String permisoMarcaMotor,
            @Field("permiso.permisoPotenciaHp") String permisoPotenciaHp,
            @Field("permiso.permisoFhVigenciaInicioStr") String permisoFhVigenciaInicio,
            @Field("permiso.permisoFhVigenciaFinStr") String permisoFhVigenciaFin,
            @Field("permiso.permisoFhVigenciaDuracion") String permisoFhVigenciaDuracion,
            @Field("permiso.permisoFhExpedicionStr") String permisoFhExpedicion,
            @Field("permiso.permisoLugarExpedicion") String permisoLugarExpedicion,
            @Field("permiso.permisoParaPesqueria") String permisoParaPesqueria,
            @Field("permiso.permisoArteCantidad") String permisoArteCantidad,
            @Field("permiso.permisoArteCaracteristica") String permisoArteCaracteristica,
            @Field("permiso.permisoArtepesca") String permisoArtepesca,
            @Field("permiso.permisoTitular") String permisoTitular,
            @Field("permiso.permisoMunicipio") String permisoMunicipio,
            @Field("permiso.permisoMunicipioId") String permisoMunicipioId,
            @Field("permiso.permisoEstado") String permisoEstado,
            @Field("permiso.permisoEstadoId") String permisoEstadoId,
            //@Field("permiso.permisoEstatusSinc") String permisoEstatusSinc,
            @Field("usuarioLogin") String usuarioLogin
    );


    @FormUrlEncoded
    @POST("existePermiso.html")
    public Call<PermisoList> existePer(
            @Field("permisoNumero") String permisoNumero
    );
}


