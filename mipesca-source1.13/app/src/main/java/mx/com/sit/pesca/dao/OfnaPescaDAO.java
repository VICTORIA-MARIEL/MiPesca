package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.OfnaPescaList;
import mx.com.sit.pesca.entity.OfnaPescaEntity;
import mx.com.sit.pesca.services.OfnaPescaService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfnaPescaDAO {

    private static final String TAG = "OfnaPescaDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public OfnaPescaDAO(){

    }

    public OfnaPescaDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando OfnaPescaDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando OfnaPescaDAO(int usuarioId, Context context)");
    }

    public void insertar(OfnaPescaList ofnaspesca){
        Log.d(TAG, "Iniciando insertar(OfnaPescaList ofnaspesca)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(ofnaspesca.getOfnasPesca());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(OfnaPescaList ofnaspesca)");
    }

    public  boolean consultarOfnasPesca(){
        Log.d(TAG, "Iniciando consultarOfnasPesca()");
        final StringBuilder error = new StringBuilder();
        OfnaPescaService service = retrofit.create(OfnaPescaService.class);
        Call<OfnaPescaList> presCall = service.getOfnasPesca();
        presCall.enqueue(new Callback<OfnaPescaList>(){

            @Override
            public void onResponse(Call<OfnaPescaList> call, Response<OfnaPescaList> response) {
                OfnaPescaList ofnapesca = response.body();
                insertar(ofnapesca);
            }

            @Override
            public void onFailure(Call<OfnaPescaList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarOfnasPesca()");
        return ("".equals(error.toString()))?true:false;

    }

    public int getOfnasPesca(){
        Log.d(TAG, "Iniciando getOfnasPesca()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<OfnaPescaEntity> list = realm.where(OfnaPescaEntity.class).findAll();
        Log.d(TAG, "# OfnasPesca>"+list.size());
        Log.d(TAG, "Terminando getOfnasPesca()");
        total = list.size();
        realm.close();
        return total;
    }


}
