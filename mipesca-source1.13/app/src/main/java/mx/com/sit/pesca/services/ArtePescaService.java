package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.RecursoList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ArtePescaService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboArtesPesca.html
    @GET("obtenerComboArtesPesca.html")
    Call<ArtePescaList> getArtesPesca();
}
