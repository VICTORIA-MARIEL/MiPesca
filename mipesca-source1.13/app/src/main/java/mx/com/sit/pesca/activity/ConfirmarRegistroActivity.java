package mx.com.sit.pesca.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.SplashActivity;
import mx.com.sit.pesca.dto.MensajeList;
import mx.com.sit.pesca.dto.PescadorList;
import mx.com.sit.pesca.dto.PresentacionList;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.services.MensajeService;
import mx.com.sit.pesca.services.PresentacionService;
import mx.com.sit.pesca.services.UsuarioService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class ConfirmarRegistroActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmarRegistroAct";
    private TextView txtCC;
    private TextView txtId;
    private EditText txtConfirmarRegistroCve1;
    private EditText txtConfirmarRegistroCve2;
    private EditText txtConfirmarRegistroCve3;
    private EditText txtConfirmarRegistroCve4;
    private Retrofit retrofit;
    private SharedPreferences prefs;
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_registro);
        Intent intentPrevio = getIntent();
        String claveConfirmacion = intentPrevio.getStringExtra("cve");
        final int pescadorId = intentPrevio.getIntExtra("pescadorId",0);
        final int usuarioId = intentPrevio.getIntExtra("usuarioId",0);
        final String movto = intentPrevio.getStringExtra("movto");
        bindUi();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        txtId.setText(""+usuarioId);
        txtCC.setText(claveConfirmacion);


        realm = Realm.getDefaultInstance();
        final PescadorEntity pescador = realm.where(PescadorEntity.class)
                .equalTo("id", pescadorId)
                .findFirst();
        enviarMensaje(claveConfirmacion, pescador.getPescadorTelefono());
        realm.close();

        final Button btnAceptar = findViewById(R.id.btnPescadorConfirmarAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String cc = txtConfirmarRegistroCve1.getText().toString()
                            + txtConfirmarRegistroCve2.getText().toString()
                            + txtConfirmarRegistroCve3.getText().toString()
                            + txtConfirmarRegistroCve4.getText().toString();
                if(txtCC.getText().toString().equals(cc)){
                    final UsuarioEntity usuarioDTO = new UsuarioEntity();
                    realm = Realm.getDefaultInstance();
                    UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class)
                            .equalTo("id", Integer.parseInt(txtId.getText().toString()))
                            .findFirst();

                    usuarioDTO.setId(usuarioEntity.getId());
                    usuarioDTO.setUsuarioLogin(usuarioEntity.getUsuarioLogin());
                    usuarioDTO.setUsuarioNombre(usuarioEntity.getUsuarioNombre());
                    usuarioDTO.setUsuarioContrasena(usuarioEntity.getUsuarioContrasena());
                    usuarioDTO.setUsuarioEstatus(1);
                    realm.close();

                    UsuarioService service = retrofit.create(UsuarioService.class);

                    if("actualizar".equals(movto)) {
                        Call<PescadorList> usuCall = service.editarPescador(
                                usuarioDTO.getId(),
                                usuarioDTO.getUsuarioLogin(),
                                usuarioDTO.getUsuarioContrasena(),
                                pescador.getId(),
                                usuarioDTO.getUsuarioNombre(),
                                pescador.getPescadorTelefono(),
                                pescador.getPescadorEsIndependiente(),
                                pescador.getPescadorCorreo(),
                                pescador.getMunicipioDescripcion(),
                                pescador.getComunidadDescripcion(),
                                pescador.getCooperativaDescripcion(),
                                pescador.getMunicipioId(),
                                pescador.getComunidadId(),
                                pescador.getCooperativaId()
                        );
                        usuCall.enqueue(new Callback<PescadorList>() {

                            @Override
                            public void onResponse(Call<PescadorList> call, Response<PescadorList> response) {
                                PescadorList resultado = response.body();
                                if (resultado.isSuccess()) {
                                    confirmarRegistro(usuarioDTO);
                                    Toast.makeText(ConfirmarRegistroActivity.this, "El usuario se actualizó exitosamente.", Toast.LENGTH_LONG).show();
                                    goToLogin();
                                } else {
                                    Toast.makeText(ConfirmarRegistroActivity.this, "Error. El usuario no se actualizó, intente nuevamente.", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PescadorList> call, Throwable t) {
                                Toast.makeText(ConfirmarRegistroActivity.this, "Error: No hay comunicación con el servidor en la nube.", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                    else{
                        Call<PescadorList> usuCall = service.altaPescador(
                                usuarioDTO.getUsuarioLogin(),
                                usuarioDTO.getUsuarioContrasena(),
                                usuarioDTO.getUsuarioNombre(),
                                pescador.getPescadorTelefono(),
                                pescador.getPescadorEsIndependiente(),
                                pescador.getPescadorCorreo(),
                                pescador.getMunicipioDescripcion(),
                                pescador.getComunidadDescripcion(),
                                pescador.getCooperativaDescripcion(),
                                pescador.getMunicipioId(),
                                pescador.getComunidadId(),
                                pescador.getCooperativaId()
                        );
                        usuCall.enqueue(new Callback<PescadorList>() {

                            @Override
                            public void onResponse(Call<PescadorList> call, Response<PescadorList> response) {
                                PescadorList resultado = response.body();
                                if (resultado.isSuccess()) {
                                    confirmarRegistro(usuarioDTO);
                                    Toast.makeText(ConfirmarRegistroActivity.this, "El usuario se ha registrado exitosamente.", Toast.LENGTH_LONG).show();
                                    goToLogin();
                                } else {
                                    Toast.makeText(ConfirmarRegistroActivity.this, "Error: El usuario no se registró, intente nuevamente.", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PescadorList> call, Throwable t) {
                                Toast.makeText(ConfirmarRegistroActivity.this, "Error: No hay comunicación con el servidor en la nube.", Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                }
                else{
                    Toast.makeText(ConfirmarRegistroActivity.this, "Error: La clave de confirmación es incorrecta.", Toast.LENGTH_LONG).show();
                    txtConfirmarRegistroCve1.setText("");
                    txtConfirmarRegistroCve2.setText("");
                    txtConfirmarRegistroCve3.setText("");
                    txtConfirmarRegistroCve4.setText("");
                    txtConfirmarRegistroCve1.requestFocus();
                }

            }
        });

        txtConfirmarRegistroCve1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                txtConfirmarRegistroCve2.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        txtConfirmarRegistroCve2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                txtConfirmarRegistroCve3.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        txtConfirmarRegistroCve3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                txtConfirmarRegistroCve4.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        txtConfirmarRegistroCve4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                btnAceptar.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Button btnCancelar = findViewById(R.id.btnPescadorConfirmarCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });


        Log.d(TAG, "Terminando onCreate()");

    }

    private void enviarMensaje(String cc, String telefono){
        MensajeService service = retrofit.create(MensajeService.class);
        Call<MensajeList> menCall = service.insertarMensaje(
                (byte)0,
                telefono,
                cc
        );
        menCall.enqueue(new Callback<MensajeList>() {

            @Override
            public void onResponse(Call<MensajeList> call, Response<MensajeList> response) {
                MensajeList resultado = response.body();
                if (resultado.getCodigo() == 200) {
                    Toast.makeText(ConfirmarRegistroActivity.this, "Mensaje enviado exitosamente.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ConfirmarRegistroActivity.this, "Error. El mensaje no se envio, intente nuevamente.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MensajeList> call, Throwable t) {
                Toast.makeText(ConfirmarRegistroActivity.this, "Error:  " + t.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void confirmarRegistro(UsuarioEntity usuarioEntity){
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(usuarioEntity);
        realm.commitTransaction();
        realm.close();
    }

    private void goToLogin(){
        Intent intent = new Intent(ConfirmarRegistroActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void bindUi(){
        Log.d(TAG, "Iniciando bindUi()");
        txtCC = findViewById(R.id.txtCC);
        txtId = findViewById(R.id.txtId);
        txtConfirmarRegistroCve1 = findViewById(R.id.txtConfirmarRegistroCve1);
        txtConfirmarRegistroCve2 = findViewById(R.id.txtConfirmarRegistroCve2);
        txtConfirmarRegistroCve3 = findViewById(R.id.txtConfirmarRegistroCve3);
        txtConfirmarRegistroCve4 = findViewById(R.id.txtConfirmarRegistroCve4);
        Log.d(TAG, "Terminando bindUi()");

    }

}
