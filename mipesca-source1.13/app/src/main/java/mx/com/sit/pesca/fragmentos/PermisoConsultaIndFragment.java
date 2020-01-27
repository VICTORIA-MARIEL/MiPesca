package mx.com.sit.pesca.fragmentos;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.ArtePescaEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class PermisoConsultaIndFragment extends Fragment {
    private static final String TAG = "PermisoConsultaIndFragm";
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private PermisoEntity permiso;
    private int permisoId;
    private SharedPreferences prefs;
    private String municipioDesc;

    private TextView txtPermisoId;
    private TextView txtPescadorId;
    private TextView txtUsuarioId;
    //private TextView txtArtepescaId;
    //private AutoCompleteTextView txtArtepesca;
    private EditText txtPermisoArtepesca;
    private EditText txtPermisoNumero;
    private EditText txtPermisoNombreEmbarcacion;
    private EditText txtPermisoRnpaEmbarcacion;
    private EditText txtPermisoMatricula;
    private  EditText txtPermisoNoEmbarcaciones;
    private  EditText txtPermisoSitioDesembarque;
    private EditText txtPermisoSitioDesembarqueClave;
    private EditText txtPermisoZonaPesca;

    private EditText txtPermisoTonelaje;
    private EditText txtPermisoMarcaMotor;
    private EditText txtPermisoPotenciaHp;
    private EditText txtPermisoFhVigenciaInicio;
    private EditText txtPermisoFhVigenciaFin;
    private TextView txtPermisoFhVigenciaDuracion;
    private EditText txtPermisoFhExpedicion;
    private EditText txtPermisoParaPesqueria;
    private EditText txtPermisoArteCantidad;
    private EditText txtPermisoArteCaracteristica;
    private EditText txtPermisoLugarExpedicion;
    private EditText txtPermisoTitular;
    private AutoCompleteTextView txtMunicipio;
    private TextView txtMunicipioId;
    private TextView txtEstado;
    private TextView txtEstadoId;
    private ImageView iconMunicipio;

    private Button btnPermisoEditar;
    private Button btnPermisoRegresar;
    //private String[] listaArtespesca;
    private String[] listaMunicipios;


    public PermisoConsultaIndFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_permiso_consulta_ind, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        bindUi(view);

        Bundle data = this.getArguments();
        if(data != null) {
            permisoId = data.getInt("permisoId",0);
        }
        //Obtener información del permiso.
        realm = Realm.getDefaultInstance();
        this.permiso = realm.where(PermisoEntity.class).equalTo("id",permisoId).findFirst();
        realm.close();

        /*Llenado de combos*/
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

        realm.close();

        /*Acciones de ItemOnClickListener*/
        txtMunicipio.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Municipio>" + itemSelected);
                MunicipioEntity municipioDTO = realm.where(MunicipioEntity.class).equalTo("municipioDescLarga",itemSelected).findFirst();
                Log.d(TAG,"Municipio Id>" + municipioDTO.getMunicipioId());
                municipioDesc = municipioDTO.getMunicipioDescripcion();
                txtMunicipioId.setText("" + municipioDTO.getMunicipioId());
                txtEstadoId.setText("" + municipioDTO.getEstadoId());
                txtEstado.setText("" + municipioDTO.getEstadoDescripcion());

                realm.close();
            }
        });

        iconMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMunicipio.showDropDown();
            }
        });

        llenarCampos();


        btnPermisoEditar = view.findViewById(R.id.btnPermisoEditar);
        btnPermisoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    int permisoId = editarPermiso(permiso);
                    Toast.makeText(getContext(), "La información del permiso se editó correctamente.", Toast.LENGTH_LONG).show();
                    goToPermisoConsulta(permisoId);
                }
            }
        });

        btnPermisoRegresar = view.findViewById(R.id.btnPermisoRegresar);
        btnPermisoRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermisoConsultaFragment fragmentoNvo = new PermisoConsultaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentoNvo).commit();

            }
        });


        Log.d(TAG, "Terminando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        return view;
    }

    private void bindUi(View view){
        Log.d(TAG, "Iniciando bindUi()");
        txtPermisoId = view.findViewById(R.id.txtPermisoId);
        txtPescadorId = view.findViewById(R.id.txtPescadorId);
        txtUsuarioId = view.findViewById(R.id.txtUsuarioId);
        //txtArtepescaId = view.findViewById(R.id.txtArtepescaId);
        txtPermisoFhVigenciaInicio = view.findViewById(R.id.txtPermisoFhVigenciaInicio);
        //txtArtepesca = view.findViewById(R.id.txtArtepesca);
        txtPermisoArtepesca = view.findViewById(R.id.txtPermisoArtepesca);
        txtPermisoNumero = view.findViewById(R.id.txtPermisoNumero);
        txtPermisoNombreEmbarcacion = view.findViewById(R.id.txtPermisoNombreEmbarcacion);
        txtPermisoRnpaEmbarcacion = view.findViewById(R.id.txtPermisoRnpaEmbarcacion);
        txtPermisoMatricula = view.findViewById(R.id.txtPermisoMatricula);
        txtPermisoNoEmbarcaciones = view.findViewById(R.id.txtPermisoNoEmbarcaciones);
        txtPermisoSitioDesembarque = view.findViewById(R.id.txtPermisoSitioDesembarque);
        txtPermisoSitioDesembarqueClave = view.findViewById(R.id.txtPermisoSitioDesembarqueClave);
        txtPermisoZonaPesca = view.findViewById(R.id.txtPermisoZonaPesca);

        txtPermisoFhVigenciaDuracion = view.findViewById(R.id.txtPermisoFhVigenciaDuracion);
        txtPermisoTonelaje = view.findViewById(R.id.txtPermisoTonelaje);
        txtPermisoMarcaMotor = view.findViewById(R.id.txtPermisoMarcaMotor);
        txtPermisoPotenciaHp = view.findViewById(R.id.txtPermisoPotenciaHp);
        txtPermisoFhVigenciaInicio = view.findViewById(R.id.txtPermisoFhVigenciaInicio);
        txtPermisoFhVigenciaFin = view.findViewById(R.id.txtPermisoFhVigenciaFin);
        txtPermisoFhExpedicion = view.findViewById(R.id.txtPermisoFhExpedicion);
        txtPermisoParaPesqueria = view.findViewById(R.id.txtPermisoParaPesqueria);
        txtPermisoArteCantidad = view.findViewById(R.id.txtPermisoArteCantidad);
        txtPermisoArteCaracteristica = view.findViewById(R.id.txtPermisoArteCaracteristica);
        txtPermisoLugarExpedicion = view.findViewById(R.id.txtPermisoLugarExpedicion);
        txtPermisoTitular = view.findViewById(R.id.txtPermisoTitular);
        txtMunicipio = view.findViewById(R.id.txtMunicipio);
        txtMunicipioId = view.findViewById(R.id.txtMunicipioId);
        txtEstado = view.findViewById(R.id.txtEstado);
        txtEstadoId = view.findViewById(R.id.txtEstadoId);
        iconMunicipio = (ImageView)view.findViewById(R.id.iconMunicipio);
        Log.d(TAG, "Terminando bindUi()");
    }

    private boolean validarCampos(){
        boolean exito = true;
        if(usuarioId == 0){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
        }
        if(exito && pescadorId == 0){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
        }



        if(exito && TextUtils.isEmpty(txtPermisoNumero.getText().toString()) && txtPermisoNumero.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el número de permiso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoNumero.setText("");
            txtPermisoNumero.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtPermisoTitular.getText().toString()) && txtPermisoTitular.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el titular del permiso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoTitular.setText("");
            txtPermisoTitular.requestFocus();
        }
        if(exito && Integer.parseInt(txtMunicipioId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar un municipio.", Toast.LENGTH_LONG).show();
            exito = false;
            txtMunicipio.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtPermisoFhVigenciaInicio.getText().toString()) && txtPermisoFhVigenciaInicio.getText().toString().length() <= 10){
            Toast.makeText(this.getContext(), "Favor de ingresar la fecha de inicio de vigencia.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoFhVigenciaInicio.setText("");
            txtPermisoFhVigenciaInicio.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtPermisoFhVigenciaFin.getText().toString()) && txtPermisoFhVigenciaFin.getText().toString().length() <= 10){
            Toast.makeText(this.getContext(), "Favor de ingresar la fecha de término de vigencia.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoFhVigenciaFin.setText("");
            txtPermisoFhVigenciaFin.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoNombreEmbarcacion.getText().toString()) && txtPermisoNombreEmbarcacion.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el nombre embarcación.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoNombreEmbarcacion.setText("");
            txtPermisoNombreEmbarcacion.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoRnpaEmbarcacion.getText().toString()) && txtPermisoRnpaEmbarcacion.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el RNPA de la embarcación.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoRnpaEmbarcacion.setText("");
            txtPermisoRnpaEmbarcacion.requestFocus();
        }
        try {
            if(exito && Integer.parseInt(txtPermisoNoEmbarcaciones.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar el número de embarcaciones.", Toast.LENGTH_LONG).show();
                exito = false;
                txtPermisoNoEmbarcaciones.setText("");
                txtPermisoNoEmbarcaciones.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
            txtPermisoNoEmbarcaciones.setText("");
            txtPermisoNoEmbarcaciones.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoMatricula.getText().toString()) && txtPermisoMatricula.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar la matrícula.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoMatricula.setText("");
            txtPermisoMatricula.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoSitioDesembarqueClave.getText().toString()) && txtPermisoSitioDesembarqueClave.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar la clave de desembarque.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoSitioDesembarqueClave.setText("");
            txtPermisoSitioDesembarqueClave.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoSitioDesembarque.getText().toString()) && txtPermisoSitioDesembarque.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el sitio de desembarque.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoSitioDesembarque.setText("");
            txtPermisoSitioDesembarque.requestFocus();
        }
        /*if(exito && Integer.parseInt(txtArtepescaId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar el arte de pesca.", Toast.LENGTH_LONG).show();
            exito = false;
            txtArtepesca.requestFocus();
        }*/
        if(exito && TextUtils.isEmpty(txtPermisoArtepesca.getText().toString()) && txtPermisoArtepesca.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el arte de pesca.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoArtepesca.setText("");
            txtPermisoArtepesca.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtPermisoZonaPesca.getText().toString()) && txtPermisoZonaPesca.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar la zona de pesca.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoZonaPesca.setText("");
            txtPermisoZonaPesca.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtPermisoTonelaje.getText().toString()) && txtPermisoTonelaje.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el tonelaje de pesca (kgs).", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoTonelaje.setText("");
            txtPermisoTonelaje.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoMarcaMotor.getText().toString()) && txtPermisoMarcaMotor.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar la marca del motor.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoMarcaMotor.setText("");
            txtPermisoMarcaMotor.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoPotenciaHp.getText().toString()) && txtPermisoPotenciaHp.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar la potencia del motor (hp).", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoPotenciaHp.setText("");
            txtPermisoPotenciaHp.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoLugarExpedicion.getText().toString()) && txtPermisoLugarExpedicion.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el lugar de expedición.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoLugarExpedicion.setText("");
            txtPermisoLugarExpedicion.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoFhExpedicion.getText().toString()) && txtPermisoFhExpedicion.getText().toString().length() <= 10){
            Toast.makeText(this.getContext(), "Favor de ingresar la fecha de expedición del permiso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoFhExpedicion.setText("");
            txtPermisoFhExpedicion.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoLugarExpedicion.getText().toString()) && txtPermisoLugarExpedicion.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar el lugar de expedición.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoLugarExpedicion.setText("");
            txtPermisoLugarExpedicion.requestFocus();
        }
        if(exito && TextUtils.isEmpty(txtPermisoParaPesqueria.getText().toString()) && txtPermisoParaPesqueria.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar para que pesquería.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoParaPesqueria.setText("");
            txtPermisoParaPesqueria.requestFocus();
        }
        if(exito && Integer.parseInt(txtPermisoArteCantidad.getText().toString()) < 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar la cantidad autorizada.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermisoArteCantidad.requestFocus();
        }

        if(exito) {
            permiso = new PermisoEntity(
                    pescadorId,
                    usuarioId,
                    txtPermisoNumero.getText().toString(),
                    txtPermisoNombreEmbarcacion.getText().toString(),
                    txtPermisoRnpaEmbarcacion.getText().toString(),
                    txtPermisoMatricula.getText().toString(),
                    Short.parseShort(txtPermisoNoEmbarcaciones.getText().toString()),
                    txtPermisoSitioDesembarque.getText().toString(),
                    txtPermisoSitioDesembarqueClave.getText().toString(),
                    txtPermisoZonaPesca.getText().toString(),
                    txtPermisoTonelaje.getText().toString(),
                    txtPermisoMarcaMotor.getText().toString(),
                    txtPermisoPotenciaHp.getText().toString(),
                    Util.convertStringToDate(txtPermisoFhVigenciaInicio.getText().toString(),"/"),
                    Util.convertStringToDate(txtPermisoFhVigenciaFin.getText().toString(),"/"),
                    Util.calcularDuracion(Util.convertStringToDate(txtPermisoFhVigenciaInicio.getText().toString(),"/"),Util.convertStringToDate(txtPermisoFhVigenciaFin.getText().toString(),"/")),
                    txtPermisoFhExpedicion.getText().toString(),
                    txtPermisoParaPesqueria.getText().toString(),
                    Short.parseShort(txtPermisoArteCantidad.getText().toString()),
                    txtPermisoArtepesca.getText().toString(),
                    txtPermisoArteCaracteristica.getText().toString(),
                    txtPermisoLugarExpedicion.getText().toString(),
                    txtPermisoTitular.getText().toString(),
                    txtEstado.getText().toString(),
                    Short.parseShort(txtEstadoId.getText().toString()),
                    municipioDesc,
                    Short.parseShort(txtMunicipioId.getText().toString())

            );
            permiso.setId(permisoId);
        }
        return exito;
    }


    private void llenarCampos(){
        if(permiso!=null){
            txtUsuarioId.setText("" + usuarioId);
            txtPescadorId.setText("" + pescadorId);
            //txtArtepescaId.setText("" +permiso.getArtePescaId());
            txtPermisoId.setText("" +permiso.getId());
            //txtArtepesca.setText("" + artepescaDesc);
            txtPermisoArtepesca.setText("" + permiso.getPermisoArtepesca());
            txtPermisoNumero.setText("" + permiso.getPermisoNumero());
            txtPermisoNombreEmbarcacion.setText("" + permiso.getPermisoNombreEmbarcacion());
            txtPermisoRnpaEmbarcacion.setText("" + permiso.getPermisoRnpaEmbarcacion());
            txtPermisoMatricula.setText("" +permiso.getPermisoMatricula());
            txtPermisoNoEmbarcaciones.setText("" +permiso.getPermisoNoEmbarcaciones());
            txtPermisoSitioDesembarque.setText("" +permiso.getPermisoSitioDesembarque());
            txtPermisoSitioDesembarqueClave.setText("" +permiso.getPermisoSitioDesembarqueClave());
            txtPermisoZonaPesca.setText("" +permiso.getPermisoZonaPesca());
            txtPermisoFhVigenciaDuracion.setText("" +permiso.getPermisoFhVigenciaDuracion());
            txtPermisoTonelaje.setText("" +permiso.getPermisoTonelaje());
            txtPermisoMarcaMotor.setText("" +permiso.getPermisoMarcaMotor());
            txtPermisoPotenciaHp.setText("" +permiso.getPermisoPotenciaHp());
            txtPermisoFhVigenciaInicio.setText("" +Util.convertDateToString( permiso.getPermisoFhVigenciaInicio(), Constantes.formatoFecha));
            txtPermisoFhVigenciaFin.setText("" +Util.convertDateToString( permiso.getPermisoFhVigenciaFin(), Constantes.formatoFecha));
            txtPermisoFhExpedicion.setText("" +permiso.getPermisoFhExpedicion());
            txtPermisoParaPesqueria.setText("" +permiso.getPermisoParaPesqueria());
            txtPermisoArteCantidad.setText("" +permiso.getPermisoArteCantidad());
            txtPermisoArteCaracteristica.setText("" +permiso.getPermisoArteCaracteristica());
            txtPermisoLugarExpedicion.setText("" +permiso.getPermisoLugarExpedicion());
            txtPermisoTitular.setText("" + permiso.getPermisoTitular());
            txtEstado.setText("" + permiso.getPermisoEstado());
            txtEstadoId.setText("" + permiso.getPermisoEstadoId());
            txtMunicipio.setText("" + permiso.getPermisoMunicipio());
            txtMunicipioId.setText("" + permiso.getPermisoMunicipioId());
        }
    }




    private void goToPermisoConsulta(int permisoId){
        Log.d(TAG, "Iniciando goToPermisoConsulta()");
        PermisoConsultaFragment fragmentoNvo = new PermisoConsultaFragment();
        Bundle data = new Bundle();
        fragmentoNvo.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentoNvo).commit();
        Log.d(TAG, "Terminando goToPermisoConsulta()");

    }

    private int editarPermiso(PermisoEntity permiso){
        Log.d(TAG, "Iniciando editarPermiso(PermisoEntity permiso)");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(permiso);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Act permiso ID >" +permiso.getId());
        Log.d(TAG, "Terminando editarPermiso(PermisoEntity permiso)");
        return permiso.getId();

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
