package mx.com.sit.pesca.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.sit.pesca.dao.ArtePescaDAO;
import mx.com.sit.pesca.dao.AvisoDAO;
import mx.com.sit.pesca.dao.AvisoViajeDAO;
import mx.com.sit.pesca.dao.ComunidadDAO;
import mx.com.sit.pesca.dao.CooperativaDAO;
import mx.com.sit.pesca.dao.OfnaPescaDAO;
import mx.com.sit.pesca.dao.PermisoDAO;
import mx.com.sit.pesca.dao.PresentacionDAO;
import mx.com.sit.pesca.dao.RecursoDAO;
import mx.com.sit.pesca.dao.RegionDAO;
import mx.com.sit.pesca.dao.SincronizacionDAO;
import mx.com.sit.pesca.dao.SitioDAO;
import mx.com.sit.pesca.dao.ViajeDAO;
import mx.com.sit.pesca.dao.ViajeRecursoDAO;
import mx.com.sit.pesca.dto.ArtePescaList;
import mx.com.sit.pesca.dto.ComunidadList;
import mx.com.sit.pesca.dto.CooperativaList;
import mx.com.sit.pesca.dto.OfnaPescaList;
import mx.com.sit.pesca.dto.PresentacionList;
import mx.com.sit.pesca.dto.RecursoList;
import mx.com.sit.pesca.dto.ViajeList;
import mx.com.sit.pesca.entity.SincronizacionEntity;
import mx.com.sit.pesca.entity.UsuarioEntity;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.services.ArtePescaService;
import mx.com.sit.pesca.services.ComunidadService;
import mx.com.sit.pesca.services.CooperativaService;
import mx.com.sit.pesca.services.OfnaPescaService;
import mx.com.sit.pesca.services.PresentacionService;
import mx.com.sit.pesca.services.RecursoService;
import mx.com.sit.pesca.services.ViajeService;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class SincronizacionJOb extends JobService {
    private static final String TAG = "SincronizacionJOb";
    private boolean jobCancelled = false;


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);


        Log.d(TAG, "Job finished");
        jobFinished(params, true);
        return true;
    }


    private void doBackgroundWork(final JobParameters params) {
        Log.d(TAG, "Iniciando doBackgroundWork");

         /*new Thread(new Runnable() {
            @Override
            public void run() {*/

         int usuarioId = params.getJobId();
         Calendar cal = Calendar.getInstance();
         boolean exito = false;
         int diaHoy = cal.get(Calendar.DAY_OF_WEEK);
         int horaHoy = cal.get(Calendar.HOUR_OF_DAY);
        int minuteHoy = cal.get(Calendar.MINUTE);
         Realm realm = Realm.getDefaultInstance();
        SincronizacionEntity dto = realm.where(SincronizacionEntity.class).equalTo("sincronizacionUsrRegistro",usuarioId).findFirst();
        if(dto!=null) {
            exito = (("" + String.format("%02d", horaHoy) + ":" + minuteHoy).equals(dto.getSincronizacionHora())) ? true : false;
            Log.d(TAG, "Hora: " + (("" + String.format("%02d", horaHoy) + ":" + minuteHoy)));
            Log.d(TAG, "Hora Sincronizacion: " + dto.getSincronizacionHora());
            switch (diaHoy) {
                case Calendar.SUNDAY:
                    exito = (dto.getSincronizacionDomingo() == 1) ? exito : false;
                    break;
                case Calendar.MONDAY:
                    exito = (dto.getSincronizacionLunes() == 1) ? exito : false;
                    break;
                case Calendar.TUESDAY:
                    exito = (dto.getSincronizacionMartes() == 1) ? exito : false;
                    break;
                case Calendar.WEDNESDAY:
                    exito = (dto.getSincronizacionMiercoles() == 1) ? exito : false;
                    break;
                case Calendar.THURSDAY:
                    exito = (dto.getSincronizacionJueves() == 1) ? exito : false;
                    break;
                case Calendar.FRIDAY:
                    exito = (dto.getSincronizacionViernes() == 1) ? exito : false;
                    break;
                case Calendar.SATURDAY:
                    exito = (dto.getSincronizacionSabado() == 1) ? exito : false;
                    break;
            }
        }
        realm.close();

        if(exito) {
            ComunidadDAO comunidadDAO = new ComunidadDAO(usuarioId, null);
            comunidadDAO.consultarComunidades();
            CooperativaDAO cooperativaDAO = new CooperativaDAO(usuarioId, null);
            cooperativaDAO.consultarCooperativas();
            PresentacionDAO presDAO = new PresentacionDAO(usuarioId, null);
            presDAO.consultarPresentaciones();
            RecursoDAO recursoDAO = new RecursoDAO(usuarioId, null);
            recursoDAO.consultarRecursos();
            ArtePescaDAO artepescaDAO = new ArtePescaDAO(usuarioId, null);
            artepescaDAO.consultarArtesPesca();
            OfnaPescaDAO ofnapescaDAO = new OfnaPescaDAO(usuarioId, null);
            ofnapescaDAO.consultarOfnasPesca();
            RegionDAO regionDAO = new RegionDAO(usuarioId, null);
            regionDAO.consultarRegiones();
            PermisoDAO permisoDAO = new PermisoDAO(usuarioId, null);
            permisoDAO.consultarPermisos();
            SitioDAO sitioDAO = new SitioDAO(usuarioId, null);
            sitioDAO.consultarSitios();

            ViajeDAO viajeDAO = new ViajeDAO(usuarioId, null);
            viajeDAO.sincronizarViajes();
            ViajeRecursoDAO vrDAO = new ViajeRecursoDAO(usuarioId, null);
            vrDAO.sincronizarViajesRecursos();
            AvisoDAO avisoDAO = new AvisoDAO(usuarioId, null);
            avisoDAO.sincronizarAvisos();
            AvisoViajeDAO avisoViajeDAO = new AvisoViajeDAO(usuarioId, null);
            avisoViajeDAO.sincronizarAvisosViajes();
        }
       /*     }
        }).start();*/

        Log.d(TAG, "Terminando doBackgroundWork");

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
