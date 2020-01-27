package mx.com.sit.pesca.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.adapters.ViajeRecursoAdaptador;
import mx.com.sit.pesca.entity.EdoPesqueriaRecursoEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;
import mx.com.sit.pesca.entity.PresentacionEntity;
import mx.com.sit.pesca.entity.RecursoEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class ViajeComplementoFragment extends Fragment  implements RealmChangeListener<RealmResults<ViajeEntity>>{
    private static final String TAG = "ViajeComplementFragment";
    private SharedPreferences prefs;
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private int viajeId;
    private String comunidadDesc;
    private String artepescaDesc;
    private boolean finalizado;

    private ViajeRecursoEntity viajeRecurso;
    private ViajeEntity viaje;
    private TextView txtViajeFhRegistro;
    private EditText txtCaptura;
    private EditText txtPrecio;
    private EditText txtNoPiezas;
    private EditText txtCombustible;

    private TextView txtComunidad;
    private AutoCompleteTextView txtRecurso;
    private TextView txtArtePesca;
    private AutoCompleteTextView txtPresentacion;

    private TextView txtComunidadId;
    private TextView txtRecursoId;
    private TextView txtArtePescaId;
    private TextView txtPresentacionId;

    private TextView txtUsuarioId;
    private TextView txtPescadorId;
    private TextView txtViajeId;

    private LinearLayout divComplemento;
    private LinearLayout divPregunta;
    private String[] listaRecursos;
    private String[] listaPresentaciones;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextInputLayout ilViajeNoPiezas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_viaje_complemento, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Bundle data = this.getArguments();
        if(data != null) {
            viajeId = data.getInt("viajeId",0);
            comunidadDesc = data.getString("comunidadDesc");
            artepescaDesc = data.getString("artepescaDesc");
            finalizado = data.getBoolean("finalizado");
        }
        setCredentialsIfExists();
        Log.d(TAG, "Terminando onCreateView");
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Iniciando onViewCreated(View view, Bundle savedInstanceState)");
        bindUi(view);

        final ImageView iconViajePresentacion = (ImageView)view.findViewById(R.id.iconViajePresentacion);
        final ImageView iconViajeRecurso = (ImageView)view.findViewById(R.id.iconViajeRecurso);

        /*Parametros iniciales para os campos de texto*/
        txtComunidadId.setText("0");
        txtRecursoId.setText("0");
        txtPresentacionId.setText("0");
        txtArtePescaId.setText("0");
        txtUsuarioId.setText("" + usuarioId);
        txtPescadorId.setText("" + pescadorId);
        txtViajeId.setText("" + viajeId );
        txtComunidad.setText( "" + comunidadDesc);
        txtArtePesca.setText( "" + artepescaDesc);
//        txtViajeFhRegistro.setText("Fecha: " + Util.convertDateToString(new Date(), Constantes.formatoFecha));

        //Obtener informacion del viaje anterior
        realm = Realm.getDefaultInstance();
        this.viaje = realm.where(ViajeEntity.class).equalTo("id",viajeId).findFirst();
        final RealmResults<ViajeRecursoEntity> viajeRecursos = realm.where(ViajeRecursoEntity.class).equalTo("viajeId",viajeId).findAll();
        if(this.viaje!=null) {
            txtComunidadId.setText("" + this.viaje.getComunidadId());
            txtArtePescaId.setText("" + this.viaje.getArtepescaId());
            txtCombustible.setText("" + this.viaje.getCombustible());
            txtViajeFhRegistro.setText( Util.convertDateToString(this.viaje.getViajeFhRegistro(), Constantes.formatoFecha));
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listViewViajeRecurso);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ViajeRecursoAdaptador(viajeRecursos, R.layout.list_view_viaje_recurso_item, new ViajeRecursoAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(ViajeRecursoEntity viajeRecurso, int position) {
                int viajeRecursoId = viajeRecursos.get(position).getId();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        /*Llenado de combos*/
        MunicipioEntity municipioDTO = realm.where(MunicipioEntity.class).equalTo("municipioId",viaje.getMunicipioId()).findFirst();
        RealmResults<EdoPesqueriaRecursoEntity> result = realm.where(EdoPesqueriaRecursoEntity.class).equalTo("estadoId",municipioDTO.getEstadoId()).findAll();
        ArrayList<String> findStringList = new ArrayList<String>();
        for (EdoPesqueriaRecursoEntity recurso : result) {
            findStringList.add(recurso.getRecursoDescripcion());
        }
        listaRecursos = Arrays.copyOf(findStringList.toArray(), findStringList.toArray().length,String[].class);
        final ArrayAdapter<String> adapterRecurso = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaRecursos);
        txtRecurso.setAdapter(adapterRecurso);
        adapterRecurso.notifyDataSetChanged();

        listaPresentaciones = new String[] {};
        final ArrayAdapter<String> adapterPresentacion = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaPresentaciones);
        txtPresentacion.setAdapter(adapterPresentacion);
        adapterPresentacion.notifyDataSetChanged();
        txtPresentacion.setEnabled(false);

        /*Acciones de ItemOnClickListener*/
        txtRecurso.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Recurso>" + itemSelected);
                RecursoEntity recursoDTO = realm.where(RecursoEntity.class).equalTo("recursoDescripcion",itemSelected).findFirst();
                Log.d(TAG,"Recurso Id>" + recursoDTO.getRecursoId());
                txtRecursoId.setText("" + recursoDTO.getRecursoId());

                RealmResults<PresentacionEntity> result3 = realm.where(PresentacionEntity.class).equalTo("recursoId",recursoDTO.getRecursoId()).findAll();
//                RealmResults<PresentacionEntity> result3 = realm.where(PresentacionEntity.class).findAll();
                ArrayList<String> findStringList3 = new ArrayList<String>();
                for (PresentacionEntity presentacion: result3) {
                    Log.d(TAG,"Presentacion ID > " + presentacion.getPresentacionId()+ " Presentacion Desc > " + presentacion.getPresentacionDescripcion()+  " Recurso Id>" + presentacion.getRecursoId());
                    findStringList3.add(presentacion.getPresentacionDescripcion());
                }
                listaPresentaciones = Arrays.copyOf(findStringList3.toArray(), findStringList3.toArray().length,String[].class);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, listaPresentaciones);
                txtPresentacion.setAdapter(adapter3);
                txtPresentacion.setEnabled(true);
