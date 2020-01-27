package mx.com.sit.pesca.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dto.AvisoList;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.services.AvisoService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvisoDAO {

    private static final String TAG = "AvisoDAO";
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private Context contexto;

    public AvisoDAO(){

    }

    public AvisoDAO(int usuarioId, Context context){
        Log.d(TAG, "Iniciando AvisoDAO(int usuarioId, Context context)");
        this.usuarioId = usuarioId;
        this.contexto = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando AvisoDAO(int usuarioId, Context context)");
    }

    public void sincronizarAvisos(){
        Log.d(TAG, "Iniciando sincronizarAvisos()");
        realm = Realm.getDefaultInstance();
        UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class).equalTo("id",usuarioId).findFirst();
        String usuarioLogin = "";
        if(usuarioEntity!=null) {
            usuarioLogin = usuarioEntity.getUsuarioLogin();
        }
        RealmResults<AvisoEntity> list = realm.where(AvisoEntity.class)
                .equalTo("usuarioId",usuarioId)
                .equalTo("avisoEstatusSinc", Constantes.ESTATUS_NO_SINCRONIZADO)
                .equalTo("avisoEstatus",Constantes.AVISO_ESTATUS_FINALIZADO).findAll();

        for(AvisoEntity avisoDTO: list) {
            AvisoService service = retrofit.create(AvisoService.class);
            Call<AvisoList> avisoCall = service.insertAviso(
                    "" + avisoDTO.getId(),
                    "" + avisoDTO.getPescadorId(),
                    "" + avisoDTO.getUsuarioId(),
                    "" + Util.convertDateToString(avisoDTO.getAvisoFhRegistro(), Constantes.formatoFechaHora),
                    "" + Util.convertDateToString(avisoDTO.getAvisoFhSolicitud(), Constantes.formatoFechaHora),
                    "" +  Util.convertDateToString(avisoDTO.getAvisoPeriodoInicio(), Constantes.formatoFechaSinc),
                    "" + Util.convertDateToString(avisoDTO.getAvisoPeriodoFin(), Constantes.formatoFechaSinc),
                    "" + avisoDTO.getAvisoDuracion(),
                    "" + avisoDTO.getAvisoDiasEfectivos(),
                    "" + avisoDTO.getAvisoFolio(),
                    "" + avisoDTO.getAvisoIdLocal(),
                    "" + Util.convertDateToString(avisoDTO.getAvisoFhSincronizacion(), Constantes.formatoFechaHora),
                    "" + avisoDTO.getAvisoEstatus(),
                    //"" + avisoDTO.getPermisoId(),
                    "2",
                    "" + avisoDTO.getAvisoZonaPesca(),
                    "" + avisoDTO.getOfnapescaId(),
                    "" + avisoDTO.getAvisoEsPesqueriaAcuacultural(),
                    "" + avisoDTO.getSitioId(),
                    "" + usuarioLogin
            );
            avisoCall.enqueue(new Callback<AvisoList>() {
                @Override
                public void onResponse(Call<AvisoList> call, Response<AvisoList> response) {
                    AvisoList avisoList = response.body();
                    AvisoEntity avisoRegresado = avisoList.getAviso();
                    Log.d(TAG, "Aviso Regresado:" + avisoRegresado);
                    if(avisoRegresado!=null ) {
                        realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        AvisoEntity aviso = realm.where(AvisoEntity.class).equalTo("id", avisoRegresado.getAvisoIdLocal()).findFirst();
                        aviso.setAvisoEstatusSinc((short) Constantes.ESTATUS_SINCRONIZADO);
                        aviso.setAvisoFhSincronizacion(new Date());
                        aviso.setAvisoIdRemoto(avisoRegresado.getAvisoIdRemoto());
                        realm.copyToRealmOrUpdate(aviso);
                        realm.commitTransaction();
                        realm.close();
                    }

                }

                @Override
                public void onFailure(Call<AvisoList> call, Throwable t) {
                    Toast.makeText(contexto, "Error." + t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        realm.close();

        Log.d(TAG, "Terminando sincronizarAvisos()");

    }

    public int totalAvisos(){
        Log.d(TAG, "Iniciando totalAvisos()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<AvisoEntity> list = realm.where(AvisoEntity.class).findAll();
        Log.d(TAG, "# avisos>"+list.size());
        for(AvisoEntity dto: list){
            Log.d(TAG, ""+dto.toString());
        }
        total = list.size();
        realm.close();
        Log.d(TAG, "Terminando totalAvisos()");
        return total;
    }


}
