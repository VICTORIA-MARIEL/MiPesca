package mx.com.sit.pesca.fragmentos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import io.realm.annotations.Required;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.activity.PescadorRegistroActivity;
import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.CooperativaEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;


public class ConfiguracionFragment extends Fragment {
    private static final String TAG = "ConfiguracionFragment";

    private SharedPreferences prefs;
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private PescadorEntity pescador;
    private UsuarioEntity usuario;

    private UsuarioEntity usuarioDos;
    private PescadorEntity pescadorDos;

    private Spinner comboPreguntas;
    private EditText txtUsuario;
    private EditText txtContrasena;
    private EditText txtRespuesta;
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
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private ImageView iconFhNacimiento;
    private ImageView iconMunicipio;
    private ImageView iconComunidad;
    private ImageView iconCooperativa;
    private ImageView iconPermiso;
    private ImageView iconCambioMunicipio;
    private ImageView iconCambioComunidad;
    private ImageView iconCambioCooperativa;

    private String[] listaCooperativas;
    private String[] listaMunicipios;
    private String[] listaComunidades;
    private String[] listaPermisos;

    private RelativeLayout rlCooperativa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        Log.d(TAG, "Terminando onCreateView");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindUi(view);

        ArrayAdapter<CharSequence> adapterPreguntas = ArrayAdapter.createFromResource(this.getContext() ,R.array.preguntas,android.R.layout.simple_spinner_item );
        comboPreguntas.setAdapter(adapterPreguntas);


        realm = Realm.getDefaultInstance();

