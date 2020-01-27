package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.EdoPesqueriaRecursoList;
import mx.com.sit.pesca.dto.RecursoList;
import mx.com.sit.pesca.entity.EdoPesqueriaRecursoEntity;
import mx.com.sit.pesca.entity.RecursoEntity;
import mx.com.sit.pesca.services.RecursoService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EdoPesqueriaRecursoDAO {

    private static final String TAG = "EdoPesqueriaRecursoDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public EdoPesqueriaRecursoDAO(){

    }

    public EdoPesqueriaRecursoDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando EdoPesqueriaRecursoDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando EdoPesqueriaRecursoDAO(int usuarioId, Context context)");
    }

    public void insertar(EdoPesqueriaRecursoList pesqueriaRecursos){
        Log.d(TAG, "Iniciando insertar(EdoPesqueriaRecursoList pesqueriaRecursos)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(pesqueriaRecursos.getPesqueriaRecursos());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(EdoPesqueriaRecursoList pesqueriaRecursos)");
    }

    
    public boolean consultarPesqueriaRecursos(){
        Log.d(TAG, "Iniciando consultarPesqueriaRecursos()");
        final StringBuilder error = new StringBuilder();
        RecursoService service = retrofit.create(RecursoService.class);
        Call<EdoPesqueriaRecursoList> presCall = service.getRecursosPesqueria();
        presCall.enqueue(new Callback<EdoPesqueriaRecursoList>(){

            @Override
            public void onResponse(Call<EdoPesqueriaRecursoList> call, Response<EdoPesqueriaRecursoList> response) {
                EdoPesqueriaRecursoList pesqueriaRecursos = response.body();
                insertar(pesqueriaRecursos);
            }

            @Override
            public void onFailure(Call<EdoPesqueriaRecursoList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error:" + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarPesqueriaRecursos()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getPesqueriaRecursos(){
        Log.d(TAG, "Iniciando getPesqueriaRecursos()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<EdoPesqueriaRecursoEntity> list = realm.where(EdoPesqueriaRecursoEntity.class).findAll();
        Log.d(TAG, "# Pesqueria Recursos>"+list.size());
        Log.d(TAG, "Terminando getPesqueriaRecursos()");
        total = list.size();
        realm.close();
        return total;
    }


}
