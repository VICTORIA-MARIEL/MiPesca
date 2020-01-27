package mx.com.sit.pesca.activity;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.dto.PescadorList;
import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.CooperativaEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.services.UsuarioService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PescadorRegistroActivity extends AppCompatActivity {
    private static final String TAG = "PescadorActivity";

    private SharedPreferences prefs;
    private Realm realm;
    private Retrofit retrofit;

    private int usuarioId;
    private int pescadorId;
    private PescadorEntity pescador;
    private UsuarioEntity usuario;

    private EditText txtUsuario;
    private EditText txtContrasena;
    private EditText txtConfirmarContrasena;
    private EditText txtNombre;
    private EditText txtPermiso;
    private MaskEditText txtFhNacimiento;
    private EditText txtTelefono;
    private EditText txtCorreo;
    private Switch chkIndependiente;
    private AutoCompleteTextView txtMunicipio;
    private AutoCompleteTextView txtComunidad;
    private AutoCompleteTextView txtCooperativa;
    private TextView txtMunicipioId;
    private TextView txtComunidadId;
    private TextView txtCooperativaId;
    private TextView txtPermisoId;

    private ImageView iconFhNacimiento;
    private ImageView iconMunicipio;
    private ImageView iconComunidad;
    private ImageView iconCooperativa;
    private ImageView iconPermiso;

    private String[] listaCooperativas;
    private String[] listaComunidades;
    private String[] listaMunicipios;
    private String[] listaPermisos;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private UsuarioEntity usuarioEntity;
    private PescadorEntity pescadorEntity;
    private RelativeLayout rlCooperativa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Iniciando onCreate");
        setContentView(R.layout.activity_pescador_registro);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        bindUi();


        txtFhNacimiento.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        realm = Realm.getDefaultInstance();

        RealmResults<MunicipioEntity> resultMun = realm.where(MunicipioEntity.class).findAll();
        ArrayList<String> findStringListMun = new ArrayList<String>();
        for (MunicipioEntity municipio : resultMun) {
            findStringListMun.add(municipio.getMunicipioDescLarga());
        }
        listaMunicipios = Arrays.copyOf(findStringListMun.toArray(), findStringListMun.toArray().length,String[].class);
        final ArrayAdapter<String> adapterMun = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, listaMunicipios);
        txtMunicipio.setAdapter(adapterMun);


