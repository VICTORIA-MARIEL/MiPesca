package mx.com.sit.pesca.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.ViajeEntity;
import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;


public class ViajeAdaptador extends RecyclerView.Adapter<ViajeAdaptador.ViewHolder>{

    private RealmResults<ViajeEntity> viajes;
    private int layout;
    private ViajeAdaptador.OnItemClickListener itemClickListener;

    private Context context;

    public ViajeAdaptador(RealmResults<ViajeEntity> viajes, int layout, ViajeAdaptador.OnItemClickListener listener) {
        this.viajes = viajes;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public ViajeAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            context = parent.getContext();
            ViajeAdaptador.ViewHolder vh = new ViajeAdaptador.ViewHolder(v);
            return vh;
    }

    @Override
    public void onBindViewHolder(ViajeAdaptador.ViewHolder holder, int position) {
        holder.bind(viajes.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return viajes.size();
    }

    public void updateList(RealmResults<ViajeEntity> viajes){
        this.viajes = viajes;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos UI a rellenar
        public TextView textViewViajeFolio;
        public TextView textViewViajeEstatus;
        public TextView textViewViajeComunidad;
        public TextView textViewViajeArtePesca;
        public TextView textViewViajeCombustible;
        public TextView textViewViajeFhRegistro;
        public TextView textViewViajeRecursos;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewViajeFolio = (TextView) itemView.findViewById(R.id.txtViajeFolio);
            textViewViajeEstatus = (TextView) itemView.findViewById(R.id.txtViajeEstatus);
            textViewViajeComunidad = (TextView) itemView.findViewById(R.id.txtViajeComunidad);
            textViewViajeArtePesca = (TextView) itemView.findViewById(R.id.txtViajeArtePesca);
            textViewViajeCombustible = (TextView) itemView.findViewById(R.id.txtViajeCombustible);
            textViewViajeFhRegistro = (TextView) itemView.findViewById(R.id.txtViajeFhRegistro);
            textViewViajeRecursos = (TextView) itemView.findViewById(R.id.txtViajeRecursos);
        }

        public void bind(final ViajeEntity viaje, final ViajeAdaptador.OnItemClickListener listener) {
            textViewViajeFolio.setText("" + viaje.getViajeFolio());
            if(viaje.getViajeEstatus()==0){
                textViewViajeEstatus.setText("INICIO");
            }
            else if(viaje.getViajeEstatus()==1){
                textViewViajeEstatus.setText("SIN RECURSOS");
            }
            else if(viaje.getViajeEstatus()==2){
                textViewViajeEstatus.setText("FINALIZADO");
            }
            else if(viaje.getViajeEstatus()==3){
                textViewViajeEstatus.setText("DECLARADO");
            }

            textViewViajeComunidad.setText("" + viaje.getComunidadDescripcion());
            textViewViajeArtePesca.setText("" + viaje.getArtepescaDescripcion());
            textViewViajeCombustible.setText("" + viaje.getCombustible() + "lts");
            textViewViajeFhRegistro.setText( Util.convertDateToString( viaje.getViajeFhRegistro(), Constantes.formatoFecha));
            textViewViajeRecursos.setText("" + viaje.getViajesRecursos().size() + " Recursos");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(viaje, getAdapterPosition());
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los método/s a implementar
    public interface OnItemClickListener {
        void onItemClick(ViajeEntity viaje, int position);
    }
}
