package mx.com.sit.pesca.fragmentos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.LinearLayout;
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
import mx.com.sit.pesca.SplashActivity;
import mx.com.sit.pesca.dto.PermisoList;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.OfnaPescaEntity;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.SitioEntity;
import mx.com.sit.pesca.services.PermisoService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvisoFragment extends Fragment {
    private static final String TAG = "AvisoFragment";
    private SharedPreferences prefs;
    private Realm realm;
    private Retrofit retrofit;
    private int usuarioId;
    private int pescadorId;
    private AvisoEntity aviso;
    private AvisoViajeEntity avisoViaje;
    private String ofnapescaDesc;
    private String sitioDesc;
    private String permisoNumero;

    private TextView txtPescadorId;
    private TextView txtUsuarioId;
    private TextView txtPermisoId;
    private TextView txtOfnapescaId;
    private TextView txtSitioId;

    private TextView txtAvisoFhSolicitud;
    private MaskEditText txtAvisoPeriodoInicio;
    private MaskEditText txtAvisoPeriodoFin;
    private EditText txtAvisoDuracion;
    private EditText txtAvisoDiasEfectivos;
    private EditText txtAvisoZonaPesca;
    private AutoCompleteTextView txtPermiso;
    private AutoCompleteTextView txtOfnapesca;
    private AutoCompleteTextView txtSitio;
    private Switch   chkAvisoEsPesqueriaAcuacultural;
    private LinearLayout ilAvisoCampos;
    private Button    btnAvisoGenerar;
    private Button    btnAvisoLimpiar;
    private Button    btnValidarPermiso;

    private String[] listaOfnaspesca;
    private String[] listaSitios;
    private String[] listaPermisos;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_aviso, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.URL_SERVIDOR_REMOTO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /*Buscando avisos a medias*/
        int avisoId = 0;
        realm = Realm.getDefaultInstance();
        AvisoEntity aviso = realm.where(AvisoEntity.class).equalTo("avisoEstatus",Constantes.AVISO_ESTATUS_INICIAL).findFirst();
        if(aviso!=null) {
            avisoId = aviso.getId();
        }
        realm.close();
        if(avisoId > 0) {
            goToAvisoViaje(avisoId);
        }

        Log.d(TAG, "Terminando onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Iniciando onViewCreated(View view, Bundle savedInstanceState)");


        txtPescadorId = view.findViewById(R.id.txtPescadorId);
         txtUsuarioId = view.findViewById(R.id.txtUsuarioId);
        txtOfnapescaId = view.findViewById(R.id.txtOfnapescaId);
        txtPermisoId = view.findViewById(R.id.txtPermisoId);
        txtSitioId = view.findViewById(R.id.txtSitioId);
        txtAvisoFhSolicitud = view.findViewById(R.id.txtAvisoFhSolicitud);
        txtOfnapesca = view.findViewById(R.id.txtOfnapesca);
        txtSitio = view.findViewById(R.id.txtSitio);
        txtPermiso = view.findViewById(R.id.txtPermiso);
        txtAvisoPeriodoInicio = view.findViewById(R.id.txtAvisoPeriodoInicio);
        txtAvisoPeriodoFin = view.findViewById(R.id.txtAvisoPeriodoFin);
        txtAvisoDuracion = view.findViewById(R.id.txtAvisoDuracion);
        txtAvisoZonaPesca = view.findViewById(R.id.txtAvisoZonaPesca);
        chkAvisoEsPesqueriaAcuacultural = view.findViewById(R.id.chkAvisoEsPesqueriaAcuacultural);
        txtAvisoDiasEfectivos = view.findViewById(R.id.txtAvisoDiasEfectivos);

        final ImageView iconAvisoOfnaPesca = (ImageView)view.findViewById(R.id.iconAvisoOfnaPesca);
        final ImageView iconAvisoSitio = (ImageView)view.findViewById(R.id.iconAvisoSitio);
        final ImageView iconAvisoPermiso = (ImageView)view.findViewById(R.id.iconAvisoPermiso);
        ilAvisoCampos = view.findViewById(R.id.llAvisoCampos);
        /*Parametros iniciales para os campos de texto*/
        txtOfnapescaId.setText("0");
        txtSitioId.setText("0");
        txtPermisoId.setText("0");
        txtAvisoPeriodoInicio.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));
        txtAvisoPeriodoFin.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));

        txtUsuarioId.setText("" + usuarioId);
        txtPescadorId.setText("" + pescadorId);
        txtAvisoFhSolicitud.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));

        /*Llenado de combos*/
        realm = Realm.getDefaultInstance();
        RealmResults<OfnaPescaEntity> result = realm.where(OfnaPescaEntity.class).findAll();
        ArrayList<String> findStringList = new ArrayList<String>();
        for (OfnaPescaEntity ofnapesca : result) {
            findStringList.add(ofnapesca.getOfnapescaDescripcion());
        }
        listaOfnaspesca = Arrays.copyOf(findStringList.toArray(), findStringList.toArray().length,String[].class);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaOfnaspesca);
        txtOfnapesca.setAdapter(adapter1);

        RealmResults<SitioEntity> resSitio = realm.where(SitioEntity.class).findAll();
        ArrayList<String> findStringListSitio = new ArrayList<String>();
        for (SitioEntity sitio : resSitio) {
            findStringListSitio.add(sitio.getSitioDescripcion());
        }
        listaSitios = Arrays.copyOf(findStringListSitio.toArray(), findStringListSitio.toArray().length,String[].class);
        final ArrayAdapter<String> adapterSitio = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaSitios);
        txtSitio.setAdapter(adapterSitio);

        RealmResults<PermisoEntity> resPermiso = realm.where(PermisoEntity.class).findAll();
        ArrayList<String> findStringListPermiso = new ArrayList<String>();
        for (PermisoEntity permiso : resPermiso) {
            findStringListPermiso.add(permiso.getPermisoNumero());
        }
        listaPermisos = Arrays.copyOf(findStringListPermiso.toArray(), findStringListPermiso.toArray().length,String[].class);
        final ArrayAdapter<String> adapterPermiso = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaPermisos);
        txtPermiso.setAdapter(adapterPermiso);

        realm.close();
        /*Funciones de click para los calendarios*/
        txtAvisoPeriodoInicio.setOnClickListener(
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
        txtAvisoPeriodoFin.setOnClickListener(
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
                txtAvisoPeriodoInicio.setText(date);
            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month) + "/" +  String.format("%04d", year);
                txtAvisoPeriodoFin.setText(date);
            }
        };

        /*Acciones de ItemOnClickListener*/
        txtOfnapesca.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Ofna pesca>" + itemSelected);
                ofnapescaDesc = itemSelected;
                OfnaPescaEntity ofnapescaDTO = realm.where(OfnaPescaEntity.class).equalTo("ofnapescaDescripcion",itemSelected).findFirst();
                Log.d(TAG,"Ofnapesca Id>" + ofnapescaDTO.getOfnapescaId());
                txtOfnapescaId.setText("" + ofnapescaDTO.getOfnapescaId());

                realm.close();
            }
        });

        txtSitio.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Sitio>" + itemSelected);
                sitioDesc = itemSelected;
                SitioEntity sitioDTO = realm.where(SitioEntity.class).equalTo("sitioDescripcion",itemSelected).findFirst();
                Log.d(TAG,"Sitio Id>" + sitioDTO.getSitioId());
                txtSitioId.setText("" + sitioDTO.getSitioId());
                realm.close();
            }
        });

        txtPermiso.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Permiso>" + itemSelected);
                permisoNumero = itemSelected;
                PermisoEntity permisoDTO = realm.where(PermisoEntity.class).equalTo("permisoNumero",itemSelected).findFirst();
                Log.d(TAG,"Permiso Id>" + permisoDTO.getId());
                txtPermisoId.setText("" + permisoDTO.getId());
                realm.close();

                btnValidarPermiso.setVisibility(View.INVISIBLE);
                txtPermiso.setEnabled(false);
                ilAvisoCampos.setVisibility(View.VISIBLE);

            }
        });
        /*Validacion de campos de duracion*/
        txtAvisoDiasEfectivos.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                short duracion = Util.calcularDuracionDias(Util.convertStringToDate(txtAvisoPeriodoInicio.getText().toString(),"/"),Util.convertStringToDate(txtAvisoPeriodoFin.getText().toString(),"/"));
                //Log.d(TAG,"Duracion > " + duracion);
                if(!"".equals(s.toString())) {
                    if ((duracion + 1) < Short.parseShort(s.toString())) {
                        Toast.makeText(getContext(), "Favor de ingresar correctamente los días efectivos.", Toast.LENGTH_LONG).show();
                        txtAvisoDiasEfectivos.requestFocus();
                        txtAvisoDiasEfectivos.setText("" );
                    }
                }

            }

        });

        /*Acciones de los iconos drop down list*/

        iconAvisoOfnaPesca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtOfnapesca.showDropDown();
            }
        });

        iconAvisoPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPermiso.showDropDown();
            }
        });

        iconAvisoSitio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSitio.showDropDown();
            }
        });

        /*Acciones de los botones*/
        btnAvisoGenerar = view.findViewById(R.id.btnAvisoGenerar);
        btnAvisoGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    int avisoId = insertarAviso(aviso);
                    Toast.makeText(getContext(), "La información del aviso se guardo correctamente.", Toast.LENGTH_LONG).show();
                    goToAvisoViaje(avisoId);
                }
            }
        });

        btnAvisoLimpiar = view.findViewById(R.id.btnAvisoLimpiar);
        btnAvisoLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvisoFragment fragmentoNvo = new AvisoFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentoNvo).commit();

            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        btnValidarPermiso = view.findViewById(R.id.btnValidarPermiso);
        btnValidarPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String permiso = txtPermiso.getText().toString();
                if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                    if (permiso != null && !"".equals(permiso)) {
                        PermisoService service = retrofit.create(PermisoService.class);
                        Call<PermisoList> perCall = service.existePer(
                                permiso
                        );
                        perCall.enqueue(new Callback<PermisoList>() {
                            @Override
                            public void onResponse(Call<PermisoList> call, Response<PermisoList> response) {
                                PermisoList resultado = response.body();
                                Log.d(TAG, "resultado: " + resultado.getCodigo());
                                if (resultado.getCodigo() == 200 && resultado.getPermiso() != null) { //el usuario se encuentra en la bd remota
                                    txtPermisoId.setText(" " + resultado.getPermiso().getPermisoNumero());
                                    btnValidarPermiso.setVisibility(View.INVISIBLE);
                                    txtPermiso.setEnabled(false);
                                    ilAvisoCampos.setVisibility(View.VISIBLE);
                                } else {
                                    txtPermiso.setText("");
                                    txtPermisoId.setText("0");
                                    Toast.makeText(getContext(), "Error: No se encontro el permiso ingresado.", Toast.LENGTH_LONG).show();
                                    btnValidarPermiso.setVisibility(View.VISIBLE);
                                    txtPermiso.setEnabled(true);
                                    ilAvisoCampos.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<PermisoList> call, Throwable t) {
                                Toast.makeText(getContext(), "Error: Ocurrió un error al consultar el permiso.", Toast.LENGTH_LONG).show();
                                txtPermiso.setEnabled(true);
                                ilAvisoCampos.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Favor de ingresar el número de permiso.", Toast.LENGTH_LONG).show();
                        txtPermiso.requestFocus();
                    }

                }
                else{
                    Toast.makeText(getContext(), "Error: No se encuentra conectado a internet." , Toast.LENGTH_LONG).show();
                    txtPermiso.requestFocus();
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

        if(exito && Integer.parseInt(txtOfnapescaId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar una oficina de pesca.", Toast.LENGTH_LONG).show();
            exito = false;
            txtOfnapesca.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtAvisoPeriodoInicio.getText().toString()) && txtAvisoPeriodoInicio.getText().toString().length() <= 10){
            Toast.makeText(this.getContext(), "Favor de ingresar el periodo de inicio.", Toast.LENGTH_LONG).show();
            exito = false;
            txtAvisoPeriodoInicio.setText("");
            txtAvisoPeriodoInicio.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtAvisoPeriodoFin.getText().toString()) && txtAvisoPeriodoFin.getText().toString().length() <= 10){
            Toast.makeText(this.getContext(), "Favor de ingresar el periodo final.", Toast.LENGTH_LONG).show();
            exito = false;
            txtAvisoPeriodoFin.setText("");
            txtAvisoPeriodoFin.requestFocus();
        }

       /* try {
            if(exito && Integer.parseInt(txtAvisoDuracion.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar la duración.", Toast.LENGTH_LONG).show();
                exito = false;
                txtAvisoDuracion.setText("");
                txtAvisoDuracion.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
            txtAvisoDuracion.setText("");
            txtAvisoDuracion.requestFocus();
        }
*/

        try {


            if(exito && Integer.parseInt(txtAvisoDiasEfectivos.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar los días efectivos.", Toast.LENGTH_LONG).show();
                exito = false;
                txtAvisoDiasEfectivos.setText("");
                txtAvisoDiasEfectivos.requestFocus();
            }

            short duracion = Util.calcularDuracionDias(Util.convertStringToDate(txtAvisoPeriodoInicio.getText().toString(),"/"),Util.convertStringToDate(txtAvisoPeriodoFin.getText().toString(),"/"));
                if ((duracion + 1) < Short.parseShort(txtAvisoDiasEfectivos.getText().toString())) {
                    Toast.makeText(getContext(), "El valor de los días efectivos no puede ser mayor que la duración del viaje.", Toast.LENGTH_LONG).show();
                    txtAvisoDiasEfectivos.setText("");
                    txtAvisoDiasEfectivos.requestFocus();
                }

        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
            txtAvisoDiasEfectivos.setText("");
            txtAvisoDiasEfectivos.requestFocus();
        }

/*        if(exito && Integer.parseInt(txtPermiso.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de ingresar su permiso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPermiso.requestFocus();
        }
*/
        if(exito && Integer.parseInt(txtSitioId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar una sitio de captura.", Toast.LENGTH_LONG).show();
            exito = false;
            txtSitio.requestFocus();
        }

        if(exito && TextUtils.isEmpty(txtAvisoZonaPesca.getText().toString()) && txtAvisoZonaPesca.getText().toString().length() <= Constantes.SELECT_MIN_CARACTERES){
            Toast.makeText(this.getContext(), "Favor de ingresar la zona de pesca.", Toast.LENGTH_LONG).show();
            exito = false;
            txtAvisoZonaPesca.setText("");
            txtAvisoZonaPesca.requestFocus();
        }


        if(exito) {
            aviso = new AvisoEntity(
                    pescadorId,
                    usuarioId,
                    Util.convertStringToDate(txtAvisoFhSolicitud.getText().toString(),"/"),
                    Util.convertStringToDate(txtAvisoPeriodoInicio.getText().toString(),"/"),
                    Util.convertStringToDate(txtAvisoPeriodoFin.getText().toString(),"/"),
                    Util.calcularDuracionDias(Util.convertStringToDate(txtAvisoPeriodoInicio.getText().toString(),"/"),Util.convertStringToDate(txtAvisoPeriodoFin.getText().toString(),"/")),
                    Short.parseShort(txtAvisoDiasEfectivos.getText().toString()),
                    0,
                    txtAvisoZonaPesca.getText().toString(),
                    Integer.parseInt(txtOfnapescaId.getText().toString()),
                    Byte.parseByte((chkAvisoEsPesqueriaAcuacultural.isChecked())?"1":"0"),
                    Integer.parseInt(txtSitioId.getText().toString()),
                    sitioDesc,
                    ofnapescaDesc,
                    txtPermiso.getText().toString()
                     );



        }
        return exito;
    }

    private void goToAvisoViaje(int avisoId){
        Log.d(TAG, "Iniciando goToAvisoViaje(int avisoId)");

        AvisoViajeFragment fragmentoNvo = new AvisoViajeFragment();
        Bundle data = new Bundle();
        data.putInt("avisoId", avisoId);
        fragmentoNvo.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentoNvo).commit();

        Log.d(TAG, "Terminando goToAvisoViaje(int avisoId)");

    }

    private int insertarAviso(AvisoEntity aviso){
        Log.d(TAG, "Iniciando insertarAviso(AvisoEntity aviso)");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(aviso);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Inserte viaje ID >" +aviso.getId());
        Log.d(TAG, "Terminando insertarAviso(AvisoEntity aviso)");
        return aviso.getId();

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
