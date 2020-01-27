package mx.com.sit.pesca.services;

import java.util.ArrayList;
import java.util.List;

import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.ViajeList;
import mx.com.sit.pesca.dto.ViajesList;
import mx.com.sit.pesca.entity.ViajeEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ViajeService {
    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboArtesPesca.html
    @FormUrlEncoded
    @POST("buscarViajes.html")
    public Call<ViajeList> insertViaje(
            @Field("viaje.viajeId") String viajeId,
            @Field("viaje.viajeFhRegistroStr") String viajeFhRegistroStr,
            @Field("viaje.viajeFhFinalizoStr") String viajeFhFinalizoStr,
            @Field("viaje.municipio.municipioId") String municipioId,
            @Field("viaje.comunidad.comunidadId") String comunidadId,
            @Field("viaje.artepesca.artepescaId") String artepescaId,
            @Field("viaje.viajeCombustible") String viajeCombutible,
            @Field("viaje.viajeUsrRegistro.usuarioId") String viajeUsrRegistro,
            @Field("viaje.pescador.pescadorId") String pescadorId,
            @Field("viaje.viajeIdApp") String viajeIdApp,
            @Field("viaje.viajeFhSincronizacionStr") String viajeFhSincronizacionStr,
            @Field("viaje.viajeEstatus") String viajeEstatus,
            @Field("viaje.viajeFolio") String viajeFolio,
            @Field("viaje.usuarioLogin") String usuarioLogin


    );

    @FormUrlEncoded
    @POST("insertarViajes.html")
    Call<ViajesList> insertarViajes(
            @Field("viajes[]") ArrayList<ViajeEntity> viajes
    );



}


