package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.PresentacionList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PresentacionService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboPresentaciones.html
    @GET("obtenerComboPresentaciones.html")
    Call<PresentacionList> getPresentaciones();
}
