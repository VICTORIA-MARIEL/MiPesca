package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.SplashActivity;
import mx.com.sit.pesca.dto.PresentacionList;
import mx.com.sit.pesca.entity.PresentacionEntity;
import mx.com.sit.pesca.services.PresentacionService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PresentacionDAO {

    private static final String TAG = "PresentacionDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public PresentacionDAO(){

    }

    public PresentacionDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando PresentacionDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando PresentacionDAO(int usuarioId, Context context)");
    }

    public void insertar(PresentacionList presentaciones){
        Log.d(TAG, "Iniciando insertar(PresentacionList presentaciones)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(presentaciones.getPresentaciones());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(PresentacionList presentaciones)");
    }



    public boolean consultarPresentaciones(){
        Log.d(TAG, "Iniciando consultarPresentaciones()");
        final StringBuilder error = new StringBuilder();
        PresentacionService service = retrofit.create(PresentacionService.class);
        Call<PresentacionList> presCall = service.getPresentaciones();
        presCall.enqueue(new Callback<PresentacionList>(){

            @Override
            public void onResponse(Call<PresentacionList> call, Response<PresentacionList> response) {
                PresentacionList presentaciones = response.body();
                insertar(presentaciones);
            }

            @Override
            public void onFailure(Call<PresentacionList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarPresentaciones()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getPresentaciones(){
        Log.d(TAG, "Iniciando getPresentaciones()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<PresentacionEntity> list = realm.where(PresentacionEntity.class).findAll();
        Log.d(TAG, "# Presentaciones>"+list.size());
        Log.d(TAG, "Terminando getPresentaciones()");
        total = list.size();
        realm.close();
        return total;
    }


}
