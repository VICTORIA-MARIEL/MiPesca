package mx.com.sit.pesca.fragmentos;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.AvisoEntity;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class AvisoPDFFragment extends Fragment {
    private static final String TAG = "AvisoPDFFragment";
    private SharedPreferences prefs;
    private Realm realm;
    private int usuarioId;
    private int pescadorId;
    private PDFView pdfView;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private int avisoId;
    private String path;
    DownloadFile df = new DownloadFile();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Iniciando onCreateView");
        View view = inflater.inflate(R.layout.fragment_aviso_pdf, container, false);
        prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExists();
        Bundle data = this.getArguments();
        if(data != null) {
            avisoId = data.getInt("avisoId",0);
        }
        realm = Realm.getDefaultInstance();
        UsuarioEntity usuarioEntity = realm.where(UsuarioEntity.class).equalTo("id", usuarioId).findFirst();
        AvisoEntity aviso = realm.where(AvisoEntity.class).equalTo("id", avisoId).findFirst();
        PermisoEntity permiso = realm.where(PermisoEntity.class).equalTo("id",aviso.getPermisoId()).findFirst();
        List<AvisoViajeEntity> viajeRecursos = realm.where(AvisoViajeEntity.class).equalTo("avisoId", avisoId).findAll();

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                //String url = "http://sgovela.mine.nu:8080/sisdoc/contratacion/generarContratoCarta.html?candidatoId=691";
                String params = llenarParametros(aviso, permiso,viajeRecursos, usuarioEntity.getUsuarioLogin());
                if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                    String url = Constantes.URL_SERVIDOR_REMOTO + "generarAviso.html?" + params;
                    df.execute(url);
                }
                else{
                    Toast.makeText(getContext(), "Error: Para generar el aviso de arribo se requiere conexión a internet." , Toast.LENGTH_LONG).show();
                }

            } else {
                requestPermission(); // Code for permission
            }
        }
        else {
            //String url = "http://sgovela.mine.nu:8080/sisdoc/contratacion/generarContratoCarta.html?candidatoId=691";
            String params = llenarParametros(aviso, permiso,viajeRecursos, usuarioEntity.getUsuarioLogin());
            if (networkInfo != null && networkInfo.isConnected() && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                String url = Constantes.URL_SERVIDOR_REMOTO+"generarAviso.html?" + params;
                df.execute(url);
            }
            else{
                Toast.makeText(getContext(), "Error: Para generar el aviso de arribo se requiere conexión a internet." , Toast.LENGTH_LONG).show();
            }

        }


        ImageView btnLocal = view.findViewById(R.id.imgDescargarLocal);
        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(df.isDownloaded) {
                    path = df.path;
                    Log.d(TAG,"Path> "+path);
                    viewPDF();

                }
                else{
                    Log.d(TAG,"El archivo no se ha descargado todavia.");

                }
            }
        });

        ImageView btnExterno = view.findViewById(R.id.imgDescargarExterno);
        btnExterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(df.isDownloaded) {
                    path = df.path;
                    Log.d(TAG,"Path> "+path);
                    appPDF();

                }
                else{
                    Log.d(TAG,"El archivo no se ha descargado todavia.");

                }
            }
        });

        ImageView btnWhatsapp = view.findViewById(R.id.imgWhatsapp);
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(df.isDownloaded) {
                    path = df.path;
                    Log.d(TAG,"Path> "+path);
                    whatsappPDF();

                }
                else{
                    Log.d(TAG,"El archivo no se ha descargado todavia.");

                }


            }
        });

        ImageView btnEmail = view.findViewById(R.id.imgEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(df.isDownloaded) {
                    path = df.path;
                    Log.d(TAG,"Path> "+path);
                    emailPDF();

                }
                else{
                    Log.d(TAG,"El archivo no se ha descargado todavia.");

                }

            }
        });

        ImageView btnOutlook = view.findViewById(R.id.imgOutlook);
        btnOutlook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(df.isDownloaded) {
                    path = df.path;
                    Log.d(TAG,"Path> "+path);
                    outlookPDF();

                }
                else{
                    Log.d(TAG,"El archivo no se ha descargado todavia.");

                }

            }
        });

        ImageView btnOtros = view.findViewById(R.id.imgOtros);
        btnOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(df.isDownloaded) {
                    path = df.path;
                    Log.d(TAG,"Path> "+path);
                    otrosPDF();

                }
                else{
                    Log.d(TAG,"El archivo no se ha descargado todavia.");

                }

            }
        });

        realm.close();
        Log.d(TAG, "Terminando onCreateView");
        return view;
    }



    private String llenarParametros(AvisoEntity aviso, PermisoEntity permiso, List<AvisoViajeEntity> avisoViajes, String usuarioLogin){

        StringBuilder sb = new StringBuilder();
        try{
                sb.append("aviso.avisoId=" + URLEncoder.encode("" + aviso.getId(), "utf-8"));
                sb.append("&aviso.pescador.pescadorId=" + URLEncoder.encode("" + aviso.getPescadorId(), "utf-8"));
                sb.append("&aviso.avisoUsrRegistro.usuarioId=" + URLEncoder.encode("" + aviso.getUsuarioId(), "utf-8"));
                sb.append("&aviso.avisoFhRegistroStr=" + URLEncoder.encode(Util.convertDateToString(aviso.getAvisoFhRegistro(), Constantes.formatoFechaHora), "utf-8"));
                sb.append("&aviso.avisoFhSolicitudStr=" + URLEncoder.encode(Util.convertDateToString(aviso.getAvisoFhSolicitud(), Constantes.formatoFechaHora), "utf-8"));
                sb.append("&aviso.avisoPeriodoInicioStr=" + URLEncoder.encode(Util.convertDateToString(aviso.getAvisoPeriodoInicio(), Constantes.formatoFechaSinc), "utf-8"));
                sb.append("&aviso.avisoPeriodoFinStr=" + URLEncoder.encode(Util.convertDateToString(aviso.getAvisoPeriodoFin(), Constantes.formatoFechaSinc), "utf-8"));
                sb.append("&aviso.avisoDuracion=" + URLEncoder.encode("" + aviso.getAvisoDuracion(), "utf-8"));
                sb.append("&aviso.avisoDiasEfectivos=" + URLEncoder.encode("" + aviso.getAvisoDiasEfectivos(), "utf-8"));
                sb.append("&aviso.avisoFolio=" + URLEncoder.encode(aviso.getAvisoFolio(), "utf-8"));
                sb.append("&aviso.avisoEstatus=" + URLEncoder.encode("" + aviso.getAvisoEstatus(), "utf-8"));
                sb.append("&aviso.permiso.permisoNumero=" + URLEncoder.encode("" + aviso.getPermisoNumero(), "utf-8"));
                sb.append("&aviso.avisoZonaPesca=" + URLEncoder.encode(aviso.getAvisoZonaPesca(), "utf-8"));
                sb.append("&aviso.ofnapesca.ofnapescaId=" + URLEncoder.encode("" + aviso.getOfnapescaId(), "utf-8"));
                sb.append("&aviso.avisoEsPesqueriaAcuacultural=" + URLEncoder.encode("" + aviso.getAvisoEsPesqueriaAcuacultural(), "utf-8"));
                sb.append("&aviso.sitio.sitioId=" + URLEncoder.encode("" + aviso.getSitioId(), "utf-8"));
                sb.append("&aviso.usuarioLogin=" + URLEncoder.encode("" + usuarioLogin, "utf-8"));


                for(int i=0; i < avisoViajes.size(); i++) {
                    AvisoViajeEntity vre = avisoViajes.get(i);
                    sb.append("&viajeRecursoId"+(i+1)+"=" + URLEncoder.encode("" + vre.getViajeRecursoId(), "utf-8"));
                    sb.append("&viajeRecursoDescripcion"+(i+1)+"=" + URLEncoder.encode(vre.getAvisoViajeRecurso(), "utf-8"));
                    sb.append("&viajeCaptura"+(i+1)+"=" + URLEncoder.encode("" + vre.getAvisoViajeCaptura(), "utf-8"));
                    sb.append("&viajePrecio"+(i+1)+"=" + URLEncoder.encode("" + vre.getAvisoViajePrecio(), "utf-8"));
                    try {
                        if(permiso!=null) {
                            sb.append("&permisoVigencia" + (i + 1) + "=" + URLEncoder.encode(Util.convertDateToString(permiso.getPermisoFhVigenciaFin(), Constantes.formatoFechaSinc), "utf-8"));
                        }
                        else{
                            sb.append("&permisoVigencia" + (i + 1) + "=" + URLEncoder.encode("0", "utf-8"));
                        }
                    }
                    catch(NullPointerException npe){
                        sb.append("&permisoVigencia" + (i + 1) + "=" + URLEncoder.encode("2019-08-26", "utf-8"));
                    }
                    try {
                        if(permiso!=null) {
                            sb.append("&permisoFhExp"+(i+1)+"=" + URLEncoder.encode(permiso.getPermisoFhExpedicion(), "utf-8"));
                        }
                        else{
                            sb.append("&permisoFhExp"+(i+1)+"=" + URLEncoder.encode("0", "utf-8"));
                        }
                    }
                    catch(NullPointerException npe){
                            sb.append("&permisoFhExp" + (i + 1) + "=" + URLEncoder.encode("2019-08-26", "utf-8"));
                    }
                    try {
                        if(permiso!=null) {
                            sb.append("&permisoNum" + (i + 1) + "=" + URLEncoder.encode(permiso.getPermisoNumero(), "utf-8"));
                        }
                        else{
                            sb.append("&permisoNum" + (i + 1) + "=" + URLEncoder.encode(aviso.getPermisoNumero(), "utf-8"));
                        }
                    }
                    catch(NullPointerException npe){
                        sb.append("&permisoNum"+(i+1)+"=" + URLEncoder.encode("", "utf-8"));
                    }
                }
        }
        catch(UnsupportedEncodingException uee){
            Log.e(TAG,"Error: " + uee.getMessage());
        }
        return sb.toString();


    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        public String path;
        public boolean isDownloaded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(getContext());
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
//                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                fileName = "aviso.pdf";
                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                //folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
                folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "mipesca/";
                //folder = Environment.getDownloadCacheDirectory() + File.separator + "mipesca/";
                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                return "Exito: Archivo descargado en: " + folder + fileName;

            }
            catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Error: Servicio no disponible.";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();
            isDownloaded = true;
            path = folder + fileName;
            // Display File path after downloading
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }



    public void viewPDF(){
        Log.d(TAG, "Iniciando viewPDF");
        AvisoResultadoFragment fragmentoNvo = new AvisoResultadoFragment();
        Bundle data = new Bundle();
        data.putString("path", path);
        fragmentoNvo.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentoNvo).commit();
        Log.d(TAG, "Terminando viewPDF");
    }



    private void appPDF(){
        Log.d(TAG, "Iniciando appPDF");
//        if(avisoPDF.pdfFile.exists()){
       // System.out.println("PAth>" + path);
            Uri uri = Uri.fromFile(new File(path));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri,"application/pdf");
            try {
                getActivity().startActivity(intent);
            }
            catch(ActivityNotFoundException acfe){
                try {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
                    Toast.makeText(getContext(), "No cuentas con alguna aplicación para visualizar el PDF.", Toast.LENGTH_LONG).show();
                }
                catch(android.content.ActivityNotFoundException anfe){
                    viewInBrowser(getContext(), "https://play.google.com/store/apps/details?id=com.adobe.reader");
                }
            }
            catch(Exception e){
                Toast.makeText(getContext(), "No cuentas con alguna aplicación para visualizar el PDF.", Toast.LENGTH_LONG).show();
            }
 /*       }
        else{
            Toast.makeText(getContext(), "El archivo no se encontro..", Toast.LENGTH_LONG).show();
        }
        */
        Log.d(TAG, "Terminando appPDF");
    }

    private void whatsappPDF(){
        Log.d(TAG, "Iniciando whatsappPDF");
 //       if(avisoPDF.pdfFile.exists()){

            Uri uri = Uri.fromFile(new File(path));
//          Uri uri = Uri.fromFile(avisoPDF.pdfFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Acuse de arribo.");
            //intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            intent.setPackage("com.whatsapp");
            try {
                getActivity().startActivity(intent);
            }
            catch(ActivityNotFoundException acfe){
                try {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp")));
                    Toast.makeText(getContext(), "No se encuentra instalado whataspp.", Toast.LENGTH_LONG).show();
                }
                catch(android.content.ActivityNotFoundException anfe){
                    viewInBrowser(getContext(), "https://play.google.com/store/apps/details?id=com.whatsapp");
                }
            }
            catch(Exception e){
                Toast.makeText(getContext(), "No se encuentra instalado whataspp.", Toast.LENGTH_LONG).show();
            }
 /*       }
        else{
            Toast.makeText(getContext(), "El archivo no se encontro..", Toast.LENGTH_LONG).show();
        }
*/
        Log.d(TAG, "Terminando whatsappPDF");
    }

    private void emailPDF(){
        Log.d(TAG, "Iniciando emailPDF");
//        if(avisoPDF.pdfFile.exists()){
            Uri uri = Uri.fromFile(new File(path));
            //Uri uri = Uri.fromFile(avisoPDF.pdfFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Acuse de arribo.");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Acuse de arribo");
//            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            intent.setPackage("com.google.android.gm");
            try {
                getActivity().startActivity(intent);
            }
            catch(ActivityNotFoundException acfe){
                try {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gm")));
                    Toast.makeText(getContext(), "No se encuentra instalado Gmail.", Toast.LENGTH_LONG).show();
                }
                catch(android.content.ActivityNotFoundException anfe){
                    viewInBrowser(getContext(), "https://play.google.com/store/apps/details?id=com.google.android.gm");
                }
            }
            catch(Exception e){
                Toast.makeText(getContext(), "No se encuentra instalado Gmail.", Toast.LENGTH_LONG).show();
            }
/*        }
        else{
            Toast.makeText(getContext(), "El archivo no se encontro..", Toast.LENGTH_LONG).show();
        }
*/
        Log.d(TAG, "Terminando emailPDF");
    }

    private void outlookPDF(){
        Log.d(TAG, "Iniciando outlookPDF");
//        if(avisoPDF.pdfFile.exists()){
            Uri uri = Uri.fromFile(new File(path));
            //Uri uri = Uri.fromFile(avisoPDF.pdfFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Acuse de arribo.");
            //intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Acuse de arribo");
//            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.setType("application/pdf");
            intent.setPackage("com.microsoft.office.outlook");
            try {
                getActivity().startActivity(intent);
            }
            catch(ActivityNotFoundException acfe){
                try {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.microsoft.office.outlook")));
                    Toast.makeText(getContext(), "No se encuentra instalado Outlook.", Toast.LENGTH_LONG).show();
                }
                catch(android.content.ActivityNotFoundException anfe){
                    viewInBrowser(getContext(), "https://play.google.com/store/apps/details?id=com.microsoft.office.outlook");
                }
            }
            catch(Exception e){
                Toast.makeText(getContext(), "No se encuentra instalado outlook.", Toast.LENGTH_LONG).show();
            }
 /*       }
        else{
            Toast.makeText(getContext(), "El archivo no se encontro..", Toast.LENGTH_LONG).show();
        }
*/
        Log.d(TAG, "Terminando outlookPDF");
    }

    private void otrosPDF(){
        Log.d(TAG, "Iniciando otrosPDF");
//        if(avisoPDF.pdfFile.exists()){
            Uri uri = Uri.fromFile(new File(path));
            //Uri uri = Uri.fromFile(avisoPDF.pdfFile);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Acuse de arribo.");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            try {
                getActivity().startActivity(intent);
            }
            catch(ActivityNotFoundException acfe){
                Toast.makeText(getContext(), "No se encuentra alguna aplicación para compartir.", Toast.LENGTH_LONG).show();
            }
            catch(Exception e){
                Toast.makeText(getContext(), "No se encuentra alguna aplicación para compartir.", Toast.LENGTH_LONG).show();
            }
 /*       }
        else{
            Toast.makeText(getContext(), "El archivo no se encontro..", Toast.LENGTH_LONG).show();
        }
*/
        Log.d(TAG, "Terminando otrosPDF");
    }


    public static void viewInBrowser(Context context, String url) {
        Log.d(TAG, "Iniciando viewInBrowser");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (null != intent.resolveActivity(context.getPackageManager())) {
            context.startActivity(intent);
        }
        Log.d(TAG, "Terminando viewInBrowser");
    }

    private boolean checkPermission() {
        Log.d(TAG, "Iniciando checkPermission");
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Terminando checkPermission");
            return true;
        } else {
            Log.d(TAG, "Terminando checkPermission");
            return false;
        }
    }

    private void requestPermission() {
        Log.d(TAG, "Iniciando requestPermission");
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "El permiso de escritura nos permite guardar los documentos generados en la aplicación. Por favor, acepte este permiso en la sección Configuración->Aplicación de su teléfono o tablet", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
        Log.d(TAG, "Terminando requestPermission");
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