//                realm.close();

                if(itemSelected.indexOf(Constantes.RECURSO_NO_PIEZAS) >= 0){
                    ilViajeNoPiezas.setVisibility(View.VISIBLE);
                }
                else{
                    ilViajeNoPiezas.setVisibility(View.GONE);
                    txtNoPiezas.setText("0");
                }

            }
        });

        txtPresentacion.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Presentacion>" + itemSelected);
                PresentacionEntity presentacionDTO = realm.where(PresentacionEntity.class).equalTo("presentacionDescripcion",itemSelected).findFirst();

                txtPresentacionId.setText("" + presentacionDTO.getPresentacionId());
//                realm.close();
            }
        });

        /*Acciones de los iconos drop down list*/
        iconViajePresentacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPresentacion.showDropDown();
            }
        });
        iconViajeRecurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRecurso.showDropDown();
            }
        });

        /*Acciones de etiquetas*/

        txtRecurso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtRecurso.setHint(R.string.lblViajeRecurso);
                } else {
                    txtRecurso.setHint("");
                }
            }
        });

        txtCaptura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtCaptura.setHint(R.string.lblViajeCaptura);
                } else {
                    txtCaptura.setHint("");
                }
            }
        });
        txtPrecio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtPrecio.setHint(R.string.lblViajePrecio);
                } else {
                    txtPrecio.setHint("");
                }
            }
        });
        txtNoPiezas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtNoPiezas.setHint(R.string.lblViajeNoPiezas);
                } else {
                    txtNoPiezas.setHint("");
                }
            }
        });

        txtPresentacion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtPresentacion.setHint(R.string.lblViajePresentacion);
                } else {
                    txtPresentacion.setHint("");
                }
            }
        });

        /*Acciones de los botones*/
        Button btnRegistrar = view.findViewById(R.id.btnRegistrarViaje);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    insertarViajeComplemento(viaje,viajeRecurso);
                    Toast.makeText(getContext(), "La información del viaje se guardo correctamente.", Toast.LENGTH_LONG).show();
                    goToComplementoViaje(viajeId,comunidadDesc,artepescaDesc);
                }
            }
        });

        Button btnViajeNoContinuar = view.findViewById(R.id.btnViajeNoContinuar);
        btnViajeNoContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                divPregunta.setVisibility(View.VISIBLE);
                divComplemento.setVisibility(View.GONE);
