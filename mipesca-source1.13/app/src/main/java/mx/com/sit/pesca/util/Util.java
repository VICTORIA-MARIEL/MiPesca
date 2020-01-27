package mx.com.sit.pesca.util;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import mx.com.sit.pesca.SplashActivity;
import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.CooperativaList;
import mx.com.sit.pesca.dto.OfnaPescaList;
import mx.com.sit.pesca.dto.PresentacionList;
import mx.com.sit.pesca.dto.RecursoList;
import mx.com.sit.pesca.services.ArtePescaService;
import mx.com.sit.pesca.services.ComunidadService;
import mx.com.sit.pesca.services.CooperativaService;
import mx.com.sit.pesca.services.OfnaPescaService;
import mx.com.sit.pesca.services.PresentacionService;
import mx.com.sit.pesca.services.RecursoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Util {
    private static final String TAG = "Util";





    public static String generarFolio(int pescadorId, int viajeId){
        String strViaje = String.format("%03d", viajeId);
        String strPescador = String.format("%03d", pescadorId);
        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);

        return "F"+strPescador + "" + strViaje;


    }

    /**
     * Metodo que cifra la contraseña ingresada
     */
    public static String md5(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae) {
            Log.e(TAG, nsae.getMessage());
        }
        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }


    /**
     * Metodo que obtiene el usuario como dato persistene
     */
    public static String getUserPrefs(SharedPreferences prefs){
        return prefs.getString("usuario","");
    }

    /**
     * Metodo que obtiene la contraseña como dato persistene
     */
    public static String getPasswordPrefs(SharedPreferences prefs){
        return prefs.getString("contrasena","");
    }

    /**
     * Metodo que obtiene la contraseña como dato persistene
     */
    public static boolean getRecordarPrefs(SharedPreferences prefs){
        return prefs.getBoolean("recordar",false);
    }


    /**
     * Metodo que obtiene la id de pescador como dato persistene
     */
    public static String getPescadorPrefs(SharedPreferences prefs){
        return prefs.getString("pescador","");
    }

    /**
     * Metodo que convierte una cadena en un objeto fecha
     */
    public static Date convertStringToDate(String fecha, String delimitador){
        try {
            return new SimpleDateFormat("dd" + delimitador + "MM" + delimitador + "yyyy").parse(fecha);
        }
        catch(ParseException pe){
            Log.e(TAG, pe.getMessage());
            return null;
        }
    }

    /**
     * Metodo que convierte una cadena en un objeto fecha
     */
    public static Date convertStringToDate(String fecha, String hora, String delimitador){
        try {
            return new SimpleDateFormat("dd" + delimitador + "MM" + delimitador + "yyyy HH:mm:ss").parse(fecha+" "+hora);
        }
        catch(ParseException pe){
            Log.e(TAG, pe.getMessage());
            return null;
        }
    }


    public static String getHoy()throws Exception{
        Date fecha = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return dt.format(fecha);
    }
    /**
     * Metodo que convierte un objeto de tipo date en una cadena.
     */
    public static String convertDateToString(Date fecha, String formato){
        DateFormat dateFormat = new SimpleDateFormat(formato);
        if(fecha!=null) {
            return dateFormat.format(fecha);
        }
        else{
            return "";
        }

    }


    public static String generarClaveConfirmacion(){
        String num1 = "" + ((int) (Math.random() * 9) + 1);
        Log.d(TAG, num1);
        String num2 = "" + ((int) (Math.random() * 9) + 1);
        Log.d(TAG, num2);
        String num3 = "" + ((int) (Math.random() * 9) + 1);
        Log.d(TAG, num3);
        String num4 = "" + ((int) (Math.random() * 9) + 1);
        Log.d(TAG, num4);
        return num1 + num2 + num3 + num4;
    }


    public static short calcularDuracion(Date dInicio, Date dFin){
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(dInicio);
        Calendar calFin= Calendar.getInstance();
        calFin.setTime(dFin);
        long duracionMillis = calFin.getTimeInMillis() - calInicio.getTimeInMillis();

        long duracion =duracionMillis / 1000 / 60 / 60 / 24 / 365;

        return (short)duracion;
    }

    public static short calcularDuracionDias(Date dInicio, Date dFin){
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(dInicio);
        Calendar calFin= Calendar.getInstance();
        calFin.setTime(dFin);
        long duracionMillis = calFin.getTimeInMillis() - calInicio.getTimeInMillis();

        long duracion =duracionMillis / 1000 / 60 / 60 / 24;

        return (short)duracion;
    }

}
