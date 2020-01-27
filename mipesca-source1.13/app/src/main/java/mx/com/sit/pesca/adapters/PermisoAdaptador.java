package mx.com.sit.pesca.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.PermisoEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class PermisoAdaptador extends RecyclerView.Adapter<PermisoAdaptador.ViewHolder>{

    private RealmResults<PermisoEntity> permisos;
    private int layout;
    private PermisoAdaptador.OnItemClickListener itemClickListener;

    private Context context;

    public PermisoAdaptador(RealmResults<PermisoEntity> permisos, int layout, PermisoAdaptador.OnItemClickListener listener) {
        this.permisos = permisos;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public PermisoAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        PermisoAdaptador.ViewHolder vh = new PermisoAdaptador.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PermisoAdaptador.ViewHolder holder, int position) {
        holder.bind(permisos.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return permisos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos UI a rellenar
        public TextView textViewPermisoNumero;
        public TextView textViewPermisoParaPesqueria;
        public TextView textViewPermisoEstatus;
        public TextView textViewPermisoLugarExpedicion;
        public TextView textViewPermisoFhExpedicion;
        public TextView textViewPermisoFhVigenciaDuracion;
        public TextView textViewPermisoFhVigenciaInicio;
        public TextView textViewPermisoFhVigenciaFin;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewPermisoNumero = (TextView) itemView.findViewById(R.id.txtPermisoNumero);
            textViewPermisoParaPesqueria = (TextView) itemView.findViewById(R.id.txtPermisoParaPesqueria);
            textViewPermisoEstatus = (TextView) itemView.findViewById(R.id.txtPermisoEstatus);
            textViewPermisoLugarExpedicion = (TextView) itemView.findViewById(R.id.txtPermisoLugarExpedicion);
            textViewPermisoFhExpedicion = (TextView) itemView.findViewById(R.id.txtPermisoFhExpedicion);
            textViewPermisoFhVigenciaDuracion = (TextView) itemView.findViewById(R.id.txtPermisoFhVigenciaDuracion);
            textViewPermisoFhVigenciaInicio = (TextView) itemView.findViewById(R.id.txtPermisoFhVigenciaInicio);
            textViewPermisoFhVigenciaFin = (TextView) itemView.findViewById(R.id.txtPermisoFhVigenciaFin);
        }

        public void bind(final PermisoEntity permiso, final PermisoAdaptador.OnItemClickListener listener) {
            textViewPermisoNumero.setText("" + permiso.getPermisoNumero());
            textViewPermisoParaPesqueria.setText("" + permiso.getPermisoParaPesqueria());
            textViewPermisoEstatus.setText("" + ((permiso.getPermisoEstatus()==0)?"INACTIVO":"ACTIVO"));
            textViewPermisoLugarExpedicion.setText("" + permiso.getPermisoLugarExpedicion());
            if(permiso.getPermisoFhExpedicion() !=null ) {
                textViewPermisoFhExpedicion.setText("" + permiso.getPermisoFhExpedicion());
            }
            else{
                textViewPermisoFhExpedicion.setText(" ");
            }
            textViewPermisoFhVigenciaDuracion.setText("" + permiso.getPermisoFhVigenciaDuracion() + " años");
            textViewPermisoFhVigenciaInicio.setText( Util.convertDateToString( permiso.getPermisoFhVigenciaInicio(), Constantes.formatoFecha));
            textViewPermisoFhVigenciaFin.setText(Util.convertDateToString( permiso.getPermisoFhVigenciaFin(), Constantes.formatoFecha));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(permiso, getAdapterPosition());
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los método/s a implementar
    public interface OnItemClickListener {
        void onItemClick(PermisoEntity permiso, int position);
    }
}
