package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.RecursoList;
import mx.com.sit.pesca.entity.RecursoEntity;
import mx.com.sit.pesca.services.RecursoService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecursoDAO {

    private static final String TAG = "RecursoDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public RecursoDAO(){

    }

    public RecursoDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando RecursoDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando RecursoDAO(int usuarioId, Context context)");
    }

    public void insertar(RecursoList recursos){
        Log.d(TAG, "Iniciando insertar(RecursoList recursos)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(recursos.getRecursos());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(RecursoList recursos)");
    }

    
    public boolean consultarRecursos(){
        Log.d(TAG, "Iniciando consultarRecursos()");
        final StringBuilder error = new StringBuilder();
        RecursoService service = retrofit.create(RecursoService.class);
        Call<RecursoList> presCall = service.getRecursos();
        presCall.enqueue(new Callback<RecursoList>(){

            @Override
            public void onResponse(Call<RecursoList> call, Response<RecursoList> response) {
                RecursoList recursos = response.body();
                insertar(recursos);
            }

            @Override
            public void onFailure(Call<RecursoList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarRecursos()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getRecursos(){
        Log.d(TAG, "Iniciando getRecursos()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<RecursoEntity> list = realm.where(RecursoEntity.class).findAll();
        Log.d(TAG, "# Recursos>"+list.size());
        Log.d(TAG, "Terminando getRecursos()");
        total = list.size();
        realm.close();
        return total;
    }


}
