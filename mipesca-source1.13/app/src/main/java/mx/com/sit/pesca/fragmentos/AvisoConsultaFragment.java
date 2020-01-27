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
import mx.com.sit.pesca.adapters.AvisoAdaptador;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class AvisoConsultaFragment extends Fragment {

    private static final String TAG = "AvisoConsultaFragment";
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
    private RealmResults<AvisoEntity> avisos;


    private RecyclerView mRecyclerView;
    private AvisoAdaptador mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    public AvisoConsultaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_aviso_consulta, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        realm = Realm.getDefaultInstance();
        lblSinRegistros = view.findViewById(R.id.lblSinRegistros);
        txtFhFin = view.findViewById(R.id.txtFhFin);
        txtFhInicio =view.findViewById(R.id.txtFhInicio);
        txtFhInicio.setOnClickListener(
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

        txtFhFin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;
                        int year = cal.get(Calendar.YEAR);
                        DatePickerDialog dialog = new DatePickerDialog(getContext(), mDateSetListener2, year, month, day);
                        dialog.show();
                    }
                }
        );

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month) + "/" +  String.format("%04d", year);
                txtFhInicio.setText(date);
            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + month + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month) + "/" +  String.format("%04d", year);
                txtFhFin.setText(date);
            }
        };


        avisos = realm.where(AvisoEntity.class).equalTo("usuarioId",usuarioId).findAll();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listViewAviso);
        mLayoutManager = new LinearLayoutManager(getContext());

        mAdapter = new AvisoAdaptador(avisos, R.layout.list_view_aviso_item, new AvisoAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(AvisoEntity aviso, int position) {
                int avisoId = avisos.get(position).getId();
                AvisoPDFFragment fragmentoNvo = new AvisoPDFFragment();
                //AvisoViajeFragment fragmentoNvo = new AvisoViajeFragment();
                Bundle data = new Bundle();
                data.putInt("avisoId", avisoId);
                fragmentoNvo.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentoNvo).commit();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        fab = view.findViewById(R.id.fabAddAviso);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AvisoFragment fragmentoNvo = new AvisoFragment();
                getActivity().setTitle(R.string.headerAvisoArribo);

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
                    txtFhInicio.setHint(R.string.lblConsultaAvisoFechaInicio);
                } else {
                    txtFhInicio.setHint("");
                }
            }
        });

        txtFhFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtFhFin.setHint(R.string.lblConsultaAvisoFechaFinal);
                } else {
                    txtFhFin.setHint("");
                }
            }
        });



        /*Acciones de los botones*/

        Button btnBuscar = view.findViewById(R.id.btnConsultaAvisoGenerar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    buscarAvisos(view);
                    if(mAdapter.getItemCount() == 0){
                        lblSinRegistros.setVisibility(View.VISIBLE);
                    }
                    else{
                        lblSinRegistros.setVisibility(View.GONE);
                    }
                }
            }
        });


        Button btnLimpiar = view.findViewById(R.id.btnConsultaAvisoLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFhInicio.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));
                txtFhFin.setText(Util.convertDateToString(new Date(),Constantes.formatoFecha));
                lblSinRegistros.setVisibility(View.GONE);
                realm = Realm.getDefaultInstance();
                avisos = realm.where(AvisoEntity.class)
                        .equalTo("usuarioId",0)
                        .findAll();
                mAdapter.updateList(avisos);
                realm.close();
            }
        });


        Log.d(TAG, "Terminando onViewCreated(View view, Bundle savedInstanceState)");
    }

    private void buscarAvisos(View view){
        Date dFechaInicio = Util.convertStringToDate(txtFhInicio.getText().toString(),"00:00:00","/");
        Date dFechaFin = Util.convertStringToDate(txtFhFin.getText().toString(),"23:59:59","/");
        realm = Realm.getDefaultInstance();
        avisos = realm.where(AvisoEntity.class)
                .equalTo("usuarioId",usuarioId)
                .between("avisoFhRegistro",dFechaInicio,dFechaFin)
                .findAll();
        mAdapter.updateList(avisos);
        realm.close();
        Log.d(TAG,"avisos>"+avisos);
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
