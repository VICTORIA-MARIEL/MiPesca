package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.CooperativaList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CooperativaService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboCooperativas.html
    @GET("obtenerComboCooperativas.html")
    Call<CooperativaList> getCooperativas();
}
