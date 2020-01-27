package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.MensajeList;
import mx.com.sit.pesca.dto.PescadorList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MensajeService {
    //http://sgovela.mine.nu:8080/pe/operacion/insertarMensaje.html
    @FormUrlEncoded
    @POST("insertarMensaje.html")
    public Call<MensajeList> insertarMensaje(
            @Field("mensaje.mensajeEstatus") short mensajeEstatus,
            @Field("mensaje.mensajeTelefono") String mensajeTelefono,
            @Field("mensaje.mensajeDesc") String mensajeDesc
    );


}