/*        RealmResults<ComunidadEntity> result = realm.where(ComunidadEntity.class).findAll();
        ArrayList<String> findStringList = new ArrayList<String>();
        for (ComunidadEntity comunidad : result) {
            findStringList.add(comunidad.getComunidadDescripcion());
        }
        listaComunidades = Arrays.copyOf(findStringList.toArray(), findStringList.toArray().length,String[].class);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, listaComunidades);
        txtComunidad.setAdapter(adapter1);
        */
    listaComunidades = new String[]{};

        RealmResults<CooperativaEntity> result2 = realm.where(CooperativaEntity.class).findAll();
        ArrayList<String> findStringList2 = new ArrayList<String>();
        for (CooperativaEntity cooperativa: result2) {
            findStringList2.add(cooperativa.getCooperativaDescripcion());
        }
        listaCooperativas = Arrays.copyOf(findStringList2.toArray(), findStringList2.toArray().length,String[].class);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, listaCooperativas);
        txtCooperativa.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();


        txtMunicipio.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Municipio>" + itemSelected);
                MunicipioEntity municipioDTO = realm.where(MunicipioEntity.class).equalTo("municipioDescLarga",itemSelected).findFirst();
                Log.d(TAG,"Municipio Id>" + municipioDTO.getMunicipioId());
                txtMunicipioId.setText("" + municipioDTO.getMunicipioId());

                RealmResults<ComunidadEntity> result3 = realm.where(ComunidadEntity.class).equalTo("municipioId",municipioDTO.getMunicipioId()).findAll();
                ArrayList<String> findStringList3 = new ArrayList<String>();
                for (ComunidadEntity comunidad: result3) {
                    Log.d(TAG,"Comunidad ID > " + comunidad.getComunidadId()+ " Comunidad Desc > " + comunidad.getComunidadDescripcion()+  " Municipio Id>" + comunidad.getMunicipioId());
                    findStringList3.add(comunidad.getComunidadDescripcion());
                }
                listaComunidades = Arrays.copyOf(findStringList3.toArray(), findStringList3.toArray().length,String[].class);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                        (PescadorRegistroActivity.this, android.R.layout.select_dialog_item, listaComunidades);
                txtComunidad.setAdapter(adapter3);
                realm.close();

            }
        });

        txtComunidad.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Comunidad>" + itemSelected);
                ComunidadEntity comunidadDTO = realm.where(ComunidadEntity.class).equalTo("comunidadDescripcion",itemSelected).findFirst();
                Log.d(TAG,"Comunidad Id>" + comunidadDTO.getComunidadId());
                txtComunidadId.setText("" + comunidadDTO.getComunidadId());
                RealmResults<CooperativaEntity> result3 = realm.where(CooperativaEntity.class).equalTo("comunidadId",comunidadDTO.getComunidadId()).findAll();
                ArrayList<String> findStringList3 = new ArrayList<String>();
                for (CooperativaEntity cooperativa: result3) {
                    Log.d(TAG,"Cooperativa ID > " + cooperativa.getCooperativaId()+ " Cooperativa Desc > " + cooperativa.getCooperativaDescripcion()+  " Comunidad Id>" + cooperativa.getComunidadId());
                    findStringList3.add(cooperativa.getCooperativaDescripcion());
                }
                listaCooperativas = Arrays.copyOf(findStringList3.toArray(), findStringList3.toArray().length,String[].class);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                        (PescadorRegistroActivity.this, android.R.layout.select_dialog_item, listaCooperativas);
                txtCooperativa.setAdapter(adapter3);
                realm.close();

            }
        });


        txtCooperativa.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Cooperativa>" + itemSelected);
                CooperativaEntity cooperativaDTO = realm.where(CooperativaEntity.class).equalTo("cooperativaDescripcion",itemSelected).findFirst();

                txtCooperativaId.setText("" + cooperativaDTO.getCooperativaId());
                realm.close();
            }
        });
/*
        txtPermiso.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Permiso>" + itemSelected);
                PermisoEntity permisoDTO = realm.where(PermisoEntity.class).equalTo("permisoNumero",itemSelected).findFirst();

                txtPermisoId.setText("" + permisoDTO.getId());
                realm.close();
            }
        });
*/
        chkIndependiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkIndependiente.isChecked()){
                    rlCooperativa.setVisibility(View.GONE);
                    txtCooperativa.setVisibility(View.GONE);
                    txtCooperativaId.setText("0");
                }
                else{
                    txtCooperativa.setVisibility(View.VISIBLE);
                    rlCooperativa.setVisibility(View.VISIBLE);
                }
            }
        });

        /*Acciones de los iconos drop down list*/
        iconMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMunicipio.showDropDown();
            }
        });

        /*Acciones de los iconos drop down list*/
        iconComunidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtComunidad.showDropDown();
            }
        });

        iconCooperativa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCooperativa.showDropDown();
            }
        });
