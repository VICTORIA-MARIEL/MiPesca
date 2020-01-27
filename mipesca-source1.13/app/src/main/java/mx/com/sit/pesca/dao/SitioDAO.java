package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.SitioList;
import mx.com.sit.pesca.entity.SitioEntity;
import mx.com.sit.pesca.services.SitioService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SitioDAO {

    private static final String TAG = "SitioDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public SitioDAO(){

    }

    public SitioDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando SitioDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando SitioDAO(int usuarioId, Context context)");
    }

    public void insertar(SitioList sitio){
        Log.d(TAG, "Iniciando insertar(SitioList sitio)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(sitio.getSitios());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(SitioList sitio)");
    }


    public boolean consultarSitios(){
        Log.d(TAG, "Iniciando consultarSitios()");
        final StringBuilder error = new StringBuilder();
        SitioService service = retrofit.create(SitioService.class);
        Call<SitioList> sitCall = service.getSitios();
        sitCall.enqueue(new Callback<SitioList>(){

            @Override
            public void onResponse(Call<SitioList> call, Response<SitioList> response) {
                SitioList sitio = response.body();
                insertar(sitio);
            }

            @Override
            public void onFailure(Call<SitioList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarSitios()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getSitios(){
        Log.d(TAG, "Iniciando getSitios()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<SitioEntity> list = realm.where(SitioEntity.class).findAll();
        Log.d(TAG, "# Sitios>"+list.size());
        Log.d(TAG, "Terminando getSitios()");
        total = list.size();
        realm.close();
        return total;
    }

}
