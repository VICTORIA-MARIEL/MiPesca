package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import io.realm.Realm;
import mx.com.sit.pesca.entity.SincronizacionEntity;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SincronizacionDAO {

    private static final String TAG = "SincronizacionDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public SincronizacionDAO(){

    }

    public SincronizacionDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando SincronizacionDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando SincronizacionDAO(int usuarioId, Context context)");
    }

    public void insert(SincronizacionEntity sincronizacion){
        Log.d(TAG, "Iniciando insert(SincronizacionEntity sincronizacion)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(sincronizacion);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insert(SincronizacionEntity sincronizacion)");
    }


}
