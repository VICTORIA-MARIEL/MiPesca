package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.CooperativaList;
import mx.com.sit.pesca.entity.CooperativaEntity;
import mx.com.sit.pesca.services.CooperativaService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CooperativaDAO {

    private static final String TAG = "CooperativaDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public CooperativaDAO(){

    }

    public CooperativaDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando CooperativaDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando CooperativaDAO(int usuarioId, Context context)");
    }

    public void insertar(CooperativaList cooperativas){
        Log.d(TAG, "Iniciando insertar(CooperativaList cooperativas)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(cooperativas.getCooperativas());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(CooperativaList cooperativas)");
    }


    public boolean consultarCooperativas(){
        Log.d(TAG, "Iniciando consultarCooperativas()");
        final StringBuilder error = new StringBuilder();
        CooperativaService service = retrofit.create(CooperativaService.class);
        Call<CooperativaList> presCall = service.getCooperativas();
        presCall.enqueue(new Callback<CooperativaList>(){
            @Override
            public void onResponse(Call<CooperativaList> call, Response<CooperativaList> response) {
                CooperativaList cooperativa = response.body();
                insertar(cooperativa);
            }

            @Override
            public void onFailure(Call<CooperativaList> call, Throwable t) {
                if(contexto!=null){
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarCooperativas()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getCooperativas(){
        Log.d(TAG, "Iniciando getCooperativas()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<CooperativaEntity> list = realm.where(CooperativaEntity.class).findAll();
        Log.d(TAG, "# Cooperativas>"+list.size());
        Log.d(TAG, "Terminando getCooperativas()");
        total = list.size();
        realm.close();
        return total;
    }


}
