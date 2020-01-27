package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.RegionList;
import mx.com.sit.pesca.dto.SitioList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SitioService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboSitios.html
    @GET("obtenerComboSitios.html")
    Call<SitioList> getSitios();
}
