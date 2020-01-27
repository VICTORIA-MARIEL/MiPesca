package mx.com.sit.pesca.fragmentos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.adapters.ViajeAdaptador;
import mx.com.sit.pesca.entity.ComunidadEntity;
import mx.com.sit.pesca.entity.PescadorEntity;
import mx.com.sit.pesca.entity.RecursoEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;


public class ReporteFragment extends Fragment    implements AdapterView.OnItemClickListener{
    private static final String TAG = "ReporteFragment";
    private SharedPreferences prefs;
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private MaskEditText txtFhInicio;
    private MaskEditText txtFhFin;
    private AutoCompleteTextView txtRecurso;
    private TextView txtRecursoId;
    private TextView txtRecursoDesc;
    private Switch chkReporteAcumulado;
    private Switch chkReportePorDia;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;

    private String[] listaRecursos;
    private ViajeAdaptador adapterVe;
    private RealmResults<ViajeEntity> viajesRecursos;

    private BarChart barChart;
    private LineChart lineChart;
//    private int[]colors=new int[]{Color.BLACK,Color.RED,Color.GREEN,Color.BLUE,Color.LTGRAY,Color.CYAN,Color.YELLOW
//    };
    private String[] dias = new String[0];
    private String[] diasCompletos = new String[0];
    private int[] pescas = new int[0];
    private int[] totales = new int[0];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_reporte, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        realm = Realm.getDefaultInstance();
        txtFhFin = view.findViewById(R.id.txtFhFin);
        txtFhInicio =view.findViewById(R.id.txtFhInicio);

