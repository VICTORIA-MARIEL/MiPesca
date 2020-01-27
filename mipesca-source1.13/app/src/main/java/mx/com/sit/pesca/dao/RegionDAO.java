package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.RegionList;
import mx.com.sit.pesca.entity.RegionEntity;
import mx.com.sit.pesca.services.RegionService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegionDAO {

    private static final String TAG = "RegionDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public RegionDAO(){

    }

    public RegionDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando RegionDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando RegionDAO(int usuarioId, Context context)");
    }

    public void insertar(RegionList region){
        Log.d(TAG, "Iniciando insertar(RegionList region)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(region.getRegiones());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(RegionList region)");
    }


    public boolean consultarRegiones(){
        Log.d(TAG, "Iniciando consultarRegiones()");
        final StringBuilder error = new StringBuilder();
        RegionService service = retrofit.create(RegionService.class);
        Call<RegionList> regCall = service.getRegiones();
        regCall.enqueue(new Callback<RegionList>(){

            @Override
            public void onResponse(Call<RegionList> call, Response<RegionList> response) {
                RegionList region = response.body();
                insertar(region);
            }

            @Override
            public void onFailure(Call<RegionList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarPermisos()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getRegiones(){
        Log.d(TAG, "Iniciando getRegiones()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<RegionEntity> list = realm.where(RegionEntity.class).findAll();
        Log.d(TAG, "# Regiones>"+list.size());
        Log.d(TAG, "Terminando getRegiones()");
        total = list.size();
        realm.close();
        return total;
    }

}
