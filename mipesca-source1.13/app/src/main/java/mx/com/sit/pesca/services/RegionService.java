package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.RegionList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RegionService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboRegiones.html
    @GET("obtenerComboRegiones.html")
    Call<RegionList> getRegiones();
}
