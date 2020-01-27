package mx.com.sit.pesca.fragmentos;


import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.activity.MainActivity;
import mx.com.sit.pesca.dao.ArtePescaDAO;
import mx.com.sit.pesca.dao.AvisoDAO;
import mx.com.sit.pesca.dao.AvisoViajeDAO;
import mx.com.sit.pesca.dao.ComunidadDAO;
import mx.com.sit.pesca.dao.CooperativaDAO;
import mx.com.sit.pesca.dao.EdoPesqueriaRecursoDAO;
import mx.com.sit.pesca.dao.MunicipioDAO;
import mx.com.sit.pesca.dao.OfnaPescaDAO;
import mx.com.sit.pesca.dao.PermisoDAO;
import mx.com.sit.pesca.dao.PresentacionDAO;
import mx.com.sit.pesca.dao.RecursoDAO;
import mx.com.sit.pesca.dao.RegionDAO;
import mx.com.sit.pesca.dao.SincronizacionDAO;
import mx.com.sit.pesca.dao.SitioDAO;
import mx.com.sit.pesca.dao.ViajeDAO;
import mx.com.sit.pesca.dao.ViajeRecursoDAO;
import mx.com.sit.pesca.entity.SincronizacionEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;


public class SincronizacionFragment extends Fragment {
    private static final String TAG = "SincronizacionFragment";
    private SharedPreferences prefs;
    private Realm realm;

    private TextView txtSincronizacionId;
    private TextView txtSincronizacionJobEstatus;
    private EditText txtHora;
    private ImageView iconHora;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private Button btnSincronizarAhora;

    private int usuarioId;
    private int pescadorId;
    private short txtLunes = (short)0;
    private short txtMartes =  (short)0;
    private short txtMiercoles =  (short)0;
    private short txtJueves =  (short)0;
    private short txtViernes =  (short)0;
    private short txtSabado =  (short)0;
    private short txtDomingo =  (short)0;

    private final Calendar c = Calendar.getInstance();
    private final int hora = c.get(Calendar.HOUR_OF_DAY);
    private final int minuto = c.get(Calendar.MINUTE);
    private SincronizacionDAO dao;

