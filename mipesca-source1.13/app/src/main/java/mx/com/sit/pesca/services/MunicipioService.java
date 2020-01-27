package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.MunicipioList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MunicipioService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboMunicipios.html
    @GET("obtenerComboMunicipios.html")
    Call<MunicipioList> getMunicipios();
}
