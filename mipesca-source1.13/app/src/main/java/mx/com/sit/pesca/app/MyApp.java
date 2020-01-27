package mx.com.sit.pesca.app;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.SincronizacionEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;

public class MyApp extends Application {
    public static AtomicInteger pescadorID = new AtomicInteger();
    public static AtomicInteger viajeID = new AtomicInteger();
    public static AtomicInteger viajeRecursoID = new AtomicInteger();
    public static AtomicInteger usuarioID = new AtomicInteger();
    public static AtomicInteger sincronizacionID = new AtomicInteger();
    public static AtomicInteger avisoID = new AtomicInteger();
    public static AtomicInteger permisoID = new AtomicInteger();
    public static AtomicInteger avisoViajeID = new AtomicInteger();
    @Override
    public void onCreate(){
        super.onCreate();
        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        pescadorID = getIdByTable(realm, PescadorEntity.class);
        viajeID = getIdByTable(realm, ViajeEntity.class);
        avisoID = getIdByTable(realm, AvisoEntity.class);
        permisoID = getIdByTable(realm, PermisoEntity.class);
        avisoViajeID = getIdByTable(realm, AvisoViajeEntity.class);
        viajeRecursoID = getIdByTable(realm, ViajeRecursoEntity.class);
        usuarioID = getIdByTable(realm, UsuarioEntity.class);
        sincronizacionID = getIdByTable(realm, SincronizacionEntity.class);
        realm.close();
    }

    private void setUpRealmConfig(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

    }


    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
       RealmResults<T> result = realm.where(anyClass).findAll();
        return (result.size() > 0) ? new AtomicInteger(result.max("id").intValue()):new AtomicInteger();
    }

}
