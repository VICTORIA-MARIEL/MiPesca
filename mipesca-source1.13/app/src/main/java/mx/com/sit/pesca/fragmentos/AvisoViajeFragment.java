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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.annotations.Required;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.adapters.AvisoExpandableListAdapter;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class AvisoViajeFragment extends Fragment {
    private static final String TAG = "AvisoViajeFragment";
    private SharedPreferences prefs;
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private int avisoId;
    private ExpandableListView listView;

    private List<String> listDataHeader;
    private HashMap<String,List<ViajeRecursoEntity>> listHash;



    public AvisoViajeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_aviso_viaje, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        Bundle data = this.getArguments();
        if(data != null) {
            avisoId = data.getInt("avisoId",0);
        }

        listView = (ExpandableListView)view.findViewById(R.id.listViewAvisoViaje);
        /*Obtener viajes finalizado*/

        realm = Realm.getDefaultInstance();
        RealmResults<ViajeEntity> viajes = realm.where(ViajeEntity.class).notEqualTo("viajeEstatus",Constantes.VIAJE_ESTATUS_DECLARADO).equalTo("usuarioId",usuarioId).findAll();
        initData(viajes);
        realm.close();

        final AvisoExpandableListAdapter  listAdapter = new AvisoExpandableListAdapter(this.getContext(), listDataHeader,listHash) ;
        listView.setAdapter(listAdapter);

        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Seleccionados >" + listAdapter.seleccionados);
                Log.d(TAG,"Listado >" + listAdapter.listado);
                if(listAdapter.seleccionados!=null && listAdapter.isEmpty()) {
                    Toast.makeText(getContext(), "Favor de seleccionar al menos un recurso.", Toast.LENGTH_LONG).show();
                }
                else{
                    List<ViajeEntity> listadoViaje = obtenerViajes(listAdapter.listado,listAdapter.seleccionados);
                    List<AvisoViajeEntity> listadoAvisoViajes = obtenerAvisoViajes(listAdapter.listado,listAdapter.seleccionados);

                    insertarAvisoViaje(listadoAvisoViajes,listadoViaje);
                    goToPDF(avisoId);
                }
            }
        });

        Log.d(TAG, "Terminando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        return view;
    }

    private List<AvisoViajeEntity> obtenerAvisoViajes(Map<String, ViajeRecursoEntity> listado, List<String> seleccionados){
        Log.d(TAG, "Iniciando obtenerAvisoViajes");
        List<AvisoViajeEntity> listadoAvisoViajes = new ArrayList<AvisoViajeEntity>();
        Set llaves = listado.keySet();
        Iterator<String> it = llaves.iterator();
        while(it.hasNext()){
            String clave = it.next();
            ViajeRecursoEntity vre = listado.get(clave);
            AvisoViajeEntity avisoViaje = new AvisoViajeEntity(
                avisoId,
                vre.getId(),
                usuarioId,
                (seleccionados.contains(clave)?(short)1:(short)0),
                vre.getRecursoDescripcion(),
                vre.getPresentacionDescripcion(),
                vre.getVeNoPiezas(),
                vre.getVePrecio(),
                vre.getVeCaptura()
            );
            listadoAvisoViajes.add(avisoViaje);
        }


        Log.d(TAG, "Terminando obtenerAvisoViajes");
        return listadoAvisoViajes;
    }


    private List<ViajeEntity> obtenerViajes(Map<String, ViajeRecursoEntity> listado, List<String> seleccionados){
        Log.d(TAG, "Iniciando obtenerViajes");
        List<ViajeEntity> listadoViaje = new ArrayList<ViajeEntity>();
                for(String clave: seleccionados){
                    if(listado.containsKey(clave)){
                        ViajeRecursoEntity cvre = listado.get(clave);
                        ViajeEntity viaje = new ViajeEntity();
                        realm = Realm.getDefaultInstance();
                        ViajeEntity viajeTemporal = realm.where(ViajeEntity.class).equalTo("id",cvre.getViajeId()).findFirst();
                        viaje.setId(viajeTemporal.getId());
                        viaje.setViajeFhRegistro(viajeTemporal.getViajeFhRegistro());
                        viaje.setViajeFhFinalizo(viajeTemporal.getViajeFhFinalizo());
                        viaje.setComunidadId(viajeTemporal.getComunidadId());
                        viaje.setArtepescaId(viajeTemporal.getArtepescaId());
                        viaje.setCombustible(viajeTemporal.getCombustible());
                        viaje.setUsuarioId(viajeTemporal.getUsuarioId());
                        viaje.setPescadorId(viajeTemporal.getPescadorId());
                        viaje.setViajeIdLocal(viajeTemporal.getViajeIdLocal());
                        viaje.setViajeFhSincronizacion(viajeTemporal.getViajeFhSincronizacion());
                        //viaje.setViajeEstatus(viajeTemporal.getViajeEstatus());
                        viaje.setViajeEstatus(Constantes.VIAJE_ESTATUS_DECLARADO);
                        viaje.setViajeEstatusSinc(viajeTemporal.getViajeEstatusSinc());
                        viaje.setViajeFolio(viajeTemporal.getViajeFolio());
                        viaje.setViajesRecursos(viajeTemporal.getViajesRecursos());
                        viaje.setComunidadDescripcion(viajeTemporal.getComunidadDescripcion());
                        viaje.setArtepescaDescripcion(viajeTemporal.getArtepescaDescripcion());
                        realm.close();
                        listadoViaje.add(viaje);
                    }
                }
        Log.d(TAG, "Terminando obtenerViajes");
        return listadoViaje;
    }

    private void insertarAvisoViaje(List<AvisoViajeEntity> avisoViajes, List<ViajeEntity> viajes){
        Log.d(TAG, "Iniciando insertarAvisoViaje()");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        //Insertar en aviso viaje
        for(AvisoViajeEntity avisoViaje: avisoViajes) {
            realm.copyToRealm(avisoViaje);
        }
        //MOdificar en aviso a estatus finalizado y agregar los viajes
        AvisoEntity aviso =  realm.where(AvisoEntity.class).equalTo("id",avisoId).findFirst();
        aviso.setAvisoEstatus(Constantes.AVISO_ESTATUS_FINALIZADO);
        realm.copyToRealmOrUpdate(aviso);
        for(AvisoViajeEntity avisoViaje: avisoViajes) {
            aviso.getAvisoViajes().add(avisoViaje);
        }
        //Modificar en viaje a estatus declarado
        for(ViajeEntity viaje:viajes ) {
            realm.copyToRealmOrUpdate(viaje);
        }
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Terminando insertarAvisoViaje()");
    }

    private void goToPDF(int avisoId){
        Log.d(TAG, "Iniciando goToPDF(int avisoId)");
        AvisoPDFFragment fragmentoNvo = new AvisoPDFFragment();
        Bundle data = new Bundle();
        data.putInt("avisoId", avisoId);
        fragmentoNvo.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentoNvo).commit();
        Log.d(TAG, "Terminando goToPDF(int avisoId)");

    }

    private void initData(RealmResults<ViajeEntity> listaViajesFinalizados){
        Log.d(TAG, "Iniciando initData()");
        listDataHeader = new ArrayList<>();
        listHash = new HashMap();

        for(ViajeEntity ve: listaViajesFinalizados) {
            String header = "Fecha: " + Util.convertDateToString(ve.getViajeFhRegistro(),Constantes.formatoFecha) + " Folio: " + ve.getViajeFolio();
            listDataHeader.add(header);
            List<ViajeRecursoEntity> data = new ArrayList<>();
            for(ViajeRecursoEntity vre: ve.getViajesRecursos()){
                String strData = "" + vre.getRecursoDescripcion() + " - " + vre.getPresentacionDescripcion();
                //data.add(strData);
                data.add(vre);
            }
            listHash.put(header,data);
        }


        Log.d(TAG, "Terminando initData()");

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
