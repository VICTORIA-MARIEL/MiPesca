package mx.com.sit.pesca.fragmentos;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import mx.com.sit.pesca.dto.PermisoList;
import mx.com.sit.pesca.entity.ArtePescaEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.services.PermisoService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermisoFragment extends Fragment {
    private static final String TAG = "PermisoFragment";
    private SharedPreferences prefs;
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private PermisoEntity permiso;
    private String municipioDesc;

    private TextView txtPescadorId;
    private TextView txtUsuarioId;
    private TextView txtEstadoId;
    private TextView txtEstado;
    private TextView txtMunicipioId;
    private EditText txtPermisoTitular;
    private EditText txtPermisoArtepesca;
    private AutoCompleteTextView txtMunicipio;
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
    private MaskEditText txtPermisoFhVigenciaInicio;
    private MaskEditText txtPermisoFhVigenciaFin;
    private TextView txtPermisoFhVigenciaDuracion;
    private EditText txtPermisoFhExpedicion;
    private EditText txtPermisoParaPesqueria;
    private EditText txtPermisoArteCantidad;
    private EditText txtPermisoArteCaracteristica;
    private EditText txtPermisoLugarExpedicion;
    private ImageView iconMunicipio;

    private Button btnPermisoGuardar;
    private String[] listaMunicipios;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;


    public PermisoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_permiso, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d(TAG, "Terminando onCreateView");
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Iniciando onViewCreated(View view, Bundle savedInstanceState)");

        txtPescadorId = view.findViewById(R.id.txtPescadorId);
        txtUsuarioId = view.findViewById(R.id.txtUsuarioId);
        txtMunicipioId = view.findViewById(R.id.txtMunicipioId);
        txtEstadoId = view.findViewById(R.id.txtEstadoId);
        txtEstado = view.findViewById(R.id.txtEstado);


        txtPermisoFhVigenciaInicio = view.findViewById(R.id.txtPermisoFhVigenciaInicio);
        txtPermisoArtepesca = view.findViewById(R.id.txtPermisoArtepesca);
        txtPermisoTitular = view.findViewById(R.id.txtPermisoTitular);
        txtMunicipio = view.findViewById(R.id.txtMunicipio);

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
        iconMunicipio = (ImageView)view.findViewById(R.id.iconMunicipio);

        /*Parametros iniciales para os campos de texto*/
        txtMunicipioId.setText("0");
        txtEstadoId.setText("0");
        txtUsuarioId.setText("" + usuarioId);
        txtPescadorId.setText("" + pescadorId);
//        txtPermisoFhVigenciaInicio.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));
//        txtPermisoFhVigenciaFin.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));

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
        /*Acciones de calendario*/
        txtPermisoFhVigenciaInicio.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);
                        DatePickerDialog dialog = new DatePickerDialog(getContext(), mDateSetListener, year, month, day);
                        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }

        );

        txtPermisoFhVigenciaFin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);
                        DatePickerDialog dialog = new DatePickerDialog(getContext(), mDateSetListener2, year, month, day);
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
                txtPermisoFhVigenciaInicio.setText(date);
            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month) + "/" +  String.format("%04d", year);
                txtPermisoFhVigenciaFin.setText(date);
            }
        };

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
                txtEstado.setText("" + municipioDTO.getEstadoDescripcion());
                txtEstadoId.setText("" + municipioDTO.getEstadoId());

                realm.close();
            }
        });

        iconMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMunicipio.showDropDown();
            }
        });

        /*Acciones de los botones*/
        btnPermisoGuardar = view.findViewById(R.id.btnPermisoGuardar);
        btnPermisoGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {

                    if (validarCampos()) {
                        String usuarioLogin = "";
                        realm = Realm.getDefaultInstance();
                        UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class).equalTo("id", usuarioId).findFirst();
                        if (usuarioEntity != null) {
                            usuarioLogin = usuarioEntity.getUsuarioLogin();
                        }
                        realm.close();
                        PermisoService service = retrofit.create(PermisoService.class);
                        Call<PermisoList> perCall = service.insertPermiso(

                                txtPescadorId.getText().toString(),
                                txtUsuarioId.getText().toString(),
                                txtPermisoNumero.getText().toString(),
                                txtPermisoNombreEmbarcacion.getText().toString(),
                                txtPermisoRnpaEmbarcacion.getText().toString(),
                                txtPermisoMatricula.getText().toString(),
                                txtPermisoNoEmbarcaciones.getText().toString(),
                                txtPermisoSitioDesembarque.getText().toString(),
                                txtPermisoSitioDesembarqueClave.getText().toString(),
                                txtPermisoZonaPesca.getText().toString(),
                                "1",
                                //txtPermisoIdLocal,
                                //txtPermisoFhSincronizacion,
                                txtPermisoTonelaje.getText().toString(),
                                txtPermisoMarcaMotor.getText().toString(),
                                txtPermisoPotenciaHp.getText().toString(),
                                txtPermisoFhVigenciaInicio.getText().toString(),
                                txtPermisoFhVigenciaFin.getText().toString(),
                                "" + Util.calcularDuracion(Util.convertStringToDate(txtPermisoFhVigenciaInicio.getText().toString(),"/"),Util.convertStringToDate(txtPermisoFhVigenciaFin.getText().toString(),"/")),
                                txtPermisoFhExpedicion.getText().toString(),
                                txtPermisoLugarExpedicion.getText().toString(),
                                txtPermisoParaPesqueria.getText().toString(),
                                txtPermisoArteCantidad.getText().toString(),
                                txtPermisoArteCaracteristica.getText().toString(),
                                txtPermisoArtepesca.getText().toString(),
                                txtPermisoTitular.getText().toString(),
                                txtMunicipio.getText().toString(),
                                txtMunicipioId.getText().toString(),
                                txtEstado.getText().toString(),
                                txtEstadoId.getText().toString(),
                                //txtPermisoEstatusSinc,
                                usuarioLogin
                        );
                        perCall.enqueue(new Callback<PermisoList>() {

                            @Override
                            public void onResponse(Call<PermisoList> call, Response<PermisoList> response) {
                                PermisoList resultado = response.body();
                                if (resultado.isSuccess()) {
                                    permiso.setId(resultado.getPermiso().getPermisoIdRemoto());
                                    permiso.setPermisoFhSincronizacion(new Date());
                                    permiso.setPermisoEstatusSinc(Constantes.ESTATUS_SINCRONIZADO);
                                    int permisoId = insertarPermiso(permiso);
                                    Toast.makeText(getContext(), "La información del permiso se guardo correctamente.", Toast.LENGTH_LONG).show();
                                    goToPermisoConsulta(permisoId);

                                } else {
                                    Toast.makeText(getActivity(), "Error: El permiso no se registró, intente nuevamente.", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PermisoList> call, Throwable t) {
                                Toast.makeText(getActivity(), "Error: No hay comunicación con el servidor en la nube.", Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                }
                else{
                    Toast.makeText(getContext(), "Error: guardar el permiso se requiere conexión a internet." , Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.d(TAG, "Terminando onViewCreated(View view, Bundle savedInstanceState)");
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
            Toast.makeText(this.getContext(), "Favor de ingresar el titular de permiso.", Toast.LENGTH_LONG).show();
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
            }
        return exito;
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

    private int insertarPermiso(PermisoEntity permiso){
        Log.d(TAG, "Iniciando insertarPermiso(PermisoEntity permiso)");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(permiso);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Inserte permiso ID >" +permiso.getId());
        Log.d(TAG, "Terminando insertarPermiso(PermisoEntity permiso)");
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
