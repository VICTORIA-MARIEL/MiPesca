package mx.com.sit.pesca.util;

public class Constantes {

    public static short ES_PRINCIPAL = 1;
    public static String formatoFecha = "dd/MM/yyyy";
    public static String formatoFechaSinAnio = "dd/MM";
    public static String formatoFechaHora = "yyyy-MM-dd hh:mm:ss";
    public static String formatoFechaSinc = "yyyy-MM-dd";
    public static int SELECT_MIN_CARACTERES = 3;
    public static String URL_SERVIDOR_REMOTO = "http://sgovela.mine.nu:8080/pe/externos/";
    //public static String URL_SERVIDOR_REMOTO_2 = "http://sgovela.mine.nu:8080/pe/operacion/";
    public static int MAXIMO_REGISTROS = 100;

    public static short VIAJE_ESTATUS_INICIAL = 0;
    public static short VIAJE_ESTATUS_SIN_FINALIZAR = 1;
    public static short VIAJE_ESTATUS_FINALIZADO = 2;
    public static short VIAJE_ESTATUS_DECLARADO = 3;

    public static short AVISO_ESTATUS_INICIAL = 0;
    public static short AVISO_ESTATUS_EN_VIAJE = 1;
    public static short AVISO_ESTATUS_FINALIZADO = 2;

    public static String RECURSO_NO_PIEZAS = "ALMEJA";

    public static short ESTATUS_SINCRONIZADO = 2;
    public static short ESTATUS_NO_SINCRONIZADO = 0;
    public static short ESTATUS_SINCRONIZANDO = 1;

    public static short JOB_CANCELADO = 0;
    public static short JOB_INICIADO = 1;


}
