package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.ViajeRecursoList;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.services.ViajeRecursoService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViajeRecursoDAO {

    private static final String TAG = "ViajeRecursoDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public ViajeRecursoDAO(){

    }

    public ViajeRecursoDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando ViajeRecursoDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando ViajeRecursoDAO(int usuarioId, Context context)");
    }


    public void sincronizarViajesRecursos(){
        Log.d(TAG, "Iniciando enviarViajesRecursos()");
        realm = Realm.getDefaultInstance();
        UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class).equalTo("id",usuarioId).findFirst();
        String usuarioLogin = "";
        if(usuarioEntity!=null) {
            usuarioLogin = usuarioEntity.getUsuarioLogin();
        }
        RealmResults<ViajeRecursoEntity> list = realm.where(ViajeRecursoEntity.class)
                                                .equalTo("veUsrRegistro",usuarioId)
                                                .equalTo("veEstatusSinc", Constantes.ESTATUS_NO_SINCRONIZADO)
                                                .equalTo("veEstatus",1)
                                                .findAll();
        for(ViajeRecursoEntity vrDTO: list) {
            ViajeEntity viajeEntity = realm.where(ViajeEntity.class).equalTo("id",vrDTO.getViajeId()).findFirst();
            String viajeIdRemoto = "";
            if(viajeEntity!=null) {
                viajeIdRemoto = "" + viajeEntity.getViajeIdRemoto();
            }

            ViajeRecursoService service = retrofit.create(ViajeRecursoService.class);
            Call<ViajeRecursoList> viajeRecursoCall = service.insertViajeRecurso(
                    "" + vrDTO.getId(), //viajeRecursoId
                    "" +vrDTO.getViajeId(),
                    "" +vrDTO.getRecursoId(),
                    "" +vrDTO.getPresentacionId(),
                    "" +vrDTO.getVeEsPrincipal(),
                    "" +vrDTO.getVeCaptura(),
                    "" +vrDTO.getVePrecio(),
                    "" +vrDTO.getVeNoPiezas(),
                    "" +vrDTO.getVeOrden(),
                    "" +vrDTO.getVeUsrRegistro(),
                    "" +Util.convertDateToString(vrDTO.getVeFhSincronizacion(), Constantes.formatoFechaHora),
                    "" +vrDTO.getVeIdLocal(),
                    "" +vrDTO.getViajeIdLocal(),
                    ""+vrDTO.getVeEstatus(),
                    usuarioLogin,
                    viajeIdRemoto
            );

            viajeRecursoCall.enqueue(new Callback<ViajeRecursoList>() {
                @Override
                public void onResponse(Call<ViajeRecursoList> call, Response<ViajeRecursoList> response) {
                    ViajeRecursoList viajeRecursoList = response.body();
                    ViajeRecursoEntity viajeRecursoRegresado = viajeRecursoList.getViajeRecurso();
                    Log.d(TAG, "Viaje Recurso Regresado:" + viajeRecursoRegresado);
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    ViajeRecursoEntity viajeRecurso = realm.where(ViajeRecursoEntity.class).equalTo("id", viajeRecursoRegresado.getVeIdLocal()).findFirst();
                    viajeRecurso.setVeEstatusSinc((short)Constantes.ESTATUS_SINCRONIZADO);
                    viajeRecurso.setVeFhSincronizacion(new Date());
                    viajeRecurso.setViajeIdRemoto(viajeRecursoRegresado.getViajeIdRemoto());
                    viajeRecurso.setVeIdRemoto(viajeRecursoRegresado.getVeIdRemoto());
                    realm.copyToRealmOrUpdate(viajeRecurso);
                    realm.commitTransaction();
                    realm.close();
                    Log.d(TAG, "" + viajeRecursoList.toString());
                }

                @Override
                public void onFailure(Call<ViajeRecursoList> call, Throwable t) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        realm.close();
        Log.d(TAG, "Terminando enviarViajesRecursos()");
    }

    public int totalViajesRecursos(){
        Log.d(TAG, "Iniciando totalViajesRecursos()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<ViajeRecursoEntity> list = realm.where(ViajeRecursoEntity.class).findAll();
        Log.d(TAG, "# viajesRecursos>"+list.size());
        for(ViajeRecursoEntity dto: list){
            Log.d(TAG, ""+dto.toString());
        }
        total = list.size();
        realm.close();
        Log.d(TAG, "Terminando totalViajesRecursos()");
        return total;
    }



}