    public SincronizacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_sincronizacion, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        dao = new SincronizacionDAO(usuarioId, this.getContext());
        Log.d(TAG, "Terminando onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtSincronizacionId = view.findViewById(R.id.txtSincronizacionId);
        txtSincronizacionJobEstatus = view.findViewById(R.id.txtSincronizacionJobEstatus);
        txtSincronizacionId.setText("0");
        txtSincronizacionJobEstatus.setText("0");
        txtHora = view.findViewById(R.id.txtHora);
        iconHora = view.findViewById(R.id.iconHora);
        final  Switch selectLunes = view.findViewById(R.id.selectLunes);
        selectLunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtLunes = selectLunes.isChecked() ? (short)1 : (short)0;
            }
        });

        final  Switch selectMartes = view.findViewById(R.id.selectMartes);
        selectMartes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMartes = selectMartes.isChecked() ? (short)1 : (short)0;
            }
        });

        final  Switch selectMiercoles = view.findViewById(R.id.selectMiercoles);
        selectMiercoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMiercoles = selectMiercoles.isChecked() ? (short)1 : (short)0;
            }
        });

        final  Switch selectJueves = view.findViewById(R.id.selectJueves);
        selectJueves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtJueves = selectJueves.isChecked() ? (short)1 : (short)0;
            }
        });

        final  Switch selectViernes = view.findViewById(R.id.selectViernes);
        selectViernes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtViernes = selectViernes.isChecked() ? (short)1 : (short)0;
            }
        });

        final  Switch selectSabado = view.findViewById(R.id.selectSabado);
        selectSabado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSabado = selectSabado.isChecked() ? (short)1 : (short)0;
            }
        });

        final  Switch selectDomingo = view.findViewById(R.id.selectDomingo);
        selectDomingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDomingo = selectDomingo.isChecked() ? (short)1 : (short)0;
            }
        });


        Button btnSincronizarAhora = view.findViewById(R.id.btnSincronizarAhora);
        btnSincronizarAhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                    iniciarSincronizacion();
                }
                else{
                    Toast.makeText(getContext(), "Error: Para sincronizar requiere conexión a internet." , Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnSincronizacionGuardar = view.findViewById(R.id.btnSincronizacionGuardar);
        btnSincronizacionGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarSincronizacion(view);
            }
        });
        Button btnSincronizacionCancelar = view.findViewById(R.id.btnSincronizacionCancelar);
        btnSincronizacionCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelarSincronizacion(view);
            }
        });

        realm = Realm.getDefaultInstance();
        SincronizacionEntity dto = realm.where(SincronizacionEntity.class)
                .equalTo("pescadorId", pescadorId)
                .equalTo("sincronizacionUsrRegistro",usuarioId)
                .findFirst();
        realm.close();
        iconHora.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                                String horaFormateada =  (hourOfDay < 10)? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                                String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);
                                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                                String AM_PM;
                                if(hourOfDay < 12) {
                                    AM_PM = "a.m.";
                                } else {
                                    AM_PM = "p.m.";
                                }
                                //Muestro la hora con el formato deseado
                                txtHora.setText(horaFormateada + ":" + minutoFormateado);
                            }
                            //Estos valores deben ir en ese orden
                            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                            //Pero el sistema devuelve la hora en formato 24 horas
                        }, hora, minuto, true);

                        recogerHora.show();                    }
                }

        );

        if(dto!=null) {
            txtSincronizacionId.setText("" + dto.getId());
            txtSincronizacionJobEstatus.setText("" + dto.getSincronizacionJobEstatus());
            txtHora.setText(dto.getSincronizacionHora());
            selectLunes.setChecked((dto.getSincronizacionLunes() == 1) ? true : false);
            selectMartes.setChecked((dto.getSincronizacionMartes() == 1) ? true : false);
            selectMiercoles.setChecked((dto.getSincronizacionMiercoles() == 1) ? true : false);
            selectJueves.setChecked((dto.getSincronizacionJueves() == 1) ? true : false);
            selectViernes.setChecked((dto.getSincronizacionViernes() == 1) ? true : false);
            selectSabado.setChecked((dto.getSincronizacionSabado() == 1) ? true : false);
            selectDomingo.setChecked((dto.getSincronizacionDomingo() == 1) ? true : false);
            txtLunes = dto.getSincronizacionLunes();
            txtMartes = dto.getSincronizacionMartes();
            txtMiercoles = dto.getSincronizacionMiercoles();
            txtJueves = dto.getSincronizacionJueves();
            txtViernes = dto.getSincronizacionViernes();
            txtSabado = dto.getSincronizacionSabado();
            txtDomingo = dto.getSincronizacionDomingo();

            if(dto.getSincronizacionJobEstatus() == 1){
                btnSincronizacionCancelar.setVisibility(View.VISIBLE);
            }

        }

    }

    private void iniciarSincronizacion(){
        Log.d(TAG, "Iniciando iniciarSincronizacion");

        PresentacionDAO presDAO = new PresentacionDAO(usuarioId, this.getContext());
        presDAO.consultarPresentaciones();
        MunicipioDAO municipioDAO = new MunicipioDAO(usuarioId, this.getContext());
        municipioDAO.consultarMunicipios();
        ComunidadDAO comunidadDAO = new ComunidadDAO(usuarioId, this.getContext());
        comunidadDAO.consultarComunidades();
        CooperativaDAO cooperativaDAO = new CooperativaDAO(usuarioId, this.getContext());
        cooperativaDAO.consultarCooperativas();
        ArtePescaDAO artepescaDAO = new ArtePescaDAO(usuarioId, this.getContext());
        artepescaDAO.consultarArtesPesca();
        OfnaPescaDAO ofnapescaDAO = new OfnaPescaDAO(usuarioId, this.getContext());
        ofnapescaDAO.consultarOfnasPesca();
        RecursoDAO recursoDAO = new RecursoDAO(usuarioId, this.getContext());
        recursoDAO.consultarRecursos();
        EdoPesqueriaRecursoDAO pesqueriaRecursoDAO = new EdoPesqueriaRecursoDAO(usuarioId, this.getContext());
        pesqueriaRecursoDAO.consultarPesqueriaRecursos();
        RegionDAO regionDAO = new RegionDAO(usuarioId, this.getContext());
        regionDAO.consultarRegiones();
        SitioDAO sitioDAO = new SitioDAO(usuarioId, this.getContext());
        sitioDAO.consultarSitios();

        //PermisoDAO permisoDAO = new PermisoDAO(usuarioId, this.getContext());
        //permisoDAO.consultarPermisos();

        //sincronizarPermisos();

        ViajeDAO viajeDAO = new ViajeDAO(usuarioId, this.getContext());
        viajeDAO.sincronizarViajes();

        ViajeRecursoDAO vrDAO = new ViajeRecursoDAO(usuarioId, this.getContext());
        vrDAO.sincronizarViajesRecursos();
        AvisoDAO avisoDAO = new AvisoDAO(usuarioId, this.getContext());
        avisoDAO.sincronizarAvisos();
        AvisoViajeDAO avisoViajeDAO = new AvisoViajeDAO(usuarioId, this.getContext());
        avisoViajeDAO.sincronizarAvisosViajes();



        Toast.makeText(getContext(), "Sincronización realizada con éxito.", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Terminando iniciarSincronizacion");
    }


    private void cancelarSincronizacion(View view){
        Log.d(TAG, "Iniciando guardarSincronizacion()");
        try {
                if(Integer.parseInt(txtSincronizacionId.getText().toString()) > 0) {
                    SincronizacionEntity sincronizacion = new SincronizacionEntity();
                    sincronizacion.setId(Integer.parseInt(txtSincronizacionId.getText().toString()));
                    sincronizacion.setPescadorId(pescadorId);
                    sincronizacion.setSincronizacionHora("");
                    sincronizacion.setSincronizacionLunes((short)0);
                    sincronizacion.setSincronizacionMartes((short)0);
                    sincronizacion.setSincronizacionMiercoles((short)0);
                    sincronizacion.setSincronizacionJueves((short)0);
                    sincronizacion.setSincronizacionViernes((short)0);
                    sincronizacion.setSincronizacionSabado((short)0);
                    sincronizacion.setSincronizacionDomingo((short)0);
                    sincronizacion.setSincronizacionEstatus((short) 1);
                    sincronizacion.setSincronizacionFhRegistro(new Date());
                    sincronizacion.setSincronizacionUsrRegistro(usuarioId);
                    sincronizacion.setSincronizacionJobEstatus(Short.parseShort(txtSincronizacionJobEstatus.getText().toString()));
                    dao.insert(sincronizacion);
                    if(sincronizacion!=null && sincronizacion.getSincronizacionJobEstatus() == 1) {
                        ((MainActivity) getActivity()).cancelJob(view, usuarioId);
                    }
                    Toast.makeText(this.getContext(), "Sincronización cancelada.", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Inserté sincronizacion con ID=>" + sincronizacion.getId());
                }
        }
        catch(NumberFormatException nfe){
            Log.e(TAG, nfe.getMessage());
        }
        catch(Exception e){
            Toast.makeText(this.getContext(),"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());

        }
        Log.d(TAG, "Terminando cancelarSincronizacion()");
    }

    public void guardarSincronizacion(View view){
        Log.d(TAG, "Iniciando guardarSincronizacion()");
        try {
            String textSelectHoras = txtHora.getText().toString();

            if(txtLunes == 0 && txtMartes == 0 && txtMiercoles == 0 && txtJueves == 0
                    && txtViernes == 0 && txtSabado == 0 && txtDomingo == 0){
                Toast.makeText(this.getContext(), "Seleccione al menos un día de la semana.", Toast.LENGTH_LONG).show();
            }
            else {
                SincronizacionEntity sincronizacion = null;

                if(Integer.parseInt(txtSincronizacionId.getText().toString()) > 0) {
                    //Edicion de Registro
                    sincronizacion = new SincronizacionEntity();
                    sincronizacion.setId(Integer.parseInt(txtSincronizacionId.getText().toString()));
                    sincronizacion.setPescadorId(pescadorId);
                    sincronizacion.setSincronizacionHora(textSelectHoras);
                    sincronizacion.setSincronizacionLunes(txtLunes);
                    sincronizacion.setSincronizacionMartes(txtMartes);
                    sincronizacion.setSincronizacionMiercoles(txtMiercoles);
                    sincronizacion.setSincronizacionJueves(txtJueves);
                    sincronizacion.setSincronizacionViernes(txtViernes);
                    sincronizacion.setSincronizacionSabado(txtSabado);
                    sincronizacion.setSincronizacionDomingo(txtDomingo);
                    sincronizacion.setSincronizacionEstatus((short) 1);
                    sincronizacion.setSincronizacionFhRegistro(new Date());
                    sincronizacion.setSincronizacionUsrRegistro(usuarioId);
                    sincronizacion.setSincronizacionJobEstatus(Short.parseShort(txtSincronizacionJobEstatus.getText().toString()));
                }
                else{
                    //NUevo Registro
                    sincronizacion = new SincronizacionEntity(
                            pescadorId,
                            textSelectHoras,
                            txtLunes,
                            txtMartes,
                            txtMiercoles,
                            txtJueves,
                            txtViernes,
                            txtSabado,
                            txtDomingo,
                            usuarioId
                    );
                    sincronizacion.setSincronizacionJobEstatus((short) Constantes.JOB_INICIADO);

                }

                dao.insert(sincronizacion);
                //((MainActivity)getActivity()).cancelJob(view,123);
                if(sincronizacion!=null && sincronizacion.getSincronizacionJobEstatus() == 1) {
                    ((MainActivity) getActivity()).scheduleJob(view, usuarioId);
                }
                Log.e(TAG, "Inserté sincronizacion con ID=>" + sincronizacion.getId());
                Toast.makeText(this.getContext(), "Sincronización guardada con éxito", Toast.LENGTH_LONG).show();
            }
        }
        catch(NumberFormatException nfe){
            Log.e(TAG, nfe.getMessage());
        }
        catch(Exception e){
            Toast.makeText(this.getContext(),"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());

        }
        Log.d(TAG, "Terminando guardarSincronizacion()");
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