/*
        iconPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPermiso.showDropDown();
            }
        });
*/
        iconFhNacimiento.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);
                        DatePickerDialog dialog = new DatePickerDialog(PescadorRegistroActivity.this, mDateSetListener, year, month, day);
                        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }

        );

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month) + "/" +  String.format("%04d", year);
                txtFhNacimiento.setText(date);
            }
        };
        /*Accciones de etiquetas*/
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
//                            txtContrasena.requestFocus();
                        }
                    }
                    txtConfirmarContrasena.setHint("");
                }
                else{

                    txtConfirmarContrasena.setHint(R.string.lblUsuarioConfContrasena);
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

                    txtContrasena.setHint(R.string.lblUsuarioContrasena);
                }
            }
        });

        txtUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (!TextUtils.isEmpty(txtUsuario.getText().toString())) {
                        UsuarioService service = retrofit.create(UsuarioService.class);
                        Call<PescadorList> usuCall = service.existe(
                                txtUsuario.getText().toString()
                        );
                        usuCall.enqueue(new Callback<PescadorList>(){
                            @Override
                            public void onResponse(Call<PescadorList> call, Response<PescadorList> response) {
                                PescadorList resultado = response.body();
                                Log.d(TAG,"resultado: " + resultado.isSuccess());
                                if(resultado.isSuccess()){ //el usuario se encuentra en la bd remota
                                    realm = Realm.getDefaultInstance();
                                    UsuarioEntity dto = realm.where(UsuarioEntity.class)
                                            .equalTo("usuarioLogin", txtUsuario.getText().toString())
                                            .findFirst();
                                    if(dto!=null) { //el usuario se encuentra en la bd local, se requiere recuperarlo
                                        Toast.makeText(getApplicationContext(), "Error: El nombre de usuario ya se encuentra en uso.", Toast.LENGTH_LONG).show();
                                        txtUsuario.setText("");
                                        txtUsuario.requestFocus();
                                    }
                                    realm.close();
                                }
                               else{
                                    realm = Realm.getDefaultInstance();
                                    UsuarioEntity dto = realm.where(UsuarioEntity.class)
                                            .equalTo("usuarioLogin", txtUsuario.getText().toString())
                                            .findFirst();
                                    if(dto!=null) { //el usuario se encuentra en la bd local, pero no remotamente.
                                        Toast.makeText(getApplicationContext(), "Error: El nombre de usuario se encuentra preregistrado. Ingrese a la sección \"Recuperar usuario\".", Toast.LENGTH_LONG).show();
                                        txtUsuario.setText("");
                                        txtUsuario.requestFocus();
                                    }
                                    realm.close();
                                }
                            }

                            @Override
                            public void onFailure(Call<PescadorList> call, Throwable t) {
                                txtUsuario.setText("");
                                txtUsuario.requestFocus();
                                Toast.makeText(PescadorRegistroActivity.this, "Error: El usuario ya se encuentra registrado.", Toast.LENGTH_LONG).show();
                            }
                        });



                    }
                    txtUsuario.setHint("");
                }
                else{
                    txtUsuario.setHint(R.string.lblUsuarioLogin);
                }
            }
        });



        txtNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtNombre.setHint(R.string.lblPescadorNombre);
                } else {
                    txtNombre.setHint("");
                }
            }
        });

        txtPermiso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtPermiso.setHint(R.string.lblPescadorPermiso);
                } else {
                    txtPermiso.setHint("");
                }
            }
        });

        txtFhNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtFhNacimiento.setHint(R.string.lblPescadorFhNacimiento);
                } else {
                    txtFhNacimiento.setHint("");
                }
            }
        });

        txtTelefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtTelefono.setHint(R.string.lblPescadorTelefono);
                } else {
                    txtTelefono.setHint("");
                }
            }
        });

        txtCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtCorreo.setHint(R.string.lblPescadorEmail);
                } else {
                    txtCorreo.setHint("");
                }
            }
        });

        txtMunicipio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtMunicipio.setHint(R.string.lblPescadorMunicipio);
                } else {
                    txtMunicipio.setHint("");
                }
            }
        });


        txtComunidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtComunidad.setHint(R.string.lblPescadorComunidad);
                } else {
                    txtComunidad.setHint("");
                }
            }
        });

        txtCooperativa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtCooperativa.setHint(R.string.lblPescadorCooperativa);
                } else {
                    txtCooperativa.setHint("");
                }
            }
        });


        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean exito = validarCampos();
                if(exito) {
                    insertUsuarioPescador(usuarioEntity, pescadorEntity);
                    Log.e(TAG, "Inserté usuario con ID=>" + usuarioEntity.getId());
                    Log.e(TAG, "Inserté pescador con ID=>" + pescadorEntity.getId());
                    goToConfirmacion(usuarioEntity.getId(), pescadorEntity.getId(), pescadorEntity.getPescadorClaveConfirmacion());
                }
