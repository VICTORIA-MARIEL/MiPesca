package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.ViajeList;
import mx.com.sit.pesca.dto.ViajesList;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.services.ViajeService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViajeDAO {

    private static final String TAG = "ViajeDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public ViajeDAO(){

    }

    public ViajeDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando ViajeDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando ViajeDAO(int usuarioId, Context context)");
    }


    public void sincronizarViajes(){
        Log.d(TAG, "Iniciando sincronizarViajes()");
        realm = Realm.getDefaultInstance();
        UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class).equalTo("id",usuarioId).findFirst();
        String usuarioLogin = "";
        if(usuarioEntity!=null) {
            usuarioLogin = usuarioEntity.getUsuarioLogin();
        }
        RealmResults<ViajeEntity> list = realm.where(ViajeEntity.class)
                                    .equalTo("usuarioId",usuarioId)
                                    .equalTo("viajeEstatusSinc", Constantes.ESTATUS_NO_SINCRONIZADO)
                                    .equalTo("viajeEstatus",Constantes.VIAJE_ESTATUS_FINALIZADO).findAll();

        for(ViajeEntity veDTO: list) {
            ViajeService service = retrofit.create(ViajeService.class);
            Call<ViajeList> viajeCall = service.insertViaje(
                    "" + veDTO.getId(), //viajeId
                    "" +Util.convertDateToString(veDTO.getViajeFhRegistro(), Constantes.formatoFechaHora),
                    "" +Util.convertDateToString(veDTO.getViajeFhFinalizo(), Constantes.formatoFechaHora),
                    "" +veDTO.getMunicipioId(),
                    "" +veDTO.getComunidadId(),
                    "" +veDTO.getArtepescaId(),
                    "" +veDTO.getCombustible(),
                    "" +veDTO.getUsuarioId(),
                    "" +veDTO.getPescadorId(),
                    "" +veDTO.getViajeIdLocal(), //viajeIdApp
                    "" +Util.convertDateToString(veDTO.getViajeFhSincronizacion(), Constantes.formatoFechaHora),
                    "" +veDTO.getViajeEstatus(),
                    "" +veDTO.getViajeFolio(),
                    "" + usuarioLogin

            );


            viajeCall.enqueue(new Callback<ViajeList>() {
                @Override
                public void onResponse(Call<ViajeList> call, Response<ViajeList> response) {
                    ViajeList viajeList = response.body();
                    ViajeEntity viajeRegresado = viajeList.getViaje();
                    Log.d(TAG, "Viaje Regresado:" + viajeRegresado);
                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    ViajeEntity viaje = realm.where(ViajeEntity.class).equalTo("id", viajeRegresado.getViajeIdLocal()).findFirst();
                    viaje.setViajeEstatusSinc((short)Constantes.ESTATUS_SINCRONIZADO);
                    viaje.setViajeFhSincronizacion(new Date());
                    viaje.setViajeIdRemoto(viajeRegresado.getViajeIdRemoto());
                    realm.copyToRealmOrUpdate(viaje);
                    realm.commitTransaction();
                    realm.close();
                }

                @Override
                public void onFailure(Call<ViajeList> call, Throwable t) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }


        Log.d(TAG, "Terminando sincronizarViajes()");

    }



    public int totalViajes(){
        Log.d(TAG, "Iniciando consultarViajes()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<ViajeEntity> list = realm.where(ViajeEntity.class).findAll();
        Log.d(TAG, "# viajes>"+list.size());
        for(ViajeEntity dto: list){
            Log.d(TAG, ""+dto.toString());
        }
        total = list.size();
        realm.close();
        Log.d(TAG, "Terminando consultarViajes()");
        return total;
    }


}