//                realm = Realm.getDefaultInstance();
                viaje = realm.where(ViajeEntity.class).equalTo("id",viajeId).findFirst();



                ViajeEntity viajeTemp = new ViajeEntity();
                viajeTemp.setId(viaje.getId());
                viajeTemp.setViajeFhRegistro(viaje.getViajeFhRegistro());
                viajeTemp.setViajeFhFinalizo(new Date());
                viajeTemp.setMunicipioId(viaje.getMunicipioId());
                viajeTemp.setComunidadId(viaje.getComunidadId());
                viajeTemp.setArtepescaId(viaje.getArtepescaId());
                viajeTemp.setCombustible(viaje.getCombustible());
                viajeTemp.setUsuarioId(viaje.getUsuarioId());
                viajeTemp.setPescadorId(viaje.getPescadorId());
                viajeTemp.setViajeIdLocal(viaje.getViajeIdLocal());
                viajeTemp.setViajeFhSincronizacion(viaje.getViajeFhSincronizacion());
                viajeTemp.setViajeEstatus(Constantes.VIAJE_ESTATUS_FINALIZADO);
                viajeTemp.setViajeEstatusSinc(viaje.getViajeEstatusSinc());
                viajeTemp.setViajesRecursos(viaje.getViajesRecursos());
                viajeTemp.setArtepescaDescripcion(viaje.getArtepescaDescripcion());
                viajeTemp.setComunidadDescripcion(viaje.getComunidadDescripcion());
                viajeTemp.setMunicipioDescripcion(viaje.getMunicipioDescripcion());
//                realm.close();
                finalizarViaje(viajeTemp);
                String folio = Util.generarFolio(pescadorId,viajeTemp.getId()  );
                Toast.makeText(getContext(), "El viaje se finalizó con el folio número " + folio +".", Toast.LENGTH_LONG).show();
                goToViaje(viajeTemp.getId());
            }
        });


        Button btnContinuar = view.findViewById(R.id.btnViajeContinuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    divPregunta.setVisibility(View.GONE);
                    divComplemento.setVisibility(View.VISIBLE);
            }
        });

        Button btnRegresar = view.findViewById(R.id.btnRegresarViaje);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                divPregunta.setVisibility(View.VISIBLE);
                divComplemento.setVisibility(View.GONE);
            }
        });

        if(!finalizado){
            divPregunta.setVisibility(View.GONE);
            divComplemento.setVisibility(View.VISIBLE);
        }

