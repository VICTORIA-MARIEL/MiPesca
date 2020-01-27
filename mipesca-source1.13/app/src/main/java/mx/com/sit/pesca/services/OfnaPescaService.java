package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.OfnaPescaList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface OfnaPescaService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboOfnasPesca.html
    @GET("obtenerComboOfnasPesca.html")
    Call<OfnaPescaList> getOfnasPesca();
}
