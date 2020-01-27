package mx.com.sit.pesca.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioDAO {
    private static final String TAG = "UsuarioDAO";
    private Realm realm;
    private Retrofit retrofit;

    public UsuarioDAO(){
        Log.d(TAG, "Iniciando UsuarioDAO()");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Log.d(TAG, "Terminando UsuarioDAO()");

    }


    public UsuarioEntity getUsuario(int usuarioId){
        UsuarioEntity usuarioEntity = null;
        realm = Realm.getDefaultInstance();
        usuarioEntity = realm.where(UsuarioEntity.class).equalTo("id",usuarioId).findFirst();
        realm.close();
        return usuarioEntity;
    }

    public int totalUsuarios(){
        Log.d(TAG, "Iniciando totalUsuarios()");
        int total = 0;
        realm = Realm.getDefaultInstance();
        RealmResults<UsuarioEntity> list = realm.where(UsuarioEntity.class).findAll();
        Log.d(TAG, "# viajes>"+list.size());
        for(UsuarioEntity dto: list){
            Log.d(TAG, ""+dto.toString());
        }
        total = list.size();
        realm.close();
        Log.d(TAG, "Terminando totalUsuarios()");
        return total;
    }

    public void debugUsuarios(){
        Log.d(TAG, "Iniciando consultarUsuarios()");
        realm = Realm.getDefaultInstance();

        UsuarioEntity usuarioEntity = new UsuarioEntity(
                "admin","administrador","7qyudcen"
        );
        usuarioEntity.setUsuarioEstatus(1);
        usuarioEntity.setId(1);

        PescadorEntity pescadorEntity = new PescadorEntity(
                "administrador",
                new Date(),
                "5518648406",
                "saag21@hotmail.com",
                0,
                0,
                0,
                "",
                (short)1,
                "",
                "",
                ""

        );
        pescadorEntity.setId(27);
        pescadorEntity.setPescadorUsrRegistro(1);
        realm.beginTransaction();
        //realm.deleteAll();
        //realm.delete(UsuarioEntity.class);
        //realm.delete(PescadorEntity.class);
        realm.copyToRealmOrUpdate(usuarioEntity);
        realm.copyToRealmOrUpdate(pescadorEntity);
        realm.commitTransaction();
        realm.close();
    }


}