/*                else{
                    Toast.makeText(PescadorRegistroActivity.this, "Error 100: Captura erronea de un dato.", Toast.LENGTH_LONG).show();
                }*/
            }
        });





        Log.d(TAG, "Terminando onCreate");
    }

    private boolean validarCampos(){
        Log.d(TAG, "Iniciando validarCampos()");
        boolean exito = true;


        if(exito && TextUtils.isEmpty(txtUsuario.getText().toString()) && txtUsuario.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this, "Favor de ingresar un usuario.", Toast.LENGTH_LONG).show();
            exito = false;
            txtUsuario.setText("");
            txtUsuario.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtContrasena.getText().toString()) && txtContrasena.getText().toString().length() <= 8){
            Toast.makeText(this, "Favor de ingresar una contrasena (mínimo 8 caracteres).", Toast.LENGTH_LONG).show();
            exito = false;
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtConfirmarContrasena.getText().toString()) && txtConfirmarContrasena.getText().toString().length() <= 8){
            Toast.makeText(this, "Favor de ingresar una contrasena (mínimo 8 caracteres).", Toast.LENGTH_LONG).show();
            exito = false;
            txtConfirmarContrasena.setText("");
            txtConfirmarContrasena.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtNombre.getText().toString()) && txtNombre.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this, "Favor de ingresar su nombre completo.", Toast.LENGTH_LONG).show();
            exito = false;
            txtNombre.setText("");
            txtNombre.requestFocus();
        }
