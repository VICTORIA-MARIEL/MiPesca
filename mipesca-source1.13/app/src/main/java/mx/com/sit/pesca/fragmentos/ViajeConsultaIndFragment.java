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
import mx.com.sit.pesca.adapters.ViajeRecursoAdaptador;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class ViajeConsultaIndFragment extends Fragment {
    private static final String TAG = "ViajeConsultaIndFragmen";
    private Realm realm;
    private int usuarioId;
    private int pescadorId;

    private ViajeEntity viaje;

    private int viajeId;
    private SharedPreferences prefs;
    private String comunidadDesc;
    private String artepescaDesc;

    private TextView txtViajeFhRegistro;
    private TextView txtCombustible;

    private TextView txtComunidad;
    private TextView txtArtePesca;

    private TextView txtComunidadId;
    private TextView txtArtePescaId;

    private TextView txtUsuarioId;
    private TextView txtPescadorId;
    private TextView txtViajeId;

    private LinearLayout divComplemento;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_viaje_consulta_ind, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        bindUi(view);
        Bundle data = this.getArguments();
        if (data != null) {
            viajeId = data.getInt("viajeId", 0);
//            finalizado = data.getBoolean("finalizado");
        }
        //Obtener información del viajeadmin    .
        realm = Realm.getDefaultInstance();
        this.viaje = realm.where(ViajeEntity.class).equalTo("id",viajeId).findFirst();
        final RealmResults<ViajeRecursoEntity> viajeRecursos = realm.where(ViajeRecursoEntity.class).equalTo("viajeId",viajeId).findAll();
        if(this.viaje!=null) {
            txtComunidadId.setText("" + this.viaje.getComunidadId());
            txtArtePescaId.setText("" + this.viaje.getArtepescaId());
            txtCombustible.setText("" + this.viaje.getCombustible());
            txtViajeFhRegistro.setText( Util.convertDateToString(this.viaje.getViajeFhRegistro(), Constantes.formatoFecha));
            artepescaDesc = viaje.getArtepescaDescripcion();
            comunidadDesc = viaje.getComunidadDescripcion();
        }
        realm.close();


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


        llenarCampos();

        Button btnRegresar = view.findViewById(R.id.btnRegresarViaje);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToConsultaViaje(viajeId);
            }
        });

        Log.d(TAG, "Terminando onCreateView");
        return view;
    }

    private void bindUi(View view) {
        Log.d(TAG, "Iniciando bindUi( )");
        divComplemento = view.findViewById(R.id.divComplemento);
        txtViajeFhRegistro = view.findViewById(R.id.txtViajeFhRegistro);
        txtCombustible = view.findViewById(R.id.txtViajeCombustible);

        txtComunidad = view.findViewById(R.id.txtViajeComunidad);
        txtArtePesca = view.findViewById(R.id.txtViajeArtePesca);

        txtComunidadId = view.findViewById(R.id.txtViajeComunidadId);
        txtArtePescaId = view.findViewById(R.id.txtViajeArtePescaId);

        txtUsuarioId = view.findViewById(R.id.txtViajeUsuarioId);
        txtPescadorId = view.findViewById(R.id.txtViajePescadorId);
        txtViajeId = view.findViewById(R.id.txtViajeId);

        divComplemento = view.findViewById(R.id.divComplemento);
        Log.d(TAG, "Terminando bindUi( )");

    }

    private void validarCampos(){

    }

    private void llenarCampos(){
        if(viaje!=null){
            txtViajeFhRegistro.setText(Util.convertDateToString( viaje.getViajeFhRegistro(), Constantes.formatoFecha));
            txtCombustible.setText("" + viaje.getCombustible()+" lts");
            txtComunidad.setText("" + comunidadDesc);
            txtArtePesca.setText("" + artepescaDesc);
            txtComunidadId.setText("" + viaje.getComunidadId());
            txtArtePescaId.setText("" + viaje.getArtepescaId());
            txtUsuarioId.setText("" + usuarioId);
            txtPescadorId.setText("" + viaje.getPescadorId());
            txtViajeId.setText("" + viaje.getId());
        }
    }





    private void goToConsultaViaje(int viajeId) {
        Log.d(TAG, "Iniciando goToConsultaViaje()");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ViajeConsultaFragment()).commit();
        Log.d(TAG, "Terminando goToConsultaViaje()");

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
