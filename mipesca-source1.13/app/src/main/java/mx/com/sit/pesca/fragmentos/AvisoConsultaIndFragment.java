package mx.com.sit.pesca.fragmentos;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.adapters.AvisoViajeAdaptador;
import mx.com.sit.pesca.adapters.ViajeRecursoAdaptador;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class AvisoConsultaIndFragment extends Fragment {
    private static final String TAG = "AvisoConsultaIndFragmen";
    private Realm realm;
    private int usuarioId;
    private int pescadorId;

    private AvisoEntity aviso;

    private int avisoId;
    private SharedPreferences prefs;
    private String ofnapescaDesc;
    private String sitioDesc;
    private String permisoNumero;
    private TextView txtAvisoFhRegistro;
    private TextView txtAvisoFhSolicitud;
    private TextView txtSitio;
    private TextView txtOfnaPesca;
    private TextView txtSitioId;
    private TextView txtOfnaPescaId;
    private TextView txtUsuarioId;
    private TextView txtPescadorId;
    private TextView txtAvisoId;
    private TextView txtAvisoPeriodoInicio;
    private TextView txtAvisoPeriodoFin;
    private TextView txtAvisoDuracion;
    private TextView txtAvisoDiasEfectivos;
    private TextView txtAvisoFolio;
    private TextView txtPermisoNumero;
    private TextView txtPermisoId;
    private TextView txtAvisoZonaPesca;
    private TextView txtAvisoEsPesqueriaAcuacultural;
    private LinearLayout divComplemento;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_aviso_consulta_ind, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        bindUi(view);
        Bundle data = this.getArguments();
        if (data != null) {
            avisoId = data.getInt("avisoId", 0);
        }
        //Obtener información del aviso.
        realm = Realm.getDefaultInstance();
        this.aviso = realm.where(AvisoEntity.class).equalTo("id",avisoId).findFirst();
        final RealmResults<AvisoViajeEntity> avisoViajes = realm.where(AvisoViajeEntity.class).equalTo("avisoId",avisoId).findAll();
        if(this.aviso!=null) {
            txtOfnaPescaId.setText("" + this.aviso.getOfnapescaId());
            txtSitioId.setText("" + this.aviso.getSitioId());
            txtPermisoId.setText("" + this.aviso.getPermisoId());
            txtAvisoFhRegistro.setText( Util.convertDateToString(this.aviso.getAvisoFhRegistro(), Constantes.formatoFecha));
            ofnapescaDesc = aviso.getOfnapescaDescripcion();
            sitioDesc = aviso.getSitioDescripcion();
            permisoNumero = aviso.getPermisoNumero();
        }
        realm.close();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listViewAvisoViaje);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AvisoViajeAdaptador(avisoViajes, R.layout.list_view_aviso_viaje_item, new AvisoViajeAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(AvisoViajeEntity avisoViaje, int position) {
                int avisoViajeId = avisoViajes.get(position).getId();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        llenarCampos();

        Button btnRegresar = view.findViewById(R.id.btnRegresarAviso);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToConsultaAviso(avisoId);
            }
        });

        Log.d(TAG, "Terminando onCreateView");
        return view;
    }

    private void bindUi(View view) {
        Log.d(TAG, "Iniciando bindUi( )");
        divComplemento = view.findViewById(R.id.divComplemento);
//          txtAvisoFhRegistro = view.findViewById(R.id.txtAvisoFhRegistro);
          txtAvisoFhSolicitud = view.findViewById(R.id.txtAvisoFhSolicitud);
          txtSitio = view.findViewById(R.id.txtSitio);
          txtOfnaPesca = view.findViewById(R.id.txtOfnaPesca);
          txtSitioId = view.findViewById(R.id.txtSitioId);
          txtOfnaPescaId = view.findViewById(R.id.txtOfnaPescaId);
          txtUsuarioId = view.findViewById(R.id.txtUsuarioId);
          txtPescadorId = view.findViewById(R.id.txtPescadorId);
          txtAvisoId = view.findViewById(R.id.txtAvisoId);
          txtAvisoPeriodoInicio = view.findViewById(R.id.txtAvisoPeriodoInicio);
          txtAvisoPeriodoFin = view.findViewById(R.id.txtAvisoPeriodoFin);
          txtAvisoDuracion = view.findViewById(R.id.txtAvisoDuracion);
          txtAvisoDiasEfectivos = view.findViewById(R.id.txtAvisoDiasEfectivos);
          txtAvisoFolio = view.findViewById(R.id.txtAvisoFolio);
          txtPermisoNumero = view.findViewById(R.id.txtPermisoNumero);
          txtPermisoId = view.findViewById(R.id.txtPermisoId);
          txtAvisoZonaPesca = view.findViewById(R.id.txtAvisoZonaPesca);
          txtAvisoEsPesqueriaAcuacultural = view.findViewById(R.id.chkAvisoEsPesqueriaAcuacultural);

        divComplemento = view.findViewById(R.id.divComplemento);
        Log.d(TAG, "Terminando bindUi( )");

    }

    private void validarCampos(){

    }

    private void llenarCampos(){
        if(aviso!=null){
            txtAvisoFhRegistro.setText(Util.convertDateToString( aviso.getAvisoFhRegistro(), Constantes.formatoFecha));
            txtSitio.setText("" + sitioDesc);
            txtOfnaPesca.setText("" + ofnapescaDesc);
            txtPermisoId.setText("" + permisoNumero);

            txtSitioId.setText("" + aviso.getSitioId());
            txtOfnaPescaId.setText("" + aviso.getOfnapescaId());
            txtPermisoId.setText("" + aviso.getPermisoId());
            txtUsuarioId.setText("" + usuarioId);
            txtPescadorId.setText("" + aviso.getPescadorId());
            txtAvisoId.setText("" + aviso.getId());
        }
    }





    private void goToConsultaAviso(int avisoId) {
        Log.d(TAG, "Iniciando goToConsultaAviso()");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AvisoConsultaFragment()).commit();
        Log.d(TAG, "Terminando goToConsultaAviso()");

    }


    private void setCredentialsIfExists() {
        Log.d(TAG, "Iniciando setCredentialsIfExists()");
        String usuario = Util.getUserPrefs(prefs);
        String contrasena = Util.getPasswordPrefs(prefs);
        String pescador = Util.getPescadorPrefs(prefs);
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(pescador)) {
            usuarioId = Integer.parseInt(usuario);
            pescadorId = Integer.parseInt(pescador);
        }
        Log.d(TAG, "Terminando setCredentialsIfExists()");
    }

}
