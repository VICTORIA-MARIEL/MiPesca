package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.AvisoList;
import mx.com.sit.pesca.dto.AvisoViajeList;
import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.CooperativaList;
import mx.com.sit.pesca.dto.OfnaPescaList;
import mx.com.sit.pesca.dto.PermisoList;
import mx.com.sit.pesca.dto.PresentacionList;
import mx.com.sit.pesca.dto.RecursoList;
import mx.com.sit.pesca.dto.RegionList;
import mx.com.sit.pesca.dto.SitioList;
import mx.com.sit.pesca.dto.ViajeList;
import mx.com.sit.pesca.dto.ViajeRecursoList;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.services.ArtePescaService;
import mx.com.sit.pesca.services.AvisoService;
import mx.com.sit.pesca.services.AvisoViajeService;
import mx.com.sit.pesca.services.ComunidadService;
import mx.com.sit.pesca.services.CooperativaService;
import mx.com.sit.pesca.services.OfnaPescaService;
import mx.com.sit.pesca.services.PermisoService;
import mx.com.sit.pesca.services.PresentacionService;
import mx.com.sit.pesca.services.RecursoService;
import mx.com.sit.pesca.services.RegionService;
import mx.com.sit.pesca.services.SitioService;
import mx.com.sit.pesca.services.ViajeRecursoService;
import mx.com.sit.pesca.services.ViajeService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComunidadDAO {

    private static final String TAG = "ComunidadDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public ComunidadDAO(){

    }

    public ComunidadDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando ComunidadDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando ComunidadDAO(int usuarioId, Context context)");
    }

    public void insertar(ComunidadList comunidades){
        Log.d(TAG, "Iniciando insertar(ComunidadList comunidades)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(comunidades.getComunidades());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(ComunidadList comunidades)");
    }


    public boolean consultarComunidades(){
        Log.d(TAG, "Iniciando consultarComunidades()");
        final StringBuilder error = new StringBuilder();
        ComunidadService service = retrofit.create(ComunidadService.class);
        Call<ComunidadList> presCall = service.getComunidades();
        presCall.enqueue(new Callback<ComunidadList>(){

            @Override
            public void onResponse(Call<ComunidadList> call, Response<ComunidadList> response) {
                ComunidadList comunidades = response.body();
                insertar(comunidades);
            }

            @Override
            public void onFailure(Call<ComunidadList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                    error.append("0");

                }
            }
        });
        Log.d(TAG, "Terminando consultarComunidades()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getComunidades(){
        Log.d(TAG, "Iniciando getComunidades()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<ComunidadEntity> list = realm.where(ComunidadEntity.class).findAll();
        Log.d(TAG, "# Comunidades>"+list.size());
        Log.d(TAG, "Terminando getComunidades()");
        total = list.size();
        realm.close();
        return total;
    }


}
