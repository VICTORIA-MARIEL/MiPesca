package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.LoginList;
import mx.com.sit.pesca.dto.PescadorList;
import mx.com.sit.pesca.dto.ViajeList;
import mx.com.sit.pesca.entity.UsuarioEntity;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsuarioService {
    //http://sgovela.mine.nu:8080/pe/operacion/altaPescador.html
    @FormUrlEncoded
    @POST("altaPescador.html")
    public Call<PescadorList> altaPescador(
            @Field("usuarioLogin") String usuarioLogin,
            @Field("usuarioContrasena") String usuarioContrasena,
            @Field("pescadorNombre") String pescadorNombre,
            @Field("pescadorTelefono") String pescadorTelefono,
            @Field("pescadorEsIndependiente") short pescadorEsIndependiente,
            @Field("pescadorCorreo") String pescadorCorreo,
            @Field("municipioDesc") String municipioDesc,
                    @Field("comunidadDesc") String comunidadDesc,
                    @Field("cooperativaDesc") String cooperativaDesc,
                    @Field("municipioId") Integer municipioId,
                    @Field("comunidadId") Integer comunidadId,
                    @Field("cooperativaId") Integer cooperativaId
    );

    @FormUrlEncoded
    @POST("editarPescador.html")
    public Call<PescadorList> editarPescador(
            @Field("usuarioId") int usuarioId,
            @Field("usuarioLogin") String usuarioLogin,
            @Field("usuarioContrasena") String usuarioContrasena,
            @Field("pescadorId") int pescadorId,
            @Field("pescadorNombre") String pescadorNombre,
            @Field("pescadorTelefono") String pescadorTelefono,
            @Field("pescadorEsIndependiente") short pescadorEsIndependiente,
            @Field("pescadorCorreo") String pescadorCorreo,
            @Field("municipioDesc") String municipioDesc,
            @Field("comunidadDesc") String comunidadDesc,
            @Field("cooperativaDesc") String cooperativaDesc,
            @Field("municipioId") Integer municipioId,
            @Field("comunidadId") Integer comunidadId,
            @Field("cooperativaId") Integer cooperativaId
    );

    @FormUrlEncoded
    @POST("existe.html")
    public Call<PescadorList> existe(
            @Field("usuarioLogin") String usuarioLogin
    );


    @FormUrlEncoded
    @POST("validar.html")
    public Call<LoginList> validar(
            @Field("usuarioLogin") String usuarioLogin,
            @Field("usuarioContrasena") String usuarioContrasena
    );
}