        barChart = view.findViewById(R.id.graficaBar);
        lineChart = view.findViewById(R.id.graficaLine);
        lineChart.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);

        txtFhInicio.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH);
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
                        int month = cal.get(Calendar.MONTH);
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
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + (month+1) + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month+1) + "/" +  String.format("%04d", year);
                txtFhInicio.setText(date);
            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Log.d(TAG,"onDateSet: mm/dd/yyyy: " + (month+1) + "/" +  day + "/" + year);
                String date = "" + String.format("%02d", day) + "/" + String.format("%02d", month+1) + "/" +  String.format("%04d", year);
                txtFhFin.setText(date);
            }
        };


        //createCharts();
        viajesRecursos = realm.where(ViajeEntity.class).equalTo("id",0).findAll();
        viajesRecursos.addChangeListener(new RealmChangeListener<RealmResults<ViajeEntity>>() {
            @Override
            public void onChange(RealmResults<ViajeEntity> viajeRecursoEntities) {
                adapterVe.notifyDataSetChanged();
            }
        });
        Log.d(TAG, "Terminando onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)");
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Iniciando onViewCreated(View view, Bundle savedInstanceState)");
        txtRecurso = view.findViewById(R.id.txtRecurso);
        txtRecursoId= view.findViewById(R.id.txtRecursoId);
        txtRecursoDesc= view.findViewById(R.id.txtRecursoDesc);
        chkReporteAcumulado= view.findViewById(R.id.chkReporteAcumulado);
        chkReportePorDia= view.findViewById(R.id.chkReportePorDia);
        final ImageView iconRecurso = (ImageView)view.findViewById(R.id.iconRecurso);
        /*Llenado de combos*/
        PescadorEntity pescadorEntity = realm.where(PescadorEntity.class).equalTo("id",pescadorId).findFirst();

        /*RealmResults<RecursoEntity> result = realm.where(RecursoEntity.class).equalTo("comunidadId",pescadorEntity.getComunidadId()).findAll();
        ArrayList<String> findStringList = new ArrayList<String>();
        for (RecursoEntity recurso : result) {
            findStringList.add(recurso.getRecursoDescripcion());
        }
        listaRecursos = Arrays.copyOf(findStringList.toArray(), findStringList.toArray().length,String[].class);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaRecursos);
        txtRecurso.setAdapter(adapter1);
        */

        txtFhInicio.setText(Util.convertDateToString(new Date(), Constantes.formatoFecha));
        txtFhFin.setText(Util.convertDateToString(new Date(),Constantes.formatoFecha));

        RealmResults<ViajeRecursoEntity> result2 = realm.where(ViajeRecursoEntity.class).equalTo("veUsrRegistro",usuarioId).distinct("recursoDescripcion").findAll();
        ArrayList<String> findStringList2 = new ArrayList<String>();
        for (ViajeRecursoEntity viajeRecurso : result2) {
            findStringList2.add(viajeRecurso.getRecursoDescripcion());
        }
        listaRecursos = Arrays.copyOf(findStringList2.toArray(), findStringList2.toArray().length,String[].class);
        final ArrayAdapter<String> adapterRecurso = new ArrayAdapter<String>
                (this.getContext(), android.R.layout.select_dialog_item, listaRecursos);
        txtRecurso.setAdapter(adapterRecurso);


        txtRecursoId.setText("0");
        txtRecursoDesc.setText("");
        txtRecurso.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itemSelected = adapterView.getAdapter().getItem(position).toString();
                Log.d(TAG,"Item Recurso>" + itemSelected);
                RecursoEntity recursoDTO = realm.where(RecursoEntity.class).equalTo("recursoDescripcion",itemSelected).findFirst();
                Log.d(TAG,"Recurso Id>" + recursoDTO.getRecursoId());
                txtRecursoId.setText("" + recursoDTO.getRecursoId());
                Log.d(TAG,"Recurso Desc>" + recursoDTO.getRecursoDescripcion());
                txtRecursoDesc.setText("" + recursoDTO.getRecursoDescripcion());

            }
        });


        /* Acciones de focus*/
        txtRecurso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtRecurso.setHint(R.string.lblReportePescaRecurso);
                } else {
                    txtRecurso.setHint("");
                }
            }
        });

        txtFhInicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtFhInicio.setHint(R.string.lblReportePescaFechaInicio);
                } else {
                    txtFhInicio.setHint("");
                }
            }
        });

        txtFhFin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtFhFin.setHint(R.string.lblReportePescaFechaFinal);
                } else {
                    txtFhFin.setHint("");
                }
            }
        });



        /*Acciones de los botones*/

        final Button btnBuscar = view.findViewById(R.id.btnReportePescaGenerar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    lineChart.setVisibility(View.VISIBLE);
                    barChart.setVisibility(View.VISIBLE);
                    buscarViajes(view);
                    createCharts();
                }
            }
        });


        Button btnLimpiar = view.findViewById(R.id.btnReportePescaLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFhInicio.setText("");
                txtFhFin.setText("");
                txtRecurso.setText("");
                txtRecurso.setSelected(false);
                txtRecursoId.setText("0");
                txtRecursoDesc.setText("0");
                lineChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
            }
        });

        chkReporteAcumulado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnBuscar.callOnClick();
            }
        });
        chkReportePorDia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnBuscar.callOnClick();
            }
        });

        iconRecurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRecurso.showDropDown();
            }
        });


        Log.d(TAG, "Terminando onViewCreated(View view, Bundle savedInstanceState)");
    }

    private void buscarViajes(View view){
        Date dFechaInicio = Util.convertStringToDate(txtFhInicio.getText().toString(),"00:00:00","/");
        Date dFechaFin = Util.convertStringToDate(txtFhFin.getText().toString(),"23:59:59","/");
//        int recursoId = Integer.parseInt(txtRecursoId.getText().toString());
        String recursoDesc = txtRecursoDesc.getText().toString();
        List<ViajeEntity> viajesTemporal = new ArrayList<ViajeEntity>();
        if(!"".equals(recursoDesc)) {
            RealmResults<ViajeEntity> viajesRecursos = realm.where(ViajeEntity.class)
                    .between("viajeFhRegistro", dFechaInicio, dFechaFin)
                    .equalTo("viajesRecursos.recursoDescripcion", recursoDesc)
                    .equalTo("usuarioId",usuarioId)
                    .equalTo("pescadorId",pescadorId)
                    .limit(Constantes.MAXIMO_REGISTROS)
                    .findAll();
        }
        else{
            RealmResults<ViajeEntity> viajesRecursos = realm.where(ViajeEntity.class)
                    .equalTo("usuarioId",usuarioId)
                    .equalTo("pescadorId",pescadorId)
                    .between("viajeFhRegistro", dFechaInicio, dFechaFin)
                    .limit(Constantes.MAXIMO_REGISTROS)
                    .findAll();
        }
        List<String> listaViajesId = new ArrayList<String>();
        Log.d(TAG,"viajes>"+viajesRecursos);
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
        try {
            generarFechas(Util.convertStringToDate(txtFhInicio.getText().toString(), "00:00:00", "/"),
                    Util.convertStringToDate(txtFhFin.getText().toString(), "23:59:59", "/"));
            generarTotales(txtRecursoDesc.getText().toString());
        }
        catch(java.lang.NegativeArraySizeException nase){
            Toast.makeText(this.getContext(), "La fecha inicial deberá ser menor que la fecha final.", Toast.LENGTH_LONG).show();
            exito = false;
            txtFhFin.setText("");
            txtFhFin.requestFocus();

        }
/*        if(exito && Integer.parseInt(txtRecursoId.getText().toString()) <= 0){
            Toast.makeText(this.getContext(), "Favor de seleccionar un recurso.", Toast.LENGTH_LONG).show();

            exito = false;
            txtRecurso.requestFocus();
        }*/
        Log.d(TAG, "Terminando validarCampos()");
        return exito;


    }

    private void generarTotales(String recursoDesc){
        pescas = new int[dias.length];
        for(int i=0; i < diasCompletos.length; i++) {
            Date dFechaInicio = Util.convertStringToDate(diasCompletos[i],"00:00:00", "/");
            Date dFechaFin = Util.convertStringToDate(diasCompletos[i],"23:59:59", "/");
            if (!"".equals(recursoDesc)) {
                RealmResults<ViajeEntity> result= realm.where(ViajeEntity.class)
                        .equalTo("usuarioId",usuarioId)
                        .equalTo("pescadorId",pescadorId)
                        .between("viajeFhRegistro", dFechaInicio, dFechaFin)
                        .equalTo("viajesRecursos.recursoDescripcion", recursoDesc).findAll();
                int total = 0;
                for(ViajeEntity ve : result){
                    for(ViajeRecursoEntity vre: ve.getViajesRecursos()){
                        total += vre.getVeCaptura();
                    }
                }
                pescas[i] = total;
            }
            else {
                RealmResults<ViajeEntity> result= realm.where(ViajeEntity.class)
                        .equalTo("usuarioId",usuarioId)
                        .equalTo("pescadorId",pescadorId)
                        .between("viajeFhRegistro", dFechaInicio, dFechaFin)
                        .findAll();
                int total = 0;
                for(ViajeEntity ve : result){
                    for(ViajeRecursoEntity vre: ve.getViajesRecursos()){
                        total += vre.getVeCaptura();
                    }
                }
                pescas[i] = total;
            }
            List<String> listaViajesId = new ArrayList<String>();
        }
        totales= new int[dias.length];
        int acumulado = 0;
        for(int z=0; z < dias.length; z++){
            acumulado += pescas[z];
            totales[z] = acumulado;
        }
    }

    private void generarFechas(Date fhInicio, Date fhFin) throws java.lang.NegativeArraySizeException{
        long fhInicioMillis = fhInicio.getTime();
        long fhFinMillis = fhFin.getTime();
        int iDias =(int) ((fhFinMillis - fhInicioMillis) / (24 * 60 * 60 * 1000)) + 1;
//        Log.d(TAG,"Hay "+iDias+" de diferencia.");

        dias = new String[iDias];
        diasCompletos = new String[iDias];
        for(int i=0; i < iDias; i++){
            dias[i] = Util.convertDateToString(fhInicio,Constantes.formatoFechaSinAnio);
            diasCompletos[i] = Util.convertDateToString(fhInicio,Constantes.formatoFecha);
            Calendar cal = Calendar.getInstance();
            cal.setTime(fhInicio);
            cal.add(Calendar.DAY_OF_YEAR,1);
            fhInicio = cal.getTime();
        }
        Log.d(TAG,"Dias "+dias+"");


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

    //Caracteristicas comunes en las graficas
    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY, boolean leyenda){
        chart.getDescription().setText(description);
        chart.getDescription().setTextColor(textColor);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(background);
        chart.animateY(animateY);

        //Validar porque la grafica de radar y dispersion tiene dos datos especificos y la leyenda se crea de acuerdo a esos datos.
        if(leyenda)
            legend(chart);
        return chart;
    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        String[] months;
        for (int i = 0; i < dias.length; i++) {
            LegendEntry entry = new LegendEntry();
           //entry.formColor = colors[i];
            entry.label = dias[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry>getBarEntries(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < pescas.length; i++)
            entries.add(new BarEntry(i,pescas[i]));
        return entries;
    }

    private ArrayList<BarEntry>getBarEntries2(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < totales.length; i++)
            entries.add(new BarEntry(i,totales[i]));
        return entries;
    }

    private ArrayList<Entry> getLineEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < pescas.length; i++)
            entries.add(new Entry(i, pescas[i]));
        return entries;
    }

    private ArrayList<Entry> getLineEntries2() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < totales.length; i++)
            entries.add(new Entry(i, totales[i]));
        return entries;
    }

