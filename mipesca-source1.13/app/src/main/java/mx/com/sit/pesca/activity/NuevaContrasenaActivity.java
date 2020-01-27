package mx.com.sit.pesca.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.dto.PescadorList;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.services.UsuarioService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NuevaContrasenaActivity extends AppCompatActivity {
    private static final String TAG = "NuevaContrasenaActivity";
    private EditText txtUsuario;
    private Realm realm;
    private int usuarioId;
    private EditText txtContrasena;
    private EditText txtConfirmarContrasena;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_contrasena);
        bindUi();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        usuarioId = getIntent().getIntExtra("usuarioId",0);
        if(usuarioId == 0){
            Toast.makeText(NuevaContrasenaActivity.this, "Se pedrió la conexión con el servidor, intente nuevamente..",Toast.LENGTH_LONG).show();
            goToLogin();
        }

        txtConfirmarContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if(!TextUtils.isEmpty(txtConfirmarContrasena.getText().toString()) &&
                            !TextUtils.isEmpty(txtContrasena.getText().toString())) {
                        if (!txtContrasena.getText().toString().equals(txtConfirmarContrasena.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Error: Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                            txtConfirmarContrasena.setText("");
                            txtContrasena.setText("");

                        }
                    }
                    txtConfirmarContrasena.setHint("");
                }
                else{
                    txtConfirmarContrasena.setHint(R.string.lblNuevaContrasenaConfirmar);
                }
            }
        });


        txtContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!TextUtils.isEmpty(txtConfirmarContrasena.getText().toString())) {
                        if (!txtContrasena.getText().toString().equals(txtConfirmarContrasena.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Error: Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                            txtConfirmarContrasena.setText("");
                            txtContrasena.setText("");

                        }
                    }
                    txtContrasena.setHint("");
                }
                else{
                    txtContrasena.setHint(R.string.lblNuevaContrasena);
                }
            }
        });


        Button btnGuardar = findViewById(R.id.btnNuevaContrasenaGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Validar que las dos contraseñas sean iguales
                if(TextUtils.isEmpty(txtContrasena.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Ingrese su nueva contraseña.", Toast.LENGTH_LONG).show();
                    txtConfirmarContrasena.setText("");
                    txtContrasena.setText("");
                    txtContrasena.requestFocus();

                }
                else if (!txtContrasena.getText().toString().equals(txtConfirmarContrasena.getText().toString())
                        ) {
                    Toast.makeText(getApplicationContext(), "Error: Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                    txtConfirmarContrasena.setText("");
                    txtContrasena.setText("");
                    txtContrasena.requestFocus();
                }
                else {
                    //Cambiar la contraseña al usuario
                    UsuarioEntity usuarioDTO = new UsuarioEntity();
                    PescadorEntity pescadorDTO = new PescadorEntity();
                    realm = Realm.getDefaultInstance();
                    UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class)
                            .equalTo("id", usuarioId)
                            .equalTo("usuarioEstatus", 1)
                            .findFirst();

                    usuarioDTO.setId(usuarioEntity.getId());
                    usuarioDTO.setUsuarioLogin(usuarioEntity.getUsuarioLogin());
                    usuarioDTO.setUsuarioNombre(usuarioEntity.getUsuarioNombre());
                    usuarioDTO.setUsuarioContrasena(txtContrasena.getText().toString());
                    usuarioDTO.setUsuarioEstatus(1);

                    PescadorEntity pescadorEntity = realm.where(PescadorEntity.class)
                            .equalTo("pescadorUsrRegistro", usuarioId)
                            .findFirst();
                    pescadorDTO.setId(pescadorEntity.getId());
                    pescadorDTO.setPescadorTelefono(pescadorEntity.getPescadorTelefono());
                    pescadorDTO.setPescadorEsIndependiente(pescadorEntity.getPescadorEsIndependiente());
                    pescadorDTO.setPescadorCorreo(pescadorEntity.getPescadorCorreo())   ;
                    realm.close();
                    cambiarContraseña(usuarioDTO);
                    cambiarContraseñaRemota(usuarioDTO, pescadorDTO);
                    Toast.makeText(NuevaContrasenaActivity.this, "Contraseña cambiada exitosamente.", Toast.LENGTH_LONG).show();
                    goToLogin();
                }
            }
        });



        Button btnCancelar = findViewById(R.id.btnNuevaContrasenaCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goToLogin();
            }
        });
        Log.d(TAG, "Terminando onCreate");
    }

    private void cambiarContraseña(UsuarioEntity usuarioEntity){
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(usuarioEntity);
        realm.commitTransaction();
        realm.close();
    }

    private boolean cambiarContraseñaRemota(UsuarioEntity usuarioDTO, PescadorEntity pescador){
        final StringBuilder sb = new StringBuilder();
        UsuarioService service = retrofit.create(UsuarioService.class);

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
                        sb.append("true");
                    } else {
                        sb.append("false");
                    }
                }

                @Override
                public void onFailure(Call<PescadorList> call, Throwable t) {
                    sb.append("false");
                }
            });
            return Boolean.parseBoolean(sb.toString());
    }


    private void goToLogin(){
        Intent intent = new Intent(NuevaContrasenaActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //evitar el atras
        startActivity(intent);

    }


    private void bindUi(){
        txtContrasena = findViewById(R.id.txtNuevaContrasena);
        txtConfirmarContrasena =  findViewById(R.id.txtNuevaContrasenaConfirmar);

    }
}
