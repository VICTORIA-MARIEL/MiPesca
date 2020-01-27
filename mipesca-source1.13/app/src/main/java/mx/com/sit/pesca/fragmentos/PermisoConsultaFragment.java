package mx.com.sit.pesca.fragmentos;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.adapters.PermisoAdaptador;
import mx.com.sit.pesca.adapters.ViajeAdaptador;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.ViajeEntity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermisoConsultaFragment extends Fragment {
    private static final String TAG = "PermisoConsultaFragment";
    private Realm realm;
    private FloatingActionButton fab;
    private RealmResults<PermisoEntity> permisos;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView lblSinRegistros;

    public PermisoConsultaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_permiso_consulta, container, false);
        lblSinRegistros = view.findViewById(R.id.lblSinRegistros);
        realm = Realm.getDefaultInstance();
        permisos = realm.where(PermisoEntity.class).findAll();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listViewPermiso);
        mLayoutManager = new LinearLayoutManager(getContext());

        mAdapter = new PermisoAdaptador(permisos, R.layout.list_view_permiso_item, new PermisoAdaptador.OnItemClickListener() {
            @Override
            public void onItemClick(PermisoEntity viaje, int position) {
                int permisoId = permisos.get(position).getId();
                PermisoConsultaIndFragment fragmentoNvo = new PermisoConsultaIndFragment();
                Bundle data = new Bundle();
                data.putInt("permisoId", permisoId);
                fragmentoNvo.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentoNvo).commit();
            }
        });
// Lo usamos en caso de que sepamos que el layout no va a cambiar de tamaño, mejorando la performance
        mRecyclerView.setHasFixedSize(true);
        // Añade un efecto por defecto, si le pasamos null lo desactivamos por completo
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Enlazamos el layout manager y adaptor directamente al recycler view
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if(mAdapter!=null && mAdapter.getItemCount() == 0){
            lblSinRegistros.setVisibility(View.VISIBLE);
        }
        else{
            lblSinRegistros.setVisibility(View.GONE);
        }


        fab = view.findViewById(R.id.fabAddPermiso);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermisoFragment fragmentoNvo = new PermisoFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentoNvo).commit();
            }
        });


        Log.d(TAG, "Terminando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        return view;
    }

}