//        realm.close();

        Log.d(TAG, "Terminando onViewCreated(View view, Bundle savedInstanceState)");
    }

    private void bindUi(View view){
        Log.d(TAG, "Iniciando bindUi( )");
        divComplemento = view.findViewById(R.id.divComplemento);

        txtViajeFhRegistro = view.findViewById(R.id.txtViajeFhRegistro);
        txtCaptura = view.findViewById(R.id.txtViajeCaptura);
        txtPrecio = view.findViewById(R.id.txtViajePrecio);
        txtNoPiezas = view.findViewById(R.id.txtViajeNoPiezas);
        txtCombustible = view.findViewById(R.id.txtViajeCombustible);

        txtComunidad = view.findViewById(R.id.txtViajeComunidad);
        txtRecurso = view.findViewById(R.id.txtViajeRecurso);
        txtArtePesca = view.findViewById(R.id.txtViajeArtePesca);
        txtPresentacion = view.findViewById(R.id.txtViajePresentacion);

        txtComunidadId = view.findViewById(R.id.txtViajeComunidadId);
        txtRecursoId = view.findViewById(R.id.txtViajeRecursoId);
        txtArtePescaId = view.findViewById(R.id.txtViajeArtePescaId);
        txtPresentacionId = view.findViewById(R.id.txtViajePresentacionId);

        txtUsuarioId = view.findViewById(R.id.txtViajeUsuarioId);
        txtPescadorId = view.findViewById(R.id.txtViajePescadorId);
        txtViajeId = view.findViewById(R.id.txtViajeId);

        divComplemento = view.findViewById(R.id.divComplemento);
        divPregunta = view.findViewById(R.id.divPregunta);

        ilViajeNoPiezas = view.findViewById(R.id.ilViajeNoPiezas);


        Log.d(TAG, "Terminando bindUi( )");

    }

    private void finalizarViaje(ViajeEntity viajeTemp){
        Log.d(TAG, "Iniciando finalizarViaje( ViajeEntity viajeTemp)");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(viajeTemp);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Finalicé viaje con ID >" +viajeTemp.getId());
        Log.d(TAG, "Terminando finalizarViaje(ViajeEntity viajeTemp)");
    }


    private void insertarViajeComplemento(ViajeEntity viaje, ViajeRecursoEntity viajeRecurso){
        Log.d(TAG, "Iniciando insertarViajeComplemento( ViajeRecursoEntity viajeRecurso)");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(viajeRecurso);
        this.viaje.getViajesRecursos().add(viajeRecurso);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Inserte viaje recurso con ID >" +viajeRecurso.getId());
        Log.d(TAG, "Terminando insertarViajeComplemento(ViajeRecursoEntity viajeRecurso)");


    }

    private boolean validarCampos(){
        Log.d(TAG, "Iniciando validarCampos()");
        boolean exito = true;

        if(viajeId == 0){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
        }
        if(usuarioId == 0){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
        }
        if(exito && pescadorId == 0){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
        }

        if(exito && Integer.parseInt(txtRecursoId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar un recurso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtRecurso.requestFocus();
        }

        //txtViajeFhRegistro;

        try {
            if(exito && Integer.parseInt(txtCaptura.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
                exito = false;
                txtCaptura.setText("");
                txtCaptura.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
            txtCaptura.setText("");
            txtCaptura.requestFocus();
        }

        try {
            if(exito && Integer.parseInt(txtPrecio.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
                exito = false;
                txtPrecio.setText("");
                txtPrecio.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
            txtPrecio.setText("");
            txtPrecio.requestFocus();
        }

        try {
            if(exito && Integer.parseInt(txtNoPiezas.getText().toString()) < 0){
                Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
                exito = false;
                txtNoPiezas.setText("");
                txtNoPiezas.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
            txtNoPiezas.setText("");
            txtNoPiezas.requestFocus();
        }


        try {
            if(exito && Integer.parseInt(txtCombustible.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
                exito = false;
                txtCombustible.setText("");
                txtCombustible.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad correcta.", Toast.LENGTH_LONG).show();
            txtCombustible.setText("");
            txtCombustible.requestFocus();
        }


        if(exito && Integer.parseInt(txtComunidadId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar una comunidad.", Toast.LENGTH_LONG).show();
            exito = false;
            txtComunidad.requestFocus();
        }



        if(exito && Integer.parseInt(txtArtePescaId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar el arte de pesca.", Toast.LENGTH_LONG).show();
            exito = false;
            txtArtePesca.requestFocus();
        }

        if(exito && Integer.parseInt(txtPresentacionId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar la presentación.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPresentacion.requestFocus();
        }
        if(exito) {
            viajeRecurso = new ViajeRecursoEntity(
                    Integer.parseInt(txtRecursoId.getText().toString()),
                    Integer.parseInt(txtPresentacionId.getText().toString()),
                    Constantes.ES_PRINCIPAL,
                    Integer.parseInt(txtCaptura.getText().toString()),
                    Integer.parseInt(txtPrecio.getText().toString()),
                    Integer.parseInt(txtNoPiezas.getText().toString()),
                    (short) 1,
                    usuarioId,
                    txtRecurso.getText().toString(),
                    txtPresentacion.getText().toString(),
                    viajeId

            );
            viajeRecurso.setViajeId(viajeId);
        }

        Log.d(TAG, "Terminando validarCampos()");
        return exito;
    }

    private void goToViaje(int viajeId){
        Log.d(TAG, "Iniciando goToViaje()");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ViajeFragment()).commit();
        Log.d(TAG, "Terminando goToViaje()");

    }


    private void goToComplementoViaje(int viajeId, String comunidadDesc, String artepescaDesc){
        Log.d(TAG, "Iniciando goToComplementoViaje(int viajeId,String comunidadDesc, String artepescaDesc)");
        ViajeComplementoFragment fragmentoNvo = new ViajeComplementoFragment();
        Bundle data = new Bundle();
        data.putInt("viajeId", viajeId);
        data.putString("comunidadDesc", comunidadDesc);
        data.putString("artepescaDesc", artepescaDesc);
        fragmentoNvo.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentoNvo).commit();
        Log.d(TAG, "Terminando goToComplementoViaje(int viajeId, String comunidadDesc, String artepescaDesc)");

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


    @Override
    public void onChange(RealmResults<ViajeEntity> viajeEntities) {

    }
}
