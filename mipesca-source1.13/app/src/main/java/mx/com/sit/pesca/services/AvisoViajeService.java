package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.AvisoViajeList;
import mx.com.sit.pesca.dto.ViajeRecursoList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AvisoViajeService {
    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboArtesPesca.html
    @FormUrlEncoded
    @POST("buscarAvisosViajes.html")
    public Call<AvisoViajeList> insertAvisoViaje(
            @Field("avisoViaje.avisoViajeId") String avisoViajeId,
            @Field("avisoViaje.aviso.avisoId") String avisoId,
            @Field("avisoViaje.viajeRecursoId") String viajeRecursoId,
            @Field("avisoViaje.avisoViajeTotal") String avisoViajeTotal,
            @Field("avisoViaje.avisoViajeFhRegistroStr") String avisoViajeFhRegistroStr,
            @Field("avisoViaje.avisoViajeUsrRegistro.usuarioId") String avisoViajeUsrRegistro,
            @Field("avisoViaje.avisoViajeFhSincronizacionStr") String avisoViajeFhSincronizacionStr,
            @Field("avisoViaje.avisoIdApp") String avisoIdApp,
            @Field("avisoViaje.avisoViajeDeclarado") String avisoViajeDeclarado,
            @Field("avisoViaje.avisoViajeEstatus") String avisoViajeEstatus,
            @Field("avisoViaje.avisoViajeIdApp") String avisoViajeIdApp,

            @Field("avisoViaje.usuarioLogin") String usuarioLogin,
            @Field("avisoViaje.avisoIdRemoto") String avisoIdRemoto,
            @Field("avisoViaje.viajeRecursoIdRemoto") String veIdRemoto
    );




}


