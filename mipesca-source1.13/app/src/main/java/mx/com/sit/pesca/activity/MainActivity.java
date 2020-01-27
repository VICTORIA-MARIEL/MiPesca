package mx.com.sit.pesca.activity;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.dao.SincronizacionDAO;
import mx.com.sit.pesca.dto.PancartaList;
import mx.com.sit.pesca.dto.PermisoList;
import mx.com.sit.pesca.entity.PancartaEntity;
import mx.com.sit.pesca.entity.SincronizacionEntity;
import mx.com.sit.pesca.fragmentos.AvisoConsultaFragment;
import mx.com.sit.pesca.fragmentos.AvisoFragment;
import mx.com.sit.pesca.fragmentos.AvisoPDFFragment;
import mx.com.sit.pesca.fragmentos.AvisoResultadoFragment;
import mx.com.sit.pesca.fragmentos.ConfiguracionFragment;
import mx.com.sit.pesca.fragmentos.PermisoConsultaFragment;
import mx.com.sit.pesca.fragmentos.ReporteFragment;
import mx.com.sit.pesca.fragmentos.SincronizacionFragment;
import mx.com.sit.pesca.fragmentos.ViajeConsultaFragment;
import mx.com.sit.pesca.fragmentos.ViajeFragment;
import mx.com.sit.pesca.jobs.SincronizacionJOb;
import mx.com.sit.pesca.services.PancartaService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private SharedPreferences prefs;
    private Realm realm;
    private SincronizacionDAO dao;
    private int usuarioId;
    private int pescadorId;
    private Retrofit retrofit;
    private NavigationView navigationView;
    private final StringBuilder sbMensaje = new StringBuilder();
    private final StringBuilder sbTitulo = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        realm = Realm.getDefaultInstance();
        dao = new SincronizacionDAO(usuarioId, MainActivity.this);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        validarSincronizacion(pescadorId, usuarioId);
        if (savedInstanceState == null) {
            validarPancarta();
        }
    }

    private void validarPancarta() {
        final StringBuilder sb = new StringBuilder();
        try {
            PancartaService service = retrofit.create(PancartaService.class);
            Call<PancartaList> paCall = service.validarPancarta(
                    "1",
                    Util.getHoy()
            );
            paCall.enqueue(new Callback<PancartaList>() {

                @Override
                public void onResponse(Call<PancartaList> call, Response<PancartaList> response) {
                    PancartaList resultado = response.body();
                    if (resultado.isSuccess()) {
                        sb.append("true");
                        List<PancartaEntity> list = resultado.getPancarta();
                        if(list!=null && !((List) list).isEmpty()) {
                            sbTitulo.append("<b>"+list.get(0).getPancartaTitulo()+"</b><br/>");
                            sbMensaje.append(list.get(0).getPancartaDescripcion());
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            toolbar.setTitle("Viaje");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                    new ViajeFragment()).commit();
                                            navigationView.setCheckedItem(R.id.nav_viaje);
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            TextView myMsg = new TextView(MainActivity.this);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                myMsg.setText(Html.fromHtml("<b>"+sbTitulo.toString()+"</b><br>"+sbMensaje.toString(), Html.FROM_HTML_MODE_LEGACY));
                            } else {
                                myMsg.setText(Html.fromHtml("<b>"+sbTitulo.toString()+"</b><br>"+sbMensaje.toString()));
                            }
                            myMsg.setPadding(24,24,24,24);
                            myMsg.setTextSize(14);
                            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                            builder.setView(myMsg);
                            builder.setPositiveButton("Aceptar", dialogClickListener)
                                    .show();
                        }
                        else{
                            toolbar.setTitle("Viaje");
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new ViajeFragment()).commit();
                            navigationView.setCheckedItem(R.id.nav_viaje);

                        }


                    }
                    else{
                        toolbar.setTitle("Viaje");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ViajeFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_viaje);

                    }
                }

                @Override
                public void onFailure(Call<PancartaList> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error: No hay comunicación con el servidor en la nube.", Toast.LENGTH_LONG).show();
                    sb.append("false");
                }
            });

        }
        catch(Exception e){
            Log.e(TAG,"Error: " + e.getMessage());
        }


    }


    private void validarSincronizacion(int pescadorId, int usuarioId){
        realm = Realm.getDefaultInstance();
        SincronizacionEntity dto = realm.where(SincronizacionEntity.class)
                .equalTo("pescadorId", pescadorId)
                .equalTo("sincronizacionUsrRegistro",usuarioId)
                .findFirst();
        if(dto==null) {
            SincronizacionEntity sincronizacion = new SincronizacionEntity();

            sincronizacion.setPescadorId(pescadorId);
            sincronizacion.setSincronizacionHora("00:15");
            sincronizacion.setSincronizacionLunes((short)1);
            sincronizacion.setSincronizacionMartes((short)1);
            sincronizacion.setSincronizacionMiercoles((short)1);
            sincronizacion.setSincronizacionJueves((short)1);
            sincronizacion.setSincronizacionViernes((short)1);
            sincronizacion.setSincronizacionSabado((short)1);
            sincronizacion.setSincronizacionDomingo((short)1);
            sincronizacion.setSincronizacionEstatus((short) 1);
            sincronizacion.setSincronizacionFhRegistro(new Date());
            sincronizacion.setSincronizacionUsrRegistro(usuarioId);
            sincronizacion.setSincronizacionJobEstatus((short) Constantes.JOB_INICIADO);
            dao.insert(sincronizacion);
            if(sincronizacion!=null && sincronizacion.getSincronizacionJobEstatus() == 1) {
                scheduleJob(null, usuarioId);
            }
        }
        realm.close();

    }

    public void scheduleJob(View v, int pescadorId) {
        ComponentName componentName = new ComponentName(MainActivity.this, SincronizacionJOb.class);
        JobInfo info = new JobInfo.Builder(pescadorId, componentName)
                .setRequiresCharging(true)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob(View v,int pescadorId) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(pescadorId);
        Log.d(TAG, "Job cancelled");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_logout:
                logout();
                break;
            case R.id.menu_forget_logout:
                removeSharedPreferences();
                logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //evitar el atras
        startActivity(intent);
    }

    private void removeSharedPreferences(){
        prefs.edit().clear().apply();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_viaje:
                toolbar.setTitle(R.string.headerViaje);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViajeFragment()).commit();
                break;
            case R.id.nav_reporte:
                toolbar.setTitle(R.string.headerReportePesca);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReporteFragment()).commit();
                break;
            case R.id.nav_aviso:
                toolbar.setTitle(R.string.headerAvisoArribo);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AvisoFragment()).commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new AvisoPDFFragment()).commit();
                break;
            case R.id.nav_configuracion:
                toolbar.setTitle(R.string.headerConfiguracion);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ConfiguracionFragment()).commit();
                break;
            case R.id.nav_sincronizacion:
                toolbar.setTitle(R.string.headerSincronizacion);
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       new SincronizacionFragment()).commit();
                break;
            case R.id.nav_historico:
                toolbar.setTitle(R.string.headerHistoricoPesca);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ViajeConsultaFragment()).commit();
                break;
            case R.id.nav_aviso_consulta:
                toolbar.setTitle(R.string.headerConsultaAviso);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AvisoConsultaFragment()).commit();
                break;
            case R.id.nav_permiso:
                toolbar.setTitle(R.string.headerPermiso);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PermisoConsultaFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            logout();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView myMsg = new TextView(this);
            myMsg.setText(R.string.cerra_sesion);
            myMsg.setPadding(24,24,24,24);
            myMsg.setTextSize(14);
            myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(myMsg);
            builder.setPositiveButton("Aceptar", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
//            super.onBackPressed();
        }
    }

    private void setCredentialsIfExists(){
        Log.d(TAG, "Iniciando setCredentialsIfExists()");
        String usuario = Util.getUserPrefs(prefs);
        String contrasena = Util.getPasswordPrefs(prefs);
        String pescador = Util.getPescadorPrefs(prefs);
        if(!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(pescador)){
            usuarioId = Integer.parseInt(usuario);
            pescadorId = Integer.parseInt(pescador);
        }
        Log.d(TAG, "Terminando setCredentialsIfExists()");
    }

}
