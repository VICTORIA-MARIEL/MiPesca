package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.PresentacionList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ComunidadService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboComunidades.html
    @GET("obtenerComboComunidades.html")
    Call<ComunidadList> getComunidades();
}