/*        if(exito && TextUtils.isEmpty(txtPermiso.getText().toString()) && txtPermiso.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this, "Favor de ingresar el número de permiso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermiso.setText("");
            txtPermiso.requestFocus();
        }
*/
        if(exito && TextUtils.isEmpty(txtFhNacimiento.getText().toString()) && txtFhNacimiento.getText().toString().length() <= 10){
            Toast.makeText(this, "Favor de ingresar su fecha de nacimiento.", Toast.LENGTH_LONG).show();
            exito = false;
            txtFhNacimiento.setText("");
            txtFhNacimiento.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtTelefono.getText().toString()) && txtTelefono.getText().toString().length() <= 10){
            Toast.makeText(this, "Favor de ingresar su número de teléfono a diez dígitos.", Toast.LENGTH_LONG).show();
            exito = false;
            txtTelefono.setText("");
            txtTelefono.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtCorreo.getText().toString()) && txtCorreo.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this, "Favor de ingresar un correo electrónico.", Toast.LENGTH_LONG).show();
            exito = false;
            txtCorreo.setText("");
            txtCorreo.requestFocus();
        }

        try {
            if(exito && Integer.parseInt(txtMunicipioId.getText().toString()) <= 0){
                Toast.makeText(this, "Favor de seleccionar un municipio.", Toast.LENGTH_LONG).show();
                exito = false;
                txtMunicipio.setText("");
                txtMunicipio.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this, "Favor de seleccionar un municipio.", Toast.LENGTH_LONG).show();
            txtMunicipio.setText("");
            txtMunicipio.requestFocus();
        }

        try {
            if(exito && Integer.parseInt(txtComunidadId.getText().toString()) <= 0){
                Toast.makeText(this, "Favor de seleccionar una comunidad.", Toast.LENGTH_LONG).show();
                exito = false;
                txtComunidad.setText("");
                txtComunidad.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this, "Favor de seleccionar una comunidad.", Toast.LENGTH_LONG).show();
            txtComunidad.setText("");
            txtComunidad.requestFocus();
        }

        if(exito && !chkIndependiente.isChecked()) {

            try {
                if (exito && Integer.parseInt(txtCooperativaId.getText().toString()) <= 0) {
                    Toast.makeText(this, "Favor de seleccionar una cooperativa.", Toast.LENGTH_LONG).show();
                    exito = false;
                    txtCooperativa.setText("");
                    txtCooperativa.requestFocus();
                }
            } catch (NumberFormatException nfe) {
                exito = false;
                Toast.makeText(this, "Favor de seleccionar una cooperativa.", Toast.LENGTH_LONG).show();
                txtCooperativa.setText("");
                txtCooperativa.requestFocus();
            }
        }


        if(exito) {
            usuarioEntity = new UsuarioEntity(
                    txtUsuario.getText().toString(),
                    txtNombre.getText().toString(),
                    txtContrasena.getText().toString()
            );


            pescadorEntity = new PescadorEntity(
                    txtNombre.getText().toString(),
                    Util.convertStringToDate(txtFhNacimiento.getText().toString(), "/"),
                    txtTelefono.getText().toString(),
                    txtCorreo.getText().toString(),
                    Integer.parseInt(txtMunicipioId.getText().toString()),
                    Integer.parseInt(txtComunidadId.getText().toString()),
                    Integer.parseInt(txtCooperativaId.getText().toString()),
                    txtPermiso.getText().toString(),
                    chkIndependiente.isChecked() ? (short) 1 : (short) 0,
                    txtMunicipio.getText().toString(),
                    txtComunidad.getText().toString(),
                    txtCooperativa.getText().toString()
            );
            Log.e(TAG, "Clave de confirmacion=>"+pescadorEntity.getPescadorClaveConfirmacion());
        }

        Log.d(TAG, "Terminando validarCampos()");
        return exito;
    }



    private void goToConfirmacion(int usuarioId,int pescadorId, String claveConfirmacion){
        Intent intent = new Intent(PescadorRegistroActivity.this, ConfirmarRegistroActivity.class);
        intent.putExtra("pescadorId",pescadorId);
        intent.putExtra("usuarioId",usuarioId);
        intent.putExtra("cve",claveConfirmacion);
        intent.putExtra("movto","insertar");
        startActivity(intent);
    }

    private void goToLogin(){
        Intent intent = new Intent(PescadorRegistroActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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



    private void bindUi(){
        Log.d(TAG, "Iniciando bindUi()");
        txtUsuario= findViewById(R.id.txtUsuario);
        txtContrasena= findViewById(R.id.txtContrasena);
        txtConfirmarContrasena= findViewById(R.id.txtConfirmarContrasena);
        txtNombre= findViewById(R.id.txtNombre);
        txtPermiso = findViewById(R.id.txtPermiso);
        txtFhNacimiento= findViewById(R.id.txtFhNacimiento);
        txtTelefono= findViewById(R.id.txtTelefono);
        txtCorreo= findViewById(R.id.txtCorreo);
        chkIndependiente = findViewById(R.id.chkIndependiente);
        txtMunicipio= findViewById(R.id.txtMunicipio);
        txtComunidad= findViewById(R.id.txtComunidad);
        rlCooperativa  = findViewById(R.id.rlCooperativa);
        txtCooperativa= findViewById(R.id.txtCooperativa);
        txtMunicipioId = findViewById(R.id.txtMunicipioId);
        txtComunidadId = findViewById(R.id.txtComunidadId);
        txtPermisoId = findViewById(R.id.txtPermisoId);
        txtCooperativaId = findViewById(R.id.txtCooperativaId);

        iconFhNacimiento = findViewById(R.id.iconFhNacimiento);
        iconMunicipio = findViewById(R.id.iconMunicipio);
        iconComunidad = findViewById(R.id.iconComunidad);
        iconCooperativa = findViewById(R.id.iconCooperativa);
        iconPermiso =  findViewById(R.id.iconPermiso);
        Log.d(TAG, "Terminando bindUi()");
    }



}
