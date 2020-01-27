package mx.com.sit.pesca.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.ArtePescaEntity;
import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.CooperativaEntity;
import mx.com.sit.pesca.entity.EdoPesqueriaRecursoEntity;
import mx.com.sit.pesca.entity.MunicipioEntity;
import mx.com.sit.pesca.entity.PresentacionEntity;
import mx.com.sit.pesca.entity.RecursoEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class ViajeFragment extends Fragment  {

    private static final String TAG = "ViajeFragment";

    private SharedPreferences prefs;
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private String municipioDesc;
    private String comunidadDesc;
    private String artepescaDesc;
    private ViajeEntity viaje;
    private ViajeRecursoEntity viajeRecurso;


    private TextView txtViajeFhRegistro;
    private EditText txtCaptura;
    private EditText txtPrecio;
    private EditText txtNoPiezas;
    private EditText txtCombustible;

    private AutoCompleteTextView txtMunicipio;
    private AutoCompleteTextView txtComunidad;
    private AutoCompleteTextView txtRecurso;
    private AutoCompleteTextView txtArtePesca;
    private AutoCompleteTextView txtPresentacion;

    private TextView txtMunicipioId;
    private TextView txtComunidadId;
    private TextView txtRecursoId;
    private TextView txtArtePescaId;
    private TextView txtPresentacionId;

    private TextView txtUsuarioId;
    private TextView txtPescadorId;


    private String[] listaMunicipios;
    private String[] listaComunidades;
    private String[] listaRecursos;
    private String[] listaArtesPesca;
    private String[] listaPresentaciones;

    private TextInputLayout ilViajeNoPiezas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_viaje, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        Log.d(TAG, "Terminando onCreateView");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Iniciando onViewCreated(View view, Bundle savedInstanceState)");
        realm = Realm.getDefaultInstance();

        txtViajeFhRegistro = view.findViewById(R.id.txtViajeFhRegistro);
        txtCaptura = view.findViewById(R.id.txtViajeCaptura);
        txtPrecio = view.findViewById(R.id.txtViajePrecio);
        txtNoPiezas = view.findViewById(R.id.txtViajeNoPiezas);
        txtCombustible = view.findViewById(R.id.txtViajeCombustible);

        txtMunicipio = view.findViewById(R.id.txtViajeMunicipio);
        txtComunidad = view.findViewById(R.id.txtViajeComunidad);
        txtRecurso = view.findViewById(R.id.txtViajeRecurso);
        txtArtePesca = view.findViewById(R.id.txtViajeArtePesca);
        txtPresentacion = view.findViewById(R.id.txtViajePresentacion);

        txtMunicipioId = view.findViewById(R.id.txtViajeMunicipioId);
        txtComunidadId = view.findViewById(R.id.txtViajeComunidadId);
        txtRecursoId = view.findViewById(R.id.txtViajeRecursoId);
        txtArtePescaId = view.findViewById(R.id.txtViajeArtePescaId);
        txtPresentacionId = view.findViewById(R.id.txtViajePresentacionId);

        txtUsuarioId = view.findViewById(R.id.txtViajeUsuarioId);
        txtPescadorId = view.findViewById(R.id.txtViajePescadorId);

        ilViajeNoPiezas = view.findViewById(R.id.ilViajeNoPiezas);

        final ImageView iconViajeArtePesca = (ImageView)view.findViewById(R.id.iconViajeArtePesca);
        final ImageView iconViajePresentacion = (ImageView)view.findViewById(R.id.iconViajePresentacion);
        final ImageView iconViajeRecurso = (ImageView)view.findViewById(R.id.iconViajeRecurso);
        final ImageView iconViajeComunidad = (ImageView)view.findViewById(R.id.iconViajeComunidad);
        final ImageView iconViajeMunicipio = (ImageView)view.findViewById(R.id.iconViajeMunicipio);

        final ImageView iconCambioArtePesca = (ImageView)view.findViewById(R.id.iconCambioArtePesca);
        final ImageView iconCambioPresentacion = (ImageView)view.findViewById(R.id.iconCambioPresentacion);
        final ImageView iconCambioRecurso = (ImageView)view.findViewById(R.id.iconCambioRecurso);
        final ImageView iconCambioComunidad = (ImageView)view.findViewById(R.id.iconCambioComunidad);
        final ImageView iconCambioMunicipio = (ImageView)view.findViewById(R.id.iconCambioMunicipio);

        /*Parametros iniciales para os campos de texto*/
        txtMunicipioId.setText("0");
        txtComunidadId.setText("0");
        txtRecursoId.setText("0");
        txtPresentacionId.setText("0");
        txtArtePescaId.setText("0");
        txtUsuarioId.setText("" + usuarioId);
        txtPescadorId.setText("" + pescadorId);
        txtViajeFhRegistro.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));

        //Buscar viaje no finalizado
        boolean bViajeNoFinalizado = false;
        String artepescaDescTemp = "";
        String municipioDescTemp = "";
        String comunidadDescTemp = "";
        String recursoDescTemp = "";
        String presentacionDescTemp = "";
        int recursoIdTemp = 0;
        int presentacionIdTemp = 0;
        ViajeEntity viajeNoFinalizado = realm.where(ViajeEntity.class).equalTo("viajeEstatus",0).findFirst();
        Log.d(TAG,"viajeNoFinalizado>" + viajeNoFinalizado);
        if(viajeNoFinalizado!=null){
            ComunidadEntity comunidadDTO = realm.where(ComunidadEntity.class).equalTo("comunidadId",viajeNoFinalizado.getComunidadId()).findFirst();
            comunidadDescTemp = comunidadDTO.getComunidadDescripcion();
            ArtePescaEntity artepescaDTO = realm.where(ArtePescaEntity.class).equalTo("artepescaId",viajeNoFinalizado.getArtepescaId()).findFirst();
            artepescaDescTemp = artepescaDTO.getArtepescaDescripcion();
            bViajeNoFinalizado = true;
        }
        //Buscar datos
        boolean bViajeUltimo = false;
        ViajeEntity viajeUltimo = realm.where(ViajeEntity.class).sort("viajeFhRegistro", Sort.DESCENDING).equalTo("viajesRecursos.veEsPrincipal",1).findFirst();
        Log.d(TAG,"viajeUltimo>" + viajeUltimo);
        if(!bViajeNoFinalizado && viajeUltimo!=null){
            try {
                MunicipioEntity municipioDTO = realm.where(MunicipioEntity.class).equalTo("municipioId", viajeUltimo.getMunicipioId()).findFirst();
                municipioDescTemp = municipioDTO.getMunicipioDescLarga();
                ComunidadEntity comunidadDTO = realm.where(ComunidadEntity.class).equalTo("comunidadId", viajeUltimo.getComunidadId()).findFirst();
                comunidadDescTemp = comunidadDTO.getComunidadDescripcion();
                ArtePescaEntity artepescaDTO = realm.where(ArtePescaEntity.class).equalTo("artepescaId", viajeUltimo.getArtepescaId()).findFirst();
                artepescaDescTemp = artepescaDTO.getArtepescaDescripcion();
                RealmList<ViajeRecursoEntity> veRecursosList = viajeUltimo.getViajesRecursos();
                if (veRecursosList != null && !veRecursosList.isEmpty()) {
                    ViajeRecursoEntity veDTO = veRecursosList.get(0);
                    recursoDescTemp = veDTO.getRecursoDescripcion();
                    presentacionDescTemp = veDTO.getPresentacionDescripcion();
                    recursoIdTemp = veDTO.getRecursoId();
                    presentacionIdTemp = veDTO.getPresentacionId();
                    if (recursoDescTemp.indexOf(Constantes.RECURSO_NO_PIEZAS) >= 0) {
                        ilViajeNoPiezas.setVisibility(View.VISIBLE);
                    } else {
                        ilViajeNoPiezas.setVisibility(View.GONE);
                        txtNoPiezas.setText("0");
                    }
                }

                bViajeUltimo = true;
            }
            catch(NullPointerException npe){
                bViajeUltimo = false;
            }
        }


        if(bViajeNoFinalizado){
            goToComplementoViaje(viajeNoFinalizado.getId(), comunidadDescTemp, artepescaDescTemp, bViajeNoFinalizado);
        }
        else{
            viajeNoFinalizado = null;
        }

         RealmResults<MunicipioEntity> result = realm.where(MunicipioEntity.class).findAll();
        ArrayList<String> findStringList = new ArrayList<String>();
        for (MunicipioEntity municipio : result) {
            findStringList.add(municipio.getMunicipioDescLarga());
        }
        listaMunicipios = Arrays.copyOf(findStringList.toArray(), findStringList.toArray().length,String[].class);
        final ArrayAdapter<String> adapterMun = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaMunicipios);
        txtMunicipio.setAdapter(adapterMun);


        listaComunidades = new String[] {};
        final ArrayAdapter<String> adapterComunidad = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaComunidades);
        txtComunidad.setAdapter(adapterComunidad);
        adapterComunidad.notifyDataSetChanged();
        txtComunidad.setEnabled(false);

        listaRecursos = new String[] {};
        final ArrayAdapter<String> adapterRecurso = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaRecursos);
        txtRecurso.setAdapter(adapterRecurso);
        adapterRecurso.notifyDataSetChanged();
        txtRecurso.setEnabled(false);


        listaArtesPesca = new String[] {};
        final ArrayAdapter<String> adapterArtePesca = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaArtesPesca);
        txtArtePesca.setAdapter(adapterArtePesca);
        adapterArtePesca.notifyDataSetChanged();
        txtArtePesca.setEnabled(false);

        listaPresentaciones = new String[] {};
        final ArrayAdapter<String> adapterPresentacion = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaPresentaciones);
        txtPresentacion.setAdapter(adapterPresentacion);
        adapterPresentacion.notifyDataSetChanged();
        txtPresentacion.setEnabled(false);

        //Precargando datos de viaje anterior
        if(bViajeUltimo){
            txtMunicipio.setText("" + municipioDescTemp);
            txtComunidad.setText("" + comunidadDescTemp);
            txtRecurso.setText("" + recursoDescTemp);
            txtPresentacion.setText("" + presentacionDescTemp);
            txtArtePesca.setText("" + artepescaDescTemp);
            artepescaDesc = artepescaDescTemp;
            comunidadDesc = comunidadDescTemp;
            municipioDesc = municipioDescTemp;

            txtMunicipioId.setText("" + viajeUltimo.getMunicipioId());
            txtComunidadId.setText("" + viajeUltimo.getComunidadId());
            txtRecursoId.setText("" + recursoIdTemp);
            txtPresentacionId.setText("" + presentacionIdTemp);
            txtArtePescaId.setText("" + viajeUltimo.getArtepescaId());

            txtMunicipio.setEnabled(false);
            txtComunidad.setEnabled(false);
            txtRecurso.setEnabled(false);
            txtPresentacion.setEnabled(false);
            txtArtePesca.setEnabled(false);

            iconCambioArtePesca.setVisibility(View.VISIBLE);
            iconCambioMunicipio.setVisibility(View.VISIBLE);
            iconCambioComunidad.setVisibility(View.VISIBLE);
            iconCambioRecurso.setVisibility(View.VISIBLE);
            iconCambioPresentacion.setVisibility(View.VISIBLE);

            iconViajeArtePesca.setVisibility(View.GONE);
            iconViajeMunicipio.setVisibility(View.GONE);
            iconViajeComunidad.setVisibility(View.GONE);
            iconViajeRecurso.setVisibility(View.GONE);
            iconViajePresentacion.setVisibility(View.GONE);

        }

        /*Acciones de ItemOnClickListener*/
        txtMunicipio.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Municipio>" + itemSelected);
                municipioDesc = itemSelected;
                MunicipioEntity municipioDTO = realm.where(MunicipioEntity.class).equalTo("municipioDescLarga",itemSelected).findFirst();
                Log.d(TAG,"Municipio Id>" + municipioDTO.getMunicipioId());
                txtMunicipioId.setText("" + municipioDTO.getMunicipioId());

                RealmResults<ComunidadEntity> result3 = realm.where(ComunidadEntity.class).equalTo("municipioId",municipioDTO.getMunicipioId()).findAll();
                ArrayList<String> findStringList3 = new ArrayList<String>();
                for (ComunidadEntity comunidad: result3) {
                    Log.d(TAG,"Comunidad ID > " + comunidad.getComunidadId()+ " Comunidad Desc > " + comunidad.getComunidadDescripcion()+  " Municipio Id>" + comunidad.getMunicipioId());
                    findStringList3.add(comunidad.getComunidadDescripcion());
                }
                listaComunidades = Arrays.copyOf(findStringList3.toArray(), findStringList3.toArray().length,String[].class);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, listaComunidades);
                txtComunidad.setAdapter(adapter3);
                txtComunidad.setEnabled(true);

                RealmResults<EdoPesqueriaRecursoEntity> result31 = realm.where(EdoPesqueriaRecursoEntity.class).equalTo("estadoId",municipioDTO.getEstadoId()).findAll();
                ArrayList<String> findStringList31 = new ArrayList<String>();
                for (EdoPesqueriaRecursoEntity pesqueriaRecurso: result31) {
                    Log.d(TAG,"Recurso ID > " + pesqueriaRecurso.getRecursoId()+ " Recurso Desc > " + pesqueriaRecurso.getRecursoDescripcion()+  " Estado Id>" + municipioDTO.getEstadoId());
                    findStringList31.add(pesqueriaRecurso.getRecursoDescripcion());
                }
                listaRecursos = Arrays.copyOf(findStringList31.toArray(), findStringList31.toArray().length,String[].class);
                ArrayAdapter<String> adapter31 = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, listaRecursos);
                txtRecurso.setAdapter(adapter31);
                txtRecurso.setEnabled(true);

                realm.close();
            }
        });


        txtComunidad.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Comunidad>" + itemSelected);
                comunidadDesc = itemSelected;
                ComunidadEntity comunidadDTO = realm.where(ComunidadEntity.class).equalTo("comunidadDescripcion",itemSelected).findFirst();
                Log.d(TAG,"Comunidad Id>" + comunidadDTO.getComunidadId());
                txtComunidadId.setText("" + comunidadDTO.getComunidadId());

                realm.close();
            }
        });

        txtRecurso.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
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

                RealmResults<ArtePescaEntity> resultAP = realm.where(ArtePescaEntity.class).equalTo("recursoId",recursoDTO.getRecursoId()).findAll();
                ArrayList<String> findStringListAP = new ArrayList<String>();
                for (ArtePescaEntity artepesca: resultAP) {
                    Log.d(TAG,"ArtePesca ID > " + artepesca.getArtepescaId()+ " ArtePesca Desc > " + artepesca.getArtepescaDescripcion()
                                            +  " Recurso Id>" + artepesca.getRecursoId());
                    findStringListAP.add(artepesca.getArtepescaDescripcion());
                }
                listaArtesPesca = Arrays.copyOf(findStringListAP.toArray(), findStringListAP.toArray().length,String[].class);
                ArrayAdapter<String> adapterAP = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, listaArtesPesca);
                txtArtePesca.setAdapter(adapterAP);
                txtArtePesca.setEnabled(true);

                realm.close();

                if(itemSelected.indexOf(Constantes.RECURSO_NO_PIEZAS) >= 0){
                    ilViajeNoPiezas.setVisibility(View.VISIBLE);
                }
                else{
                    ilViajeNoPiezas.setVisibility(View.GONE);
                    txtNoPiezas.setText("0");
                }

            }
        });

        txtArtePesca.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item ArtePesca>" + itemSelected);
                artepescaDesc = itemSelected;
                ArtePescaEntity artepescaDTO = realm.where(ArtePescaEntity.class).equalTo("artepescaDescripcion",itemSelected).findFirst();

                txtArtePescaId.setText("" + artepescaDTO.getArtepescaId());
                realm.close();
            }
        });

        txtPresentacion.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                realm = Realm.getDefaultInstance();
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Presentacion>" + itemSelected);
                PresentacionEntity presentacionDTO = realm.where(PresentacionEntity.class).equalTo("presentacionDescripcion",itemSelected).findFirst();

                txtPresentacionId.setText("" + presentacionDTO.getPresentacionId());
                realm.close();
            }
        });
        /*Acciones de los iconos drop down list*/

        iconViajeArtePesca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtArtePesca.showDropDown();
            }
        });

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

        iconViajeComunidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtComunidad.showDropDown();
            }
        });

        iconViajeMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMunicipio.showDropDown();
            }
        });


        iconCambioArtePesca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm = Realm.getDefaultInstance();
                RealmResults<ArtePescaEntity> resultAP = realm.where(ArtePescaEntity.class).equalTo("recursoId",Integer.parseInt(txtRecursoId.getText().toString())).findAll();
                ArrayList<String> findStringListAP = new ArrayList<String>();
                for (ArtePescaEntity artepesca: resultAP) {
                    Log.d(TAG,"ArtePesca ID > " + artepesca.getArtepescaId()+ " ArtePesca Desc > " + artepesca.getArtepescaDescripcion()
                            +  " Recurso Id>" + artepesca.getRecursoId());
                    findStringListAP.add(artepesca.getArtepescaDescripcion());
                }
                listaArtesPesca = Arrays.copyOf(findStringListAP.toArray(), findStringListAP.toArray().length,String[].class);
                ArrayAdapter<String> adapterAP = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, listaArtesPesca);
                txtArtePesca.setAdapter(adapterAP);
                txtArtePesca.setText("");
                txtArtePescaId.setText("0");
                iconCambioArtePesca.setVisibility(View.GONE);
                iconViajeArtePesca.setVisibility(View.VISIBLE);
                txtArtePesca.setEnabled(true);
                artepescaDesc ="";

                realm.close();
            }
        });

        iconCambioPresentacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm = Realm.getDefaultInstance();
                RealmResults<PresentacionEntity> result3 = realm.where(PresentacionEntity.class).equalTo("recursoId",Integer.parseInt(txtRecursoId.getText().toString())).findAll();
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
                txtPresentacion.setText("");
                txtPresentacionId.setText("0");
                iconCambioPresentacion.setVisibility(View.GONE);
                iconViajePresentacion.setVisibility(View.VISIBLE);
                txtPresentacion.setEnabled(true);
                realm.close();
            }
        });

        iconCambioRecurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm = Realm.getDefaultInstance();
                MunicipioEntity municipioDTO = realm.where(MunicipioEntity.class).equalTo("municipioId",Integer.parseInt(txtMunicipioId.getText().toString())).findFirst();

                RealmResults<EdoPesqueriaRecursoEntity> result3 = realm.where(EdoPesqueriaRecursoEntity.class).equalTo("estadoId",municipioDTO.getEstadoId()).findAll();
                ArrayList<String> findStringList3 = new ArrayList<String>();
                for (EdoPesqueriaRecursoEntity pesqueriaRecurso: result3) {
                    Log.d(TAG,"Recurso ID > " + pesqueriaRecurso.getRecursoId()+ " Recurso Desc > " + pesqueriaRecurso.getRecursoDescripcion()+  " Estado Id>" + pesqueriaRecurso.getEstadoId());
                    findStringList3.add(pesqueriaRecurso.getRecursoDescripcion());
                }
                listaRecursos = Arrays.copyOf(findStringList3.toArray(), findStringList3.toArray().length,String[].class);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, listaRecursos);
                txtRecurso.setAdapter(adapter3);
                txtRecurso.setText("");
                txtRecursoId.setText("0");
                iconCambioRecurso.setVisibility(View.GONE);
                iconViajeRecurso.setVisibility(View.VISIBLE);
                txtRecurso.setEnabled(true);


                txtArtePesca.setText("");
                txtArtePescaId.setText("0");
                iconCambioArtePesca.setVisibility(View.GONE);
                iconViajeArtePesca.setVisibility(View.VISIBLE);
                txtArtePesca.setEnabled(false);
                artepescaDesc ="";

                txtPresentacion.setText("");
                txtPresentacionId.setText("0");
                iconCambioPresentacion.setVisibility(View.GONE);
                iconViajePresentacion.setVisibility(View.VISIBLE);
                txtPresentacion.setEnabled(false);
                realm.close();

            }
        });

        iconCambioComunidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm = Realm.getDefaultInstance();
                RealmResults<ComunidadEntity> result3 = realm.where(ComunidadEntity.class).equalTo("municipioId",Integer.parseInt(txtMunicipioId.getText().toString())).findAll();
                ArrayList<String> findStringList3 = new ArrayList<String>();
                for (ComunidadEntity comunidad: result3) {
                    Log.d(TAG,"Comunidad ID > " + comunidad.getComunidadId()+ " Comunidad Desc > " + comunidad.getComunidadDescripcion()+  " Municipio Id>" + comunidad.getMunicipioId());
                    findStringList3.add(comunidad.getComunidadDescripcion());
                }
                listaComunidades = Arrays.copyOf(findStringList3.toArray(), findStringList3.toArray().length,String[].class);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, listaComunidades);
                txtComunidad.setAdapter(adapter3);
                txtComunidad.setText("");
                txtComunidadId.setText("0");
                iconCambioComunidad.setVisibility(View.GONE);
                iconViajeComunidad.setVisibility(View.VISIBLE);
                txtComunidad.setEnabled(true);

                txtArtePesca.setText("");
                txtArtePescaId.setText("0");
                iconCambioArtePesca.setVisibility(View.GONE);
                iconViajeArtePesca.setVisibility(View.VISIBLE);
                txtArtePesca.setEnabled(false);
                artepescaDesc ="";

                txtRecurso.setText("");
                txtRecursoId.setText("0");
                iconCambioRecurso.setVisibility(View.GONE);
                iconViajeRecurso.setVisibility(View.VISIBLE);
                txtRecurso.setEnabled(false);

                txtPresentacion.setText("");
                txtPresentacionId.setText("0");
                iconCambioPresentacion.setVisibility(View.GONE);
                iconViajePresentacion.setVisibility(View.VISIBLE);
                txtPresentacion.setEnabled(false);
                realm.close();
            }
        });


        iconCambioMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMunicipio.setText("");
                txtMunicipioId.setText("0");
                iconCambioMunicipio.setVisibility(View.GONE);
                iconViajeMunicipio.setVisibility(View.VISIBLE);
                txtMunicipio.setEnabled(true);
                municipioDesc = "";


                txtComunidad.setText("");
                txtComunidadId.setText("0");
                iconCambioComunidad.setVisibility(View.GONE);
                iconViajeComunidad.setVisibility(View.VISIBLE);
                txtComunidad.setEnabled(true);
                comunidadDesc = "";

                txtRecurso.setText("");
                txtRecursoId.setText("0");
                iconCambioRecurso.setVisibility(View.GONE);
                iconViajeRecurso.setVisibility(View.VISIBLE);
                txtRecurso.setEnabled(false);

                txtArtePesca.setText("");
                txtArtePescaId.setText("0");
                iconCambioArtePesca.setVisibility(View.GONE);
                iconViajeArtePesca.setVisibility(View.VISIBLE);
                txtArtePesca.setEnabled(false);
                artepescaDesc ="";


                txtPresentacion.setText("");
                txtPresentacionId.setText("0");
                iconCambioPresentacion.setVisibility(View.GONE);
                iconViajePresentacion.setVisibility(View.VISIBLE);
                txtPresentacion.setEnabled(false);
            }
        });


        /*Acciones de etiquetas*/
        txtMunicipio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtMunicipio.setHint(R.string.lblViajeMunicipio);
                } else {
                    txtMunicipio.setHint("");
                }
            }
        });
        txtComunidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtComunidad.setHint(R.string.lblViajeComunidad);
                } else {
                    txtComunidad.setHint("");
                }
            }
        });
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
        txtArtePesca.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtArtePesca.setHint(R.string.lblViajeArtePesca);
                } else {
                    txtArtePesca.setHint("");
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
        txtCombustible.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtCombustible.setHint(R.string.lblViajeCombustible);
                } else {
                    txtCombustible.setHint("");
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
                    int viajeId = insertarViaje(viaje, viajeRecurso);
                    Toast.makeText(getContext(), "La información del viaje se guardo correctamente.", Toast.LENGTH_LONG).show();
                    goToComplementoViaje(viajeId,comunidadDesc , artepescaDesc, false);
                }
            }
        });
        realm.close();
        Log.d(TAG, "Terminando onViewCreated(View view, Bundle savedInstanceState)");
    }

    private int insertarViaje(ViajeEntity viaje, ViajeRecursoEntity viajeRecurso){
        Log.d(TAG, "Iniciando insertarViaje(ViajeEntity viaje, ViajeRecursoEntity viajeRecurso)");

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(viaje);
        viajeRecurso.setViajeId(viaje.getId());
        realm.copyToRealmOrUpdate(viajeRecurso);
        viaje.getViajesRecursos().add(viajeRecurso);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "Inserte viaje ID >" +viaje.getId());
        Log.d(TAG, "Terminando insertarViaje(ViajeEntity viaje, ViajeRecursoEntity viajeRecurso)");
        return viaje.getId();
    }

    private boolean validarCampos(){
        Log.d(TAG, "Iniciando validarCampos()");
        boolean exito = true;

        if(usuarioId == 0){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
        }
        if(exito && pescadorId == 0){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
        }

        if(exito && Integer.parseInt(txtMunicipioId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar un municipio.", Toast.LENGTH_LONG).show();
            exito = false;
            txtMunicipio.requestFocus();
        }

        if(exito && Integer.parseInt(txtComunidadId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar una comunidad.", Toast.LENGTH_LONG).show();
            exito = false;
            txtComunidad.requestFocus();
        }

        if(exito && Integer.parseInt(txtRecursoId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar un recurso.", Toast.LENGTH_LONG).show();
            exito = false;
            txtRecurso.requestFocus();
        }

        if(exito && Integer.parseInt(txtArtePescaId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar el arte de pesca.", Toast.LENGTH_LONG).show();
            exito = false;
            txtArtePesca.requestFocus();
        }

        try {
            if(exito && Integer.parseInt(txtCaptura.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar una cantidad para la captura.", Toast.LENGTH_LONG).show();
                exito = false;
                txtCaptura.setText("");
                txtCaptura.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar una cantidad para la captura.", Toast.LENGTH_LONG).show();
            txtCaptura.setText("");
            txtCaptura.requestFocus();
        }

        try {
            if(exito && Integer.parseInt(txtPrecio.getText().toString()) <= 0){
                Toast.makeText(this.getContext(), "Favor de ingresar un precio correcto.", Toast.LENGTH_LONG).show();
                exito = false;
                txtPrecio.setText("");
                txtPrecio.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar un precio correcto.", Toast.LENGTH_LONG).show();
            txtPrecio.setText("");
            txtPrecio.requestFocus();
        }

        try {
            if(exito && Integer.parseInt(txtNoPiezas.getText().toString()) < 0){
                Toast.makeText(this.getContext(), "Favor de ingresar el número de piezas.", Toast.LENGTH_LONG).show();
                exito = false;
                txtNoPiezas.setText("");
                txtNoPiezas.requestFocus();
            }
        }
        catch(NumberFormatException nfe){
            exito = false;
            Toast.makeText(this.getContext(), "Favor de ingresar el número de piezas.", Toast.LENGTH_LONG).show();
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


        if(exito && Integer.parseInt(txtPresentacionId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar la presentación.", Toast.LENGTH_LONG).show();
            exito = false;
            txtPresentacion.requestFocus();
        }
        if(exito) {
            viaje = new ViajeEntity(
                    Integer.parseInt(txtMunicipioId.getText().toString()),
                    Integer.parseInt(txtComunidadId.getText().toString()),
                    Integer.parseInt(txtArtePescaId.getText().toString()),
                    Integer.parseInt(txtCombustible.getText().toString()),
                    usuarioId, pescadorId, municipioDesc,comunidadDesc,  artepescaDesc);
            viajeRecurso = new ViajeRecursoEntity(
                    Integer.parseInt(txtRecursoId.getText().toString()),
                    Integer.parseInt(txtPresentacionId.getText().toString()),
                    Constantes.ES_PRINCIPAL,
                    Integer.parseInt(txtCaptura.getText().toString()),
                    Integer.parseInt(txtPrecio.getText().toString()),
                    Integer.parseInt(txtNoPiezas.getText().toString()),
                    (short) Constantes.VIAJE_ESTATUS_INICIAL,
                    usuarioId,
                    txtRecurso.getText().toString(),
                    txtPresentacion.getText().toString(),
                    0
            );
            viaje.getViajesRecursos().add(viajeRecurso);
        }

        Log.d(TAG, "Terminando validarCampos()");
        return exito;
    }

    private void goToComplementoViaje(int viajeId, String comunidadDesc, String artepescaDesc, boolean finalizado){
        Log.d(TAG, "Iniciando goToComplementoViaje(iint viajeId,String comunidadDesc, String artepescaDesc)");
        ViajeComplementoFragment fragmentoNvo = new ViajeComplementoFragment();
        Bundle data = new Bundle();
        data.putInt("viajeId", viajeId);
        data.putString("comunidadDesc", comunidadDesc);
        data.putString("artepescaDesc", artepescaDesc);
        data.putBoolean("finalizado", !finalizado);
        fragmentoNvo.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentoNvo).commit();
        Log.d(TAG, "Terminando goToComplementoViaje(iint viajeId,String comunidadDesc, String artepescaDesc)");

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
