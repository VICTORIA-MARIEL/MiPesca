package mx.com.sit.pesca.services;

import mx.com.sit.pesca.dto.CooperativaList;
import mx.com.sit.pesca.dto.EdoPesqueriaRecursoList;
import mx.com.sit.pesca.dto.RecursoList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecursoService {

    //http://sgovela.mine.nu:8080/pe/externos/obtenerComboRecursos.html
    @GET("obtenerComboRecursos.html")
    Call<RecursoList> getRecursos();

    @GET("obtenerComboRecursosPesqueria.html")
    Call<EdoPesqueriaRecursoList> getRecursosPesqueria();

}
