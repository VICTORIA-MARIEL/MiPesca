package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.MunicipioList;
import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;
import mx.com.sit.pesca.services.ComunidadService;
import mx.com.sit.pesca.services.MunicipioService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MunicipioDAO {

    private static final String TAG = "MunicipioDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public MunicipioDAO(){

    }

    public MunicipioDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando MunicipioDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando MunicipioDAO(int usuarioId, Context context)");
    }

    public void insertar(MunicipioList municipios){
        Log.d(TAG, "Iniciando insertar(MunicipioList municipios)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(municipios.getMunicipios());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(MunicipioList municipios)");
    }


    public boolean consultarMunicipios(){
        Log.d(TAG, "Iniciando consultarMunicipios()");
        final StringBuilder error = new StringBuilder();
        MunicipioService service = retrofit.create(MunicipioService.class);
        Call<MunicipioList> presCall = service.getMunicipios();
        presCall.enqueue(new Callback<MunicipioList>(){

            @Override
            public void onResponse(Call<MunicipioList> call, Response<MunicipioList> response) {
                MunicipioList municipios = response.body();
                insertar(municipios);
            }

            @Override
            public void onFailure(Call<MunicipioList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error:" + t.toString(), Toast.LENGTH_LONG).show();
                    error.append("0");

                }
            }
        });
        Log.d(TAG, "Terminando consultarMunicipios()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getMunicipios(){
        Log.d(TAG, "Iniciando getMunicipios()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<MunicipioEntity> list = realm.where(MunicipioEntity.class).findAll();
        Log.d(TAG, "# Municipios>"+list.size());
        Log.d(TAG, "Terminando getMunicipios()");
        total = list.size();
        realm.close();
        return total;
    }


}
