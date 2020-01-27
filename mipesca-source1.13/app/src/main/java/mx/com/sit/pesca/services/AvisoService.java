package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.AvisoList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AvisoService {
    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboArtesPesca.html
    @FormUrlEncoded
    @POST("buscarAvisos.html")
    public Call<AvisoList> insertAviso(
            @Field("aviso.avisoId") String avisoId,
            @Field("aviso.pescador.pescadorId") String pescadorId,
            @Field("aviso.avisoUsrRegistro.usuarioId") String avisoUsrRegistro,
            @Field("aviso.avisoFhRegistroStr") String avisoFhRegistro,
            @Field("aviso.avisoFhSolicitudStr") String avisoFhSolicitud,
            @Field("aviso.avisoPeriodoInicioStr") String avisoPeriodoInicio,
            @Field("aviso.avisoPeriodoFinStr") String avisoPeriodoFin,
            @Field("aviso.avisoDuracion") String avisoDuracion,
            @Field("aviso.avisoDiasEfectivos") String avisoDiasEfectivos,
            @Field("aviso.avisoFolio") String avisoFolio,
            @Field("aviso.avisoIdApp") String avisoIdApp,
            @Field("aviso.avisoFhSincronizacionStr") String avisoFhSincronizacionStr,
            @Field("aviso.avisoEstatus") String avisoEstatus,
            @Field("aviso.permiso.permisoId") String permisoId,
            @Field("aviso.avisoZonaPesca") String avisoZonaPesca,
            @Field("aviso.ofnapesca.ofnapescaId") String ofnapescaId,
            @Field("aviso.avisoEsPesqueriaAcuacultural") String avisoEsPesqueriaAcuacultural,
            @Field("aviso.sitio.sitioId") String sitioId,
            @Field("aviso.usuarioLogin") String usuarioLogin
    );


}


