package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ViajeList;
import mx.com.sit.pesca.dto.ViajeRecursoList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ViajeRecursoService {
    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboArtesPesca.html
    @FormUrlEncoded
    @POST("buscarViajesRecursos.html")
    public Call<ViajeRecursoList> insertViajeRecurso(
            @Field("viajeRecurso.viajeRecursoId") String viajeRecursoId,
            @Field("viajeRecurso.viaje.viajeId") String veViajeId,
            @Field("viajeRecurso.recurso.recursoId") String recursoId,
            @Field("viajeRecurso.presentacion.presentacionId") String presentacionId,
            @Field("viajeRecurso.veEsPrincipal") String veEsPrincipal,
            @Field("viajeRecurso.veCaptura") String veCaptura,
            @Field("viajeRecurso.vePrecio") String vePrecio,
            @Field("viajeRecurso.veNoPiezas") String veNoPiezas,
            @Field("viajeRecurso.veOrden") String veOrden,
            @Field("viajeRecurso.veUsrRegistro.usuarioId") String veUsrRegistro,
            @Field("viajeRecurso.veFhSincronizacionStr") String veFhSincronizacionStr,
            @Field("viajeRecurso.viajeRecursoIdApp") String viajeRecursoIdApp,
            @Field("viajeRecurso.viajeIdApp") String veViajeIdApp,
            @Field("viajeRecurso.veEstatus") String veEstatus,
            @Field("viajeRecurso.usuarioLogin") String usuarioLogin,
            @Field("viajeRecurso.viajeIdRemoto") String viajeIdRemoto

    );


}