//Eje horizontal o eje X
    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(dias));
    }
    //Eje Vertical o eje Y lado izquierdo
    private void axisLeft(YAxis axis){
        float percent = 10;
        axis.setSpaceTop(percent);
        axis.setSpaceBottom(percent);
        axis.setDrawZeroLine(true); // draw a zero line
        axis.setZeroLineColor(Color.GRAY);
        axis.setZeroLineWidth(1.0f);
        //axis.setAxisMinimum(10);
        //axis.setGranularity(10);
        //axis.setAxisMaximum(100);
    }
    //Eje Vertical o eje Y lado Derecho
    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }

    //Crear graficas
    public void createCharts(){
        //BarChart
        barChart=(BarChart)getSameChart(barChart,"",Color.GRAY,Color.TRANSPARENT,3000,true);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(true);
        barChart.setData(getBarData());
        barChart.invalidate();
        barChart.getLegend().setEnabled(false);
        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());

        //LineChart
        lineChart = (LineChart) getSameChart(lineChart, "", Color.GRAY, Color.TRANSPARENT, 3000,true);
        lineChart.setData(getLineData());
        lineChart.invalidate();
        axisX(lineChart.getXAxis());
        axisLeft(lineChart.getAxisLeft());
        axisRight(lineChart.getAxisRight());


    }

    //Carasteristicas comunes en dataset
    private DataSet getDataSame(DataSet dataSet){
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarData(){
        BarDataSet barDataSet=(BarDataSet)getDataSame(new BarDataSet(getBarEntries(),""));
        barDataSet.setBarShadowColor(Color.TRANSPARENT);
        BarData barData=new BarData(barDataSet);
        barData.setBarWidth(1.0f);

        BarDataSet barDataSet2=(BarDataSet)getDataSame(new BarDataSet(getBarEntries2(),""));
        barDataSet2.setBarShadowColor(Color.TRANSPARENT);
        BarData barData2=new BarData(barDataSet2);
        barData2.setBarWidth(1.0f);

        ArrayList<IBarDataSet> sets = new ArrayList<>();
        if(chkReporteAcumulado.isChecked() == chkReportePorDia.isChecked()){
            sets.add(barDataSet);
            sets.add(barDataSet2);
        }
        else if(chkReportePorDia.isChecked()) {
            sets.add(barDataSet);
        }
        else if(chkReporteAcumulado.isChecked()) {
            sets.add(barDataSet2);
        }
        return new BarData(sets);
    }

    private LineData getLineData() {
        LineDataSet lineDataSet = (LineDataSet) getDataSame(new LineDataSet(getLineEntries(), ""));
        lineDataSet.setLineWidth(2.5f);
        //Color de los circulos de la grafica
        //lineDataSet.setCircleColors(colors);
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //lineDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        lineDataSet.setCircleColors(ColorTemplate.COLORFUL_COLORS);
        //Tamaño de los circulos de la grafica
        lineDataSet.setCircleRadius(5f);
        //Sombra grafica
        lineDataSet.setDrawFilled(true);
        //Estilo de la linea picos(linear) o curveada(cubic) cuadrada(Stepped)
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //Segunda fila
        LineDataSet d2 = (LineDataSet) getDataSame(new LineDataSet(getLineEntries2(), ""));
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColors(ColorTemplate.COLORFUL_COLORS);
        d2.setCircleColors(ColorTemplate.COLORFUL_COLORS);
        d2.setDrawValues(false);


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        if(chkReporteAcumulado.isChecked() == chkReportePorDia.isChecked()){
            sets.add(lineDataSet);
            sets.add(d2);
        }
        else if(chkReportePorDia.isChecked()) {
            sets.add(lineDataSet);
        }
        else if(chkReporteAcumulado.isChecked()) {
            sets.add(d2);
        }
        return new LineData(sets);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
