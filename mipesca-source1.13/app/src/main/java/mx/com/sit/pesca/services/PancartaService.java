package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.PancartaList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PancartaService {

    @FormUrlEncoded
    @POST("obtenerComboPancartas.html")
    public Call<PancartaList> validarPancarta(
            @Field("filtro.estatusId") String estatusId,
            @Field("filtro.fecha") String fecha
    );
}


