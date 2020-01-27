package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.entity.ArtePescaEntity;
import mx.com.sit.pesca.services.ArtePescaService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtePescaDAO {

    private static final String TAG = "ArtePescaDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public ArtePescaDAO(){

    }

    public ArtePescaDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando ArtePescaDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando ArtePescaDAO(int usuarioId, Context context)");
    }

    public void insertar(ArtePescaList artespesca){
        Log.d(TAG, "Iniciando insertar(ArtePescaList artespesca)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(artespesca.getArtesPesca());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(ArtePescaList artespesca)");
    }


    public int getArtesPesca(){
        Log.d(TAG, "Iniciando getArtesPesca()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<ArtePescaEntity> list = realm.where(ArtePescaEntity.class).findAll();
        Log.d(TAG, "# ArtesPesca>"+list.size());
        Log.d(TAG, "Terminando getArtesPesca()");
        total = list.size();
        realm.close();

        return total;
    }


    public boolean consultarArtesPesca(){
        Log.d(TAG, "Iniciando consultarArtesPesca()");
        final StringBuilder error = new StringBuilder();
        ArtePescaService service = retrofit.create(ArtePescaService.class);
        Call<ArtePescaList> presCall = service.getArtesPesca();
        presCall.enqueue(new Callback<ArtePescaList>(){
            @Override
            public void onResponse(Call<ArtePescaList> call, Response<ArtePescaList> response) {
                ArtePescaList artepesca = response.body();
                insertar(artepesca);
            }
            @Override
            public void onFailure(Call<ArtePescaList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                final StringBuilder error = new StringBuilder();
            }
        });
        Log.d(TAG, "Terminando consultarArtesPesca()");
        return ("".equals(error.toString()))?true:false;
    }


}