        RealmResults<MunicipioEntity> result = realm.where(MunicipioEntity.class).findAll();
        ArrayList<String> findStringList = new ArrayList<String>();
        for (MunicipioEntity municipio : result) {
            findStringList.add(municipio.getMunicipioDescLarga());
        }
        listaMunicipios = Arrays.copyOf(findStringList.toArray(), findStringList.toArray().length,String[].class);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaMunicipios);
        txtMunicipio.setAdapter(adapter1);

        listaComunidades = new String[] {};

        RealmResults<CooperativaEntity> result2 = realm.where(CooperativaEntity.class).findAll();
        ArrayList<String> findStringList2 = new ArrayList<String>();
        for (CooperativaEntity cooperativa: result2) {
            findStringList2.add(cooperativa.getCooperativaDescripcion());
        }
        listaCooperativas = Arrays.copyOf(findStringList2.toArray(), findStringList2.toArray().length,String[].class);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaCooperativas);
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
                        (getContext(), android.R.layout.select_dialog_item, listaComunidades);
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
                        (getContext(), android.R.layout.select_dialog_item, listaCooperativas);
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

        /*txtPermiso.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Permiso>" + itemSelected);
                PermisoEntity permisoDTO = realm.where(PermisoEntity.class).equalTo("permisoNumero",itemSelected).findFirst();

                txtPermisoId.setText("" + permisoDTO.getId());
                realm.close();
            }
        });*/

        chkIndependiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkIndependiente.isChecked()){
//                    txtComunidad.setVisibility(View.GONE);
                    txtCooperativa.setVisibility(View.GONE);
                    txtComunidadId.setText("0");
                    txtCooperativaId.setText("0");
                    rlCooperativa.setVisibility(View.GONE);
                }
                else{
//                    txtComunidad.setVisibility(View.VISIBLE);
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

        /*iconPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPermiso.showDropDown();
            }
        });*/

        iconCambioMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMunicipio.setText("");
                txtMunicipioId.setText("0");
                iconCambioMunicipio.setVisibility(View.GONE);
                iconMunicipio.setVisibility(View.VISIBLE);
                txtMunicipio.setEnabled(true);
            }
        });


        iconCambioComunidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtComunidad.setText("");
                txtComunidadId.setText("0");
                iconCambioComunidad.setVisibility(View.GONE);
                iconComunidad.setVisibility(View.VISIBLE);
                txtComunidad.setEnabled(true);
            }
        });


        iconCambioCooperativa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCooperativa.setText("");
                txtCooperativaId.setText("0");
                iconCambioCooperativa.setVisibility(View.GONE);
                iconCooperativa.setVisibility(View.VISIBLE);
                txtCooperativa.setEnabled(true);
            }
        });

        iconFhNacimiento.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);
                        DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Material_Dialog_MinWidth, mDateSetListener, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        txtUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtUsuario.setHint(R.string.lblConfUsuarioLogin);
                } else {
                    txtUsuario.setHint("");
                }
            }
        });
        txtContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtContrasena.setHint(R.string.lblConfUsuarioContrasena);
                } else {
                    txtContrasena.setHint("");
                }
            }
        });
        txtRespuesta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtRespuesta.setHint(R.string.lblConfPescadorRespuesta);
                } else {
                    txtRespuesta.setHint("");
                }
            }
        });
        txtNombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtNombre.setHint(R.string.lblConfPescadorNombre);
                } else {
                    txtNombre.setHint("");
                }
            }
        });
        txtPermiso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtPermiso.setHint(R.string.lblConfPescadorPermiso);
                } else {
                    txtPermiso.setHint("");
                }
            }
        });
        txtFhNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtFhNacimiento.setHint(R.string.lblConfPescadorFhNacimiento);
                } else {
                    txtFhNacimiento.setHint("");
                }
            }
        });
        txtTelefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtTelefono.setHint(R.string.lblConfPescadorTelefono);
                } else {
                    txtTelefono.setHint("");
                }
            }
        });
        txtCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtCorreo.setHint(R.string.lblConfPescadorEmail);
                } else {
                    txtCorreo.setHint("");
                }
            }
        });
        txtMunicipio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtMunicipio.setHint(R.string.lblConfPescadorMunicipio);
                } else {
                    txtMunicipio.setHint("");
                }
            }
        });
        txtComunidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtComunidad.setHint(R.string.lblConfPescadorComunidad);
                } else {
                    txtComunidad.setHint("");
                }
            }
        });
        txtCooperativa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtCooperativa.setHint(R.string.lblConfPescadorCooperativa);
                } else {
                    txtCooperativa.setHint("");
                }
            }
        });


        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()) {
                    insertConfiguracion();
                }
            }
        });

        usuario = realm.where(UsuarioEntity.class)
                .equalTo("id", usuarioId)
                .findFirst();

        pescador = realm.where(PescadorEntity.class)
                .equalTo("id", pescadorId)
                .findFirst();

        if(pescador!=null && usuario!=null) {
            txtUsuario.setText(usuario.getUsuarioLogin());
            txtContrasena.setText(usuario.getUsuarioContrasena());
            comboPreguntas.setSelection(adapterPreguntas.getPosition(pescador.getPescadorPregunta()));
            txtRespuesta.setText(pescador.getPescadorRespuesta());
            txtNombre.setText(pescador.getPescadorNombre());
            txtPermiso.setText(pescador.getPescadorPermiso());
            txtFhNacimiento.setText(Util.convertDateToString(pescador.getPescadorFhNacimiento(), Constantes.formatoFecha));
            txtTelefono.setText(pescador.getPescadorTelefono());
            txtCorreo.setText(pescador.getPescadorCorreo());
            txtComunidadId.setText(""+pescador.getComunidadId());
            txtMunicipioId.setText(""+pescador.getMunicipioId());
            txtComunidad.setText(pescador.getComunidadDescripcion());
            txtMunicipio.setText(pescador.getMunicipioDescripcion());
            if(pescador.getPescadorEsIndependiente() == 1) {
                chkIndependiente.setChecked(true);
                txtCooperativa.setVisibility(View.GONE);
                txtCooperativaId.setText("0");
                txtCooperativa.setText("");
                rlCooperativa.setVisibility(View.GONE);
//                txtCooperativa.setVisibility(View.GONE);
            }
            else {
                chkIndependiente.setChecked(false);
                txtCooperativa.setText(""+pescador.getCooperativaDescripcion());
  //              txtCooperativaId.setText(""+pescador.getCooperativaId());
                txtCooperativaId.setText(""+pescador.getCooperativaId());
            }
            iconCambioCooperativa.setVisibility(View.VISIBLE);
            iconCooperativa.setVisibility(View.GONE);
            iconCambioMunicipio.setVisibility(View.VISIBLE);
            iconCambioComunidad.setVisibility(View.VISIBLE);
            iconComunidad.setVisibility(View.GONE);
            iconMunicipio.setVisibility(View.GONE);
            //iconPermiso.setVisibility(View.VISIBLE);

        }
        realm.close();

    }

    private boolean validarCampos(){
        Log.d(TAG, "Iniciando validarCampos()");
        boolean exito = true;


        if(exito && TextUtils.isEmpty(txtUsuario.getText().toString()) && txtUsuario.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(getContext(), "Favor de ingresar un usuario.", Toast.LENGTH_LONG).show();
            exito = false;
            txtUsuario.setText("");
            txtUsuario.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtContrasena.getText().toString()) && txtContrasena.getText().toString().length() <= 8){
            Toast.makeText(getContext(), "Favor de ingresar una contrasena (mínimo 8 caracteres).", Toast.LENGTH_LONG).show();
            exito = false;
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtNombre.getText().toString()) && txtNombre.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(getContext(), "Favor de ingresar su nombre completo.", Toast.LENGTH_LONG).show();
            exito = false;
            txtNombre.setText("");
            txtNombre.requestFocus();
        }
       /* if(exito && TextUtils.isEmpty(txtPermiso.getText().toString()) && txtPermiso.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(getContext(), "Favor de ingresar el número de permiso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermiso.setText("");
            txtPermiso.requestFocus();
        }*/

        if(exito && TextUtils.isEmpty(txtFhNacimiento.getText().toString()) && txtFhNacimiento.getText().toString().length() <= 10){
            Toast.makeText(getContext(), "Favor de ingresar su fecha de nacimiento.", Toast.LENGTH_LONG).show();
            exito = false;
            txtFhNacimiento.setText("");
            txtFhNacimiento.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtTelefono.getText().toString()) && txtTelefono.getText().toString().length() <= 10){
            Toast.makeText(getContext(), "Favor de ingresar su número de teléfono a diez dígitos.", Toast.LENGTH_LONG).show();
            exito = false;
            txtTelefono.setText("");
            txtTelefono.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtCorreo.getText().toString()) && txtCorreo.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(getContext(), "Favor de ingresar un correo electrónico.", Toast.LENGTH_LONG).show();
            exito = false;
            txtCorreo.setText("");
            txtCorreo.requestFocus();
        }
        try {
            if(exito && Integer.parseInt(txtMunicipioId.getText().toString()) <= 0){
                Toast.makeText(getContext(), "Favor de seleccionar un municipio.", Toast.LENGTH_LONG).show();
                exito = false;
                txtMunicipio.setText("");
                txtMunicipio.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(getContext(), "Favor de seleccionar un municipio.", Toast.LENGTH_LONG).show();
            txtMunicipio.setText("");
            txtMunicipio.requestFocus();
        }


        try {
            if(exito && Integer.parseInt(txtComunidadId.getText().toString()) <= 0){
                Toast.makeText(getContext(), "Favor de seleccionar una comunidad.", Toast.LENGTH_LONG).show();
                exito = false;
                txtComunidad.setText("");
                txtComunidad.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(getContext(), "Favor de seleccionar una comunidad.", Toast.LENGTH_LONG).show();
            txtComunidad.setText("");
            txtComunidad.requestFocus();
        }

        if(exito && !chkIndependiente.isChecked()) {

            try {
                if (exito && Integer.parseInt(txtCooperativaId.getText().toString()) <= 0) {
                    Toast.makeText(getContext(), "Favor de seleccionar una cooperativa.", Toast.LENGTH_LONG).show();
                    exito = false;
                    txtCooperativa.setText("");
                    txtCooperativa.requestFocus();
                }
            } catch (NumberFormatException nfe) {
                exito = false;
                Toast.makeText(getContext(), "Favor de seleccionar una cooperativa.", Toast.LENGTH_LONG).show();
                txtCooperativa.setText("");
                txtCooperativa.requestFocus();
            }
        }


        if(exito) {
            usuarioDos = new UsuarioEntity();
            pescadorDos = new PescadorEntity();
            usuarioDos.setId(usuarioId);
            usuarioDos.setUsuarioLogin(txtUsuario.getText().toString());
            usuarioDos.setUsuarioNombre(txtNombre.getText().toString());
            usuarioDos.setUsuarioContrasena(txtContrasena.getText().toString());
            usuarioDos.setUsuarioEstatus((short)1);
            pescadorDos.setId(pescadorId);
            pescadorDos.setPescadorUsrRegistro(usuarioId);
            pescadorDos.setPescadorNombre(txtNombre.getText().toString());
            pescadorDos.setPescadorFhNacimiento(Util.convertStringToDate(txtFhNacimiento.getText().toString(), "/"));
            pescadorDos.setPescadorTelefono(txtTelefono.getText().toString());
            pescadorDos.setPescadorCorreo(txtCorreo.getText().toString());
            pescadorDos.setMunicipioId(Integer.parseInt(txtMunicipioId.getText().toString()));
            pescadorDos.setComunidadId(Integer.parseInt(txtComunidadId.getText().toString()));
            pescadorDos.setCooperativaId(Integer.parseInt(txtCooperativaId.getText().toString()));
            pescadorDos.setPescadorPermiso(txtPermiso.getText().toString());
            pescadorDos.setPescadorEsIndependiente(chkIndependiente.isChecked() ? (short) 1 : (short) 0);
            pescadorDos.setPescadorFhRegistro(pescador.getPescadorFhRegistro());
            pescadorDos.setMunicipioDescripcion((txtMunicipio.getText().toString()));
            pescadorDos.setComunidadDescripcion((txtComunidad.getText().toString()));
            pescadorDos.setCooperativaDescripcion((txtCooperativa.getText().toString()));
            pescadorDos.setPescadorPregunta(comboPreguntas.getSelectedItem().toString());
            pescadorDos.setPescadorRespuesta(txtRespuesta.getText().toString());
            pescadorDos.setPescadorEstatus(pescador.getPescadorEstatus());
            pescadorDos.setPreguntaId(pescador.getPreguntaId());
            pescadorDos.setPescadorFhSincronizacion(pescador.getPescadorFhSincronizacion());
            pescadorDos.setPescadorEstatusSinc(pescador.getPescadorEstatusSinc());
            pescadorDos.setPescadorClaveConfirmacion(pescador.getPescadorClaveConfirmacion());
            Log.e(TAG, "Clave de confirmacion=>"+pescadorDos.getPescadorClaveConfirmacion());
        }

        Log.d(TAG, "Terminando validarCampos()");
        return exito;
    }




    private void insertConfiguracion(){
        Log.d(TAG, "Inicianproabndodo insertConfiguracion(PescadorEntity entity)");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(usuarioDos);
        realm.copyToRealmOrUpdate(pescadorDos);
        realm.commitTransaction();
        realm.close();
        Toast.makeText(this.getContext(), "Configuración guardada con éxito", Toast.LENGTH_LONG).show();
        Log.e(TAG, "Actualiceé usuario con ID=>" + usuario.getId());
        Log.e(TAG, "Actualicé pescador con ID=>" + pescador.getId());
        Log.d(TAG, "Terminando insertConfiguracion(PescadorEntity entity)");
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

    private void bindUi(View view){
        comboPreguntas = (Spinner)view.findViewById(R.id.selectPreguntas);
        rlCooperativa = view.findViewById(R.id.rlCooperativa);
        txtUsuario = view.findViewById(R.id.txtUsuario);
        txtContrasena = view.findViewById(R.id.txtContrasena);
        // txtPregunta = view.findViewById(R.id.txtPregunta);
        txtRespuesta = view.findViewById(R.id.txtRespuesta);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtPermiso = view.findViewById(R.id.txtPermiso);
        txtFhNacimiento = view.findViewById(R.id.txtFhNacimiento);
        txtTelefono = view.findViewById(R.id.txtTelefono);
        txtCorreo = view.findViewById(R.id.txtCorreo);
        chkIndependiente = view.findViewById(R.id.chkIndependiente);
        txtMunicipio = view.findViewById(R.id.txtMunicipio);
        txtComunidad = view.findViewById(R.id.txtComunidad);
        txtCooperativa = view.findViewById(R.id.txtCooperativa);

        txtMunicipioId = view.findViewById(R.id.txtMunicipioId);
        txtComunidadId = view.findViewById(R.id.txtComunidadId);
        txtCooperativaId = view.findViewById(R.id.txtCooperativaId);
        txtPermisoId = view.findViewById(R.id.txtPermisoId);
        iconFhNacimiento = view.findViewById(R.id.iconFhNacimiento);
        iconMunicipio = view.findViewById(R.id.iconMunicipio);
        iconComunidad = view.findViewById(R.id.iconComunidad);
        iconCooperativa = view.findViewById(R.id.iconCooperativa);
        iconPermiso =  view.findViewById(R.id.iconPermiso);
        iconCambioMunicipio = view.findViewById(R.id.iconCambioMunicipio);
        iconCambioComunidad = view.findViewById(R.id.iconCambioComunidad);
        iconCambioCooperativa = view.findViewById(R.id.iconCambioCooperativa);

    }

}
