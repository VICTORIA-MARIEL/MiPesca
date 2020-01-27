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
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.services.UsuarioService;
import mx.com.sit.pesca.util.Constantes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecuperarContrasenaActivity extends AppCompatActivity {
    private static final String TAG = "RecuperarContrasenaAct";
    private EditText txtUsuario;
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);
        bindUi();
        realm = Realm.getDefaultInstance();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Button btnEnviar = findViewById(R.id.btnRecuperarEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                final String usuario = txtUsuario.getText().toString();
                if (!TextUtils.isEmpty(usuario)) {
                    UsuarioEntity dto = realm.where(UsuarioEntity.class)
                            .equalTo("usuarioLogin", usuario)
                            //.equalTo("usuarioEstatus", 1)
                            .findFirst();
                    if(dto!=null) {
                        //Terminar registro
                        if(dto.getUsuarioEstatus() == 0){
                            Log.d(TAG,"Nivel 1 goToRecuperarUsuario>" + dto.getId());
                            usuarioId = dto.getId();
                            goToRecuperarUsuario(usuarioId);
                        }
                        else {
                            Log.d(TAG,"Nivel 2 goToNuevaContrasena>" + dto.getId());
                            usuarioId = dto.getId();
                            goToNuevaContrasena(usuarioId);
                        }
                    }
                    else{
                        Log.d(TAG,"Nivel 3 Buscando remotamente>");
                        //buscarlo remotamente
                        UsuarioService service = retrofit.create(UsuarioService.class);
                        Call<PescadorList> usuCall = service.existe(
                                usuario
                        );
                        usuCall.enqueue(new Callback<PescadorList>(){
                            @Override
                            public void onResponse(Call<PescadorList> call, Response<PescadorList> response) {
                                PescadorList resultado = response.body();
                                Log.d(TAG,"resultado: " + resultado.isSuccess());
                                if(resultado.isSuccess()){ //el usuario se encuentra en la bd remota
                                    //recuperar usuario
                                    UsuarioEntity usuarioEntity = new UsuarioEntity(
                                            usuario,
                                            resultado.getUsuarioNombre(),
                                            ""
                                    );
                                    realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    realm.copyToRealm(usuarioEntity);
                                    realm.commitTransaction();
                                    realm.close();

                                    goToRecuperarUsuario(usuarioEntity.getId());
                                }
                                else{
                                    Toast.makeText(RecuperarContrasenaActivity.this, "Usuario no encontrado.",Toast.LENGTH_LONG).show();
                                    txtUsuario.requestFocus();
                                    txtUsuario.setText("");
                                }
                            }

                            @Override
                            public void onFailure(Call<PescadorList> call, Throwable t) {
                                txtUsuario.setText("");
                                txtUsuario.requestFocus();
                                Toast.makeText(RecuperarContrasenaActivity.this, "Error: El usuario ya se encuentra registrado.", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                }
                else{
                    Toast.makeText(RecuperarContrasenaActivity.this, "Usuario no encontrado.",Toast.LENGTH_LONG).show();
                    txtUsuario.requestFocus();
                    txtUsuario.setText("");
                }
            }

        });

        Button btnCancelar = findViewById(R.id.btnRecuperarCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goToLogin();
            }
        });

        Log.d(TAG, "Terminando onCreate");
    }

    private void bindUi(){
        txtUsuario = findViewById(R.id.txtRecuperarUsuario);

    }

    private void goToNuevaContrasena(int usuarioId){
        Intent intent = new Intent(RecuperarContrasenaActivity.this, NuevaContrasenaActivity.class);
        intent.putExtra("usuarioId",usuarioId);
        startActivity(intent);

    }

    private void goToRecuperarUsuario(int usuarioId){
        Intent intent = new Intent(RecuperarContrasenaActivity.this, RecuperarUsuarioActivity.class);
        intent.putExtra("usuarioId",usuarioId);
        startActivity(intent);
    }

    private void goToLogin(){
        Intent intent = new Intent(RecuperarContrasenaActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //evitar el atras
        startActivity(intent);

    }

}
