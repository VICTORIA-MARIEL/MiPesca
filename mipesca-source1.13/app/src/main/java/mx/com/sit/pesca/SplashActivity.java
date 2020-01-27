package mx.com.sit.pesca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import mx.com.sit.pesca.activity.LoginActivity;
import mx.com.sit.pesca.activity.MainActivity;
import mx.com.sit.pesca.dao.ArtePescaDAO;
import mx.com.sit.pesca.dao.ComunidadDAO;
import mx.com.sit.pesca.dao.CooperativaDAO;
import mx.com.sit.pesca.dao.EdoPesqueriaRecursoDAO;
import mx.com.sit.pesca.dao.MunicipioDAO;
import mx.com.sit.pesca.dao.OfnaPescaDAO;
import mx.com.sit.pesca.dao.PermisoDAO;
import mx.com.sit.pesca.dao.PresentacionDAO;
import mx.com.sit.pesca.dao.RecursoDAO;
import mx.com.sit.pesca.dao.RegionDAO;
import mx.com.sit.pesca.dao.SitioDAO;
import mx.com.sit.pesca.dao.UsuarioDAO;
import mx.com.sit.pesca.util.Util;

public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private SharedPreferences prefs;
//    private Realm realm;
    private boolean modoDepuracion = true;

//    private Retrofit retrofit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreate");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash);
//        realm = Realm.getDefaultInstance();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        /*Metodos de depuracion*/
        if(modoDepuracion) {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.debugUsuarios();
            usuarioDAO.totalUsuarios();
            //ViajeDAO viajeDAO = new ViajeDAO(0,SplashActivity.this);
            //viajeDAO.totalViajes();
            //ViajeRecursoDAO viajeRecursoDAO = new ViajeRecursoDAO(0,SplashActivity.this);
            //viajeRecursoDAO.totalViajesRecursos();
            //AvisoDAO avisoDAO = new AvisoDAO(0,SplashActivity.this);
            //avisoDAO.totalAvisos();
            //AvisoViajeDAO avisoViajeDAO = new AvisoViajeDAO(0,SplashActivity.this);
            //avisoViajeDAO.totalAvisosViajes();
        }


        llenarCatalogos();


        if(Util.getRecordarPrefs(prefs)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();

        Log.d(TAG, "Terminando onCreate");
    }




    private void llenarCatalogos(){
        Log.d(TAG, "Iniciando llenarCatalogos");
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean exito = true;

        PresentacionDAO presentacionDAO = new PresentacionDAO(0,SplashActivity.this);
        int tamanoPres = presentacionDAO.getPresentaciones();
        if(tamanoPres <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito = presentacionDAO.consultarPresentaciones();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        MunicipioDAO municipioDAO = new MunicipioDAO(0,SplashActivity.this);
        int tamanoMun = municipioDAO.getMunicipios();
        if(exito && tamanoMun <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =municipioDAO.consultarMunicipios();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        ComunidadDAO comunidadDAO = new ComunidadDAO(0,SplashActivity.this);
        int tamanoCom = comunidadDAO.getComunidades();
        if(exito && tamanoCom <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =comunidadDAO.consultarComunidades();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        CooperativaDAO cooperativaDAO = new CooperativaDAO(0,SplashActivity.this);
        int tamanoCop = cooperativaDAO.getCooperativas();
        if(exito && tamanoCop <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito = cooperativaDAO.consultarCooperativas();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        ArtePescaDAO artepescaDAO = new ArtePescaDAO(0,SplashActivity.this);
        int tamanoAp = artepescaDAO.getArtesPesca();
        if(exito && tamanoAp <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =artepescaDAO.consultarArtesPesca();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        OfnaPescaDAO ofnapescaDAO = new OfnaPescaDAO(0,SplashActivity.this);
        int tamanoOp = ofnapescaDAO.getOfnasPesca();
        if(exito && tamanoOp <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =ofnapescaDAO.consultarOfnasPesca();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        RecursoDAO recursoDAO = new RecursoDAO(0,SplashActivity.this);
        int tamanorec = recursoDAO.getRecursos();
        if(exito && tamanorec <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =recursoDAO.consultarRecursos();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        EdoPesqueriaRecursoDAO pesqueriaRecursoDAO = new EdoPesqueriaRecursoDAO(0,SplashActivity.this);
        int tamanopesrec = pesqueriaRecursoDAO.getPesqueriaRecursos();
        if(exito && tamanopesrec <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =pesqueriaRecursoDAO.consultarPesqueriaRecursos();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }


        RegionDAO regionDAO = new RegionDAO(0,SplashActivity.this);
        int tamanoreg = regionDAO.getRegiones();
        if(exito && tamanoreg <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =regionDAO.consultarRegiones();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        SitioDAO sitioDAO = new SitioDAO(0,SplashActivity.this);
        int tamanosit = sitioDAO.getSitios();
        if(exito && tamanosit <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =sitioDAO.consultarSitios();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        PermisoDAO permisoDAO = new PermisoDAO(0,SplashActivity.this);
        int tamanoper = permisoDAO.getPermisos();
        if(exito && tamanoper <= 0) {
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                exito =permisoDAO.consultarPermisos();
            }
            else{
                Toast.makeText(SplashActivity.this, "Error: No se encontro conexión a internet." , Toast.LENGTH_LONG).show();
                exito = false;
            }
        }

        Log.d(TAG, "Terminando llenarCatalogos");
    }



}
