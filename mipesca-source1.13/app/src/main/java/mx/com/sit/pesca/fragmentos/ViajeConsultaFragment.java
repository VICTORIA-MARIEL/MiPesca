package mx.com.sit.pesca.fragmentos;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.adapters.ViajeAdaptador;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class ViajeConsultaFragment extends Fragment{
    private static final String TAG = "ViajeConsultaFragment";
    private SharedPreferences prefs;
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private TextView lblSinRegistros;
    private MaskEditText txtFhInicio;
    private MaskEditText txtFhFin;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;

    private FloatingActionButton fab;
    private RealmResults<ViajeEntity> viajes;

    private RecyclerView mRecyclerView;
    private ViajeAdaptador mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    public ViajeConsultaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_viaje_consulta, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        lblSinRegistros = view.findViewById(R.id.lblSinRegistros);
        txtFhFin = view.findViewById(R.id.txtFhFin);
        txtFhInicio =view.findViewById(R.id.txtFhInicio);
        txtFhInicio.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        DatePickerDialog dialog = new DatePickerDialog(getContext(), mDateSetListener, year, month, day);
                        dialog.show();
                    }
                }

        );

        txtFhFin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH);
                        int year = cal.get(Calendar.YEAR);
                        DatePickerDialog dialog = new DatePickerDialog(getContext(), mDateSetListener2, year, month, day);
                        dialog.show();
                    }
                }
        );

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + (month+1) + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month+1) + "/" +  String.format("%04d", year);
                txtFhInicio.setText(date);
            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month+1) + "/" +  String.format("%04d", year);
                txtFhFin.setText(date);
            }
        };


        realm = Realm.getDefaultInstance();
        viajes = realm.where(ViajeEntity.class).equalTo("usuarioId",usuarioId).findAll();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());

        mAdapter = new ViajeAdaptador(viajes, R.layout.list_view_viaje_item, new ViajeAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(ViajeEntity viaje, int position) {
                //Toast.makeText(MainActivity.this, name + " - " + position, Toast.LENGTH_LONG).show();
                int viajeId = viajes.get(position).getId();
                ViajeConsultaIndFragment fragmentoNvo = new ViajeConsultaIndFragment();
                Bundle data = new Bundle();
                data.putInt("viajeId", viajeId);
                fragmentoNvo.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentoNvo).commit();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        fab = view.findViewById(R.id.fabAddViaje);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViajeFragment fragmentoNvo = new ViajeFragment();
                getActivity().setTitle(R.string.headerViaje);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentoNvo).commit();
            }
        });
        Log.d(TAG, "Terminando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Iniciando onViewCreated(View view, Bundle savedInstanceState)");
        /*Llenado de combos*/
        realm = Realm.getDefaultInstance();
        PescadorEntity pescadorEntity = realm.where(PescadorEntity.class).equalTo("id",pescadorId).findFirst();

        realm.close();
        txtFhInicio.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));
        txtFhFin.setText(Util.convertDateToString(new Date(),Constantes.formatoFecha));
        /* Acciones de focus*/
        txtFhInicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtFhInicio.setHint(R.string.lblConsultaViajeFechaInicio);
                } else {
                    txtFhInicio.setHint("");
                }
            }
        });

        txtFhFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtFhFin.setHint(R.string.lblConsultaViajeFechaFinal);
                } else {
                    txtFhFin.setHint("");
                }
            }
        });

        /*Acciones de los botones*/
        Button btnBuscar = view.findViewById(R.id.btnConsultaViajeGenerar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    buscarViajes(view);
                    if(mAdapter.getItemCount() == 0){
                        lblSinRegistros.setVisibility(View.VISIBLE);
                    }
                    else{
                        lblSinRegistros.setVisibility(View.GONE);
                    }
                }
            }
        });


        Button btnLimpiar = view.findViewById(R.id.btnConsultaViajeLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFhInicio.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));
                txtFhFin.setText(Util.convertDateToString(new Date(),Constantes.formatoFecha));
                lblSinRegistros.setVisibility(View.GONE);
                realm = Realm.getDefaultInstance();
                viajes = realm.where(ViajeEntity.class)
                        .equalTo("usuarioId",0)
                        .findAll();
                mAdapter.updateList(viajes);
                realm.close();
            }
        });


        Log.d(TAG, "Terminando onViewCreated(View view, Bundle savedInstanceState)");
    }

    private void buscarViajes(View view){
        Date dFechaInicio = Util.convertStringToDate(txtFhInicio.getText().toString(),"00:00:00","/");
        Date dFechaFin = Util.convertStringToDate(txtFhFin.getText().toString(),"23:59:59","/");
        realm = Realm.getDefaultInstance();
        viajes = realm.where(ViajeEntity.class)
                .equalTo("usuarioId",usuarioId)
                .between("viajeFhRegistro",dFechaInicio,dFechaFin)
                .findAll();
        mAdapter.updateList(viajes);
        realm.close();
        Log.d(TAG,"viajes>"+viajes);
    }

    private boolean validarCampos(){
        Log.d(TAG, "Iniciando validarCampos()");
        boolean exito = true;
        if(exito && Util.convertStringToDate(txtFhInicio.getText().toString(), "/")==null){
            Toast.makeText(this.getContext(), "Favor de ingresar una fecha de inicio.", Toast.LENGTH_LONG).show();
            exito = false;
            txtFhInicio.setText("");
            txtFhInicio.requestFocus();
        }
        if(exito && Util.convertStringToDate(txtFhFin.getText().toString(), "/")==null){
            Toast.makeText(this.getContext(), "Favor de ingresar la fecha final.", Toast.LENGTH_LONG).show();
            exito = false;
            txtFhFin.setText("");
            txtFhFin.requestFocus();
        }

        Log.d(TAG, "Terminando validarCampos()");
        return exito;
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
