package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.AvisoViajeList;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.services.AvisoViajeService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvisoViajeDAO {

    private static final String TAG = "AvisoViajeDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public AvisoViajeDAO(){

    }

    public AvisoViajeDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando AvisoViajeDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando AvisoViajeDAO(int usuarioId, Context context)");
    }

    public void sincronizarAvisosViajes(){
        Log.d(TAG, "Iniciando sincronizarAvisosViajes()");
        realm = Realm.getDefaultInstance();
        UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class).equalTo("id",usuarioId).findFirst();
        String usuarioLogin = "";
        if(usuarioEntity!=null) {
            usuarioLogin = usuarioEntity.getUsuarioLogin();
        }
        RealmResults<AvisoViajeEntity> list = realm.where(AvisoViajeEntity.class)
                .equalTo("avisoViajeUsrRegistro",usuarioId)
                .equalTo("avisoViajeEstatusSinc", Constantes.ESTATUS_NO_SINCRONIZADO)
                .equalTo("avisoViajeEstatus",1)
                .findAll();


        for(AvisoViajeEntity avDTO: list) {
            AvisoEntity avisoEntity = realm.where(AvisoEntity.class).equalTo("id",avDTO.getAvisoId()).findFirst();
            String avisoIdRemoto = "";
            if(avisoEntity!=null) {
                avisoIdRemoto = "" + avisoEntity.getAvisoIdRemoto();
            }

            ViajeRecursoEntity viajeRecursoEntity = realm.where(ViajeRecursoEntity.class).equalTo("id",avDTO.getViajeRecursoId()).findFirst();
            String veIdRemoto = "";
            if(viajeRecursoEntity!=null) {
                veIdRemoto = "" + viajeRecursoEntity.getVeIdRemoto();
            }


            AvisoViajeService service = retrofit.create(AvisoViajeService.class);
            Call<AvisoViajeList> avisoViajeCall = service.insertAvisoViaje(
                    "" + avDTO.getId(),
                    "" + avDTO.getAvisoId(),
                    "" + avDTO.getViajeRecursoId(),
                    "" + avDTO.getAvisoViajeTotal(),
                    "" +Util.convertDateToString(avDTO.getAvisoViajeFhRegistro(), Constantes.formatoFechaHora),
                    "" + avDTO.getAvisoViajeUsrRegistro(),
                    "" + Util.convertDateToString(avDTO.getAvisoViajeFhSincronizacion(), Constantes.formatoFechaHora),
                    "" + avDTO.getAvisoIdLocal(),
                    "" + avDTO.getAvisoViajeEstatusDeclarado(),
                    "" + avDTO.getAvisoViajeEstatus(),
                    "" + avDTO.getAvisoViajeIdLocal(),
                     "" + usuarioLogin,
                    "" + avisoIdRemoto,
                    "" + veIdRemoto
            );

            avisoViajeCall.enqueue(new Callback<AvisoViajeList>() {
                @Override
                public void onResponse(Call<AvisoViajeList> call, Response<AvisoViajeList> response) {
                    AvisoViajeList avisoViajeList = response.body();
                    AvisoViajeEntity avisoViajeRegresado = avisoViajeList.getAvisoViaje();
                    Log.d(TAG, "Aviso Viaje Regresado:" + avisoViajeRegresado);
                    if(avisoViajeRegresado!=null) {
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        AvisoViajeEntity avisoViaje = realm.where(AvisoViajeEntity.class).equalTo("id", avisoViajeRegresado.getAvisoViajeIdLocal()).findFirst();
                        avisoViaje.setAvisoViajeEstatusSinc((short) Constantes.ESTATUS_SINCRONIZADO);
                        avisoViaje.setAvisoViajeFhSincronizacion(new Date());
                        avisoViaje.setAvisoIdRemoto(avisoViajeRegresado.getAvisoIdRemoto());
                        avisoViaje.setAvisoViajeIdRemoto(avisoViajeRegresado.getAvisoViajeIdRemoto());
                        realm.copyToRealmOrUpdate(avisoViaje);
                        realm.commitTransaction();
                        realm.close();
                    }
                    Log.d(TAG, "" + avisoViajeList.toString());
                }

                @Override
                public void onFailure(Call<AvisoViajeList> call, Throwable t) {
                    Log.e(TAG,"Erro: "+ t.toString());
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        realm.close();
        Log.d(TAG, "Terminando enviarAvisosViajes()");
    }

    public int totalAvisosViajes(){
        Log.d(TAG, "Iniciando totalAvisosViajes()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<AvisoViajeEntity> list = realm.where(AvisoViajeEntity.class).findAll();
        Log.d(TAG, "# avisosViajes>"+list.size());
        for(AvisoViajeEntity dto: list){
            Log.d(TAG, ""+dto.toString());
        }
        total = list.size();
        realm.close();
        Log.d(TAG, "Terminando totalAvisosViajes()");
        return total;
    }

}
