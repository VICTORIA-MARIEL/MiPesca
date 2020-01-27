package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.PermisoList;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.services.PermisoService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PermisoDAO {

    private static final String TAG = "PermisoDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public PermisoDAO(){

    }

    public PermisoDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando PermisoDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando PermisoDAO(int usuarioId, Context context)");
    }


  /*  public void enviarPermisos(){
        Log.d(TAG, "Iniciando enviarPermisos()");
        realm = Realm.getDefaultInstance();
        RealmResults<PermisoEntity> list = realm.where(PermisoEntity.class).findAll();
        Call<PermisoList> permisoCall = null;
        for(PermisoEntity permisoDTO: list) {
            PermisoService service = retrofit.create(PermisoService.class);
            permisoCall = service.insertPermiso(
                    "" + permisoDTO.getId(),
                    "" +permisoDTO.getPescadorId(),
                    "" +permisoDTO.getUsuarioId(),
                    "" +permisoDTO.getPermisoFhRegistro(),
                    "" +permisoDTO.getPermisoNumero(),
                    "" +permisoDTO.getPermisoNombreEmbarcacion(),
                    "" +permisoDTO.getPermisoRnpaEmbarcacion(),
                    "" +permisoDTO.getPermisoMatricula(),
                    "" +permisoDTO.getPermisoNoEmbarcaciones(),
                    "" +permisoDTO.getPermisoSitioDesembarque(),
                    "" +permisoDTO.getPermisoSitioDesembarqueClave(),
                    "" +permisoDTO.getPermisoZonaPesca(),
                    "" +permisoDTO.getPermisoEstatus(),
                    "" +permisoDTO.getPermisoIdLocal(),
                    "" +permisoDTO.getPermisoFhSincronizacion(),
                    "" +permisoDTO.getPermisoTonelaje(),
                    "" +permisoDTO.getPermisoMarcaMotor(),
                    "" +permisoDTO.getPermisoPotenciaHp(),
                    "" +permisoDTO.getPermisoFhVigenciaInicio(),
                    "" +permisoDTO.getPermisoFhVigenciaFin(),
                    "" + permisoDTO.getPermisoFhVigenciaDuracion(),
                    ""+permisoDTO.getPermisoFhExpedicion(),
                    "" + permisoDTO.getPermisoParaPesqueria(),
                    "" + permisoDTO.getPermisoArteCantidad(),
                    "" + permisoDTO.getArtePescaId(),
                    ""+permisoDTO.getPermisoArteCaracteristica(),
                    ""+permisoDTO.getPermisoLugarExpedicion(),
                    ""+permisoDTO.getPermisoEstatusSinc(),
                    "" + permisoDTO.getPermisoTitular(),
                    "" + permisoDTO.getPermisoArtepesca(),
                    "" + permisoDTO.getPermisoEstado(),
                    "" + permisoDTO.getPermisoEstadoId(),
                    "" + permisoDTO.getPermisoMunicipio(),
                    "" + permisoDTO.getPermisoMunicipioId()

            );
            realm.close();
        }
        permisoCall.enqueue(new Callback<PermisoList>() {
            @Override
            public void onResponse(Call<PermisoList> call, Response<PermisoList> response) {
                PermisoList permisoList = response.body();
                Log.d(TAG, "" + permisoList.toString());
            }

            @Override
            public void onFailure(Call<PermisoList> call, Throwable t) {
                Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Log.d(TAG, "Terminando enviarPermisos()");
    }*/

    public void insertar(PermisoList permiso){
        Log.d(TAG, "Iniciando insertar(PermisoList permis)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(permiso.getPermisos());
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertar(PermisoList permiso)");
    }


    public  boolean consultarPermisos(){
        Log.d(TAG, "Iniciando consultarPermisos()");
        final StringBuilder error = new StringBuilder();
        PermisoService service = retrofit.create(PermisoService.class);
        Call<PermisoList> perCall = service.getPermisos();
        perCall.enqueue(new Callback<PermisoList>(){

            @Override
            public void onResponse(Call<PermisoList> call, Response<PermisoList> response) {
                PermisoList permiso = response.body();
                insertar(permiso);
            }

            @Override
            public void onFailure(Call<PermisoList> call, Throwable t) {
                if(contexto!=null) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
                error.append("0");
            }
        });
        Log.d(TAG, "Terminando consultarPermisos()");
        return ("".equals(error.toString()))?true:false;
    }

    public int getPermisos(){
        Log.d(TAG, "Iniciando getPermisos()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<PermisoEntity> list = realm.where(PermisoEntity.class).findAll();
        Log.d(TAG, "# Permisos>"+list.size());
        Log.d(TAG, "Terminando getPermisos()");
        total = list.size();
        realm.close();
        return total;
    }

}
