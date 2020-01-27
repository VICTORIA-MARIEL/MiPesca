package mx.com.sit.pesca.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.SplashActivity;
import mx.com.sit.pesca.dao.SincronizacionDAO;
import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.CooperativaList;
import mx.com.sit.pesca.dto.LoginList;
import mx.com.sit.pesca.dto.OfnaPescaList;
import mx.com.sit.pesca.dto.PescadorList;
import mx.com.sit.pesca.dto.PresentacionList;
import mx.com.sit.pesca.dto.RecursoList;
import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.CooperativaEntity;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.SincronizacionEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.services.ArtePescaService;
import mx.com.sit.pesca.services.ComunidadService;
import mx.com.sit.pesca.services.CooperativaService;
import mx.com.sit.pesca.services.OfnaPescaService;
import mx.com.sit.pesca.services.PresentacionService;
import mx.com.sit.pesca.services.RecursoService;
import mx.com.sit.pesca.services.UsuarioService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private SharedPreferences prefs;
    private UsuarioEntity usuarioEntity;
    private PescadorEntity pescadorEntity;

    private EditText txtUsuario;
    private EditText txtContrasena;
    private Switch chkRecordar;
    private int pescadorId;
    private Realm realm;
    private int usuarioId;
    static final String STATE_SCORE = "playerScore";
    static final String STATE_LEVEL = "playerLevel";
    private Retrofit retrofit;
    private SincronizacionDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindUi();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        realm = Realm.getDefaultInstance();


        final TextView btnOlvido = findViewById(R.id.btnOlvido);
        btnOlvido.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btnOlvido.setPaintFlags(btnOlvido.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                Intent intent = new Intent(LoginActivity.this, RecuperarContrasenaActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });


        btnOlvido.setOnFocusChangeListener((new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    btnOlvido.setPaintFlags(btnOlvido.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {
                    btnOlvido.setPaintFlags(btnOlvido.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        }));

        btnOlvido.setOnHoverListener((new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_HOVER_ENTER:
                        btnOlvido.setPaintFlags(btnOlvido.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        btnOlvido.setPaintFlags(btnOlvido.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        btnOlvido.setPaintFlags(btnOlvido.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        break;
                }

                return false;
            }
            /*@Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    btnOlvido.setPaintFlags(btnOlvido.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                } else {

                }
            }*/
        }));

        txtUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtUsuario.setHint(R.string.lblUsuario);
                } else {
                    txtUsuario.setHint("");
                }
            }
        });

        txtContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtContrasena.setHint(R.string.lblContrasena);
                } else {
                    txtContrasena.setHint("");
                }
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            final String usuario = txtUsuario.getText().toString();
            final String contrasena = txtContrasena.getText().toString();

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (login(usuario, contrasena)) {
                UsuarioEntity dto = realm.where(UsuarioEntity.class)
                        .equalTo("usuarioLogin", usuario)
                        .equalTo("usuarioContrasena", contrasena)
                        .equalTo("usuarioEstatus", 1)
                        .findFirst();
                    if(dto!=null) {
                        usuarioId = dto.getId();
                        PescadorEntity pescadorDTO = realm.where(PescadorEntity.class)
                                .equalTo("pescadorUsrRegistro", usuarioId)
                                .equalTo("pescadorEstatus", 1)
                                .findFirst();
                        if(pescadorDTO!=null) {
                            pescadorId = pescadorDTO.getId();
                                saveOnPreferences("" + usuarioId, contrasena, "" + pescadorId);

                                goToMain(usuario, contrasena);
                        }
                    }
                    else{
                        if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                            UsuarioService service = retrofit.create(UsuarioService.class);
                            Call<LoginList> usuCall = service.validar(
                                    txtUsuario.getText().toString(),
                                    txtContrasena.getText().toString()
                            );
                            usuCall.enqueue(new Callback<LoginList>(){
                                @Override
                                public void onResponse(Call<LoginList> call, Response<LoginList> response) {
                                    LoginList resultado = response.body();
                                    Log.d(TAG,"resultado: " + resultado.isSuccess());
                                    if(resultado.isSuccess()){ //el usuario se encuentra en la bd remota
                                        usuarioEntity = new UsuarioEntity(
                                                resultado.getUsuarioLogin(),
                                                resultado.getUsuarioNombre(),
                                                resultado.getUsuarioContrasena()
                                        );
                                        usuarioEntity.setUsuarioEstatus(1);
                                        int municipioId = 0;
                                        int comunidadId = 0;
                                        int cooperativaId = 0;
                                        String municipio = "";
                                        String comunidad = "";
                                        String cooperativa = "";
                                        try{
                                            municipioId = Integer.parseInt(resultado.getPescadorMunicipioId());
                                            municipio = resultado.getPescadorMunicipio();
                                        }
                                        catch(NumberFormatException nfe){
                                            Log.e(TAG, nfe.getMessage());
                                        }

                                        try{
                                            comunidadId = Integer.parseInt(resultado.getPescadorComunidadId());
                                            comunidad = resultado.getPescadorComunidad();
                                        }
                                        catch(NumberFormatException nfe){
                                            Log.e(TAG, nfe.getMessage());
                                        }

                                        try{
                                            cooperativaId = Integer.parseInt(resultado.getPescadorCooperativaId());
                                            cooperativa = resultado.getPescadorCooperativa();
                                        }
                                        catch(NumberFormatException nfe){
                                            Log.e(TAG, nfe.getMessage());
                                        }

                                        pescadorEntity = new PescadorEntity(
                                                resultado.getPescadorNombre(),
                                                Util.convertStringToDate(resultado.getPescadorFhNacimiento(), "/"),
                                                resultado.getPescadorTelefono(),
                                                resultado.getPescadorCorreo(),
                                                municipioId,
                                                comunidadId,
                                                cooperativaId,
                                                "",
                                                resultado.getPescadorEsIndependiente() ? (short) 1 : (short) 0,
                                                municipio,
                                                comunidad,
                                                cooperativa
                                        );
                                        if(pescadorEntity!=null && pescadorEntity.getPescadorFhNacimiento()==null){
                                            pescadorEntity.setPescadorFhNacimiento(new Date());
                                        }
                                        insertUsuarioPescador(usuarioEntity, pescadorEntity);

                                        saveOnPreferences("" + usuarioEntity.getId(), usuarioEntity.getUsuarioContrasena(), "" + pescadorEntity.getId());

                                        goToMain("" + usuarioEntity.getId(), resultado.getPescadorNombre());
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, "Usuario no encontrado.", Toast.LENGTH_LONG).show();
                                        txtUsuario.requestFocus();
                                        txtUsuario.setText("");
                                        txtContrasena.setText("");
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoginList> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this, "Usuario no encontrado.", Toast.LENGTH_LONG).show();
                                    txtUsuario.requestFocus();
                                    txtUsuario.setText("");
                                    txtContrasena.setText("");
                                }
                            });

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Usuario no encontrado.", Toast.LENGTH_LONG).show();
                            txtUsuario.requestFocus();
                            txtUsuario.setText("");
                            txtContrasena.setText("");
                        }
                    }
            }


            }

        });


        TextView btnRegistrarUsuario = findViewById(R.id.lblRegistrar);
        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ///if(validarCatalogos()) {
                    Intent intentRegistro = new Intent(LoginActivity.this, PescadorRegistroActivity.class);
                    LoginActivity.this.startActivity(intentRegistro);
                /*}
                else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    retrofit = new Retrofit.Builder()
                                            .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                        consultarPresentaciones();
                                        consultarComunidades();
                                        consultarCooperativas();
                                        consultarArtesPesca();
                                        consultarOfnasPesca();
                                        consultarRecursos();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    TextView myMsg = new TextView(LoginActivity.this);
                    myMsg.setText("No se encontraron los catálogos de comunidades y cooperativas. ¿Desea descargalos ahora?");
                    myMsg.setTextSize(14);
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(myMsg);
                    builder.setPositiveButton("Aceptar", dialogClickListener)
                            .setNegativeButton("Cancelar", dialogClickListener).show();

                }*/
            }
        });
        Log.d(TAG, "Terminando onCreate");
    }



    private void insertUsuarioPescador(UsuarioEntity usuarioEntity, PescadorEntity pescadorEntity){
        Log.d(TAG, "Iniciando insertUsuarioPescador(PescadorEntity entity)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(usuarioEntity);
        pescadorEntity.setPescadorUsrRegistro(usuarioEntity.getId());
        realm.copyToRealm(pescadorEntity);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertUsuarioPescador(PescadorEntity entity)");
    }


    /*Autoguardado de dato, no base de datos*/
    @Override
    public void onPause(){
        super.onPause();
    }

    /*Datos y procesos pesados*/
    @Override
    public void onStop(){
        super.onStop();
    }

    /*Datos y procesos pesados*/
    @Override
    public void onRestart(){
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }


    private void bindUi(){
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        chkRecordar = findViewById(R.id.chkRecordar);

    }



    private boolean login(String usuario, String contrasena){
        if(!isValidUser(usuario)){
            Toast.makeText(this, "Usuario incorrecto, intente nuevamente.",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!isValidPassword(contrasena)){
            Toast.makeText(this, "Contraseña incorrecta, intente nuevamente.", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void saveOnPreferences(String usuario, String contrasena, String pescador){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("usuario",usuario);
            editor.putString("pescador",pescador);
            editor.putString("contrasena",contrasena);
            editor.putBoolean("recordar",chkRecordar.isChecked());
            editor.apply();


    }


    private boolean isValidUser(String usuario){

        return !TextUtils.isEmpty(usuario);
    }

    private boolean isValidPassword(String contrasena){
        return contrasena.length() >= 2;
    }

    private void goToMain(String usuarioId, String usuarioNombre){
        Intent intentViaje = new Intent(this,MainActivity.class);
        intentViaje.putExtra("usuarioId", usuarioId);
        intentViaje.putExtra("usuarioNombre", usuarioNombre);
        //intentViaje.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentViaje);
    }



}
