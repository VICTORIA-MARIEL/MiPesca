package mx.com.sit.pesca.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.AvisoViajeEntity;
import mx.com.sit.pesca.util.Constantes;

public class AvisoViajeAdaptador extends RecyclerView.Adapter<AvisoViajeAdaptador.ViewHolder>{

    private RealmResults<AvisoViajeEntity> avisoViajes;
    private int layout;
    private AvisoViajeAdaptador.OnItemClickListener itemClickListener;

    private Context context;

    public AvisoViajeAdaptador(RealmResults<AvisoViajeEntity> avisoViajes, int layout, AvisoViajeAdaptador.OnItemClickListener listener) {
        this.avisoViajes = avisoViajes;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public AvisoViajeAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        AvisoViajeAdaptador.ViewHolder vh = new AvisoViajeAdaptador.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AvisoViajeAdaptador.ViewHolder holder, int position) {
        holder.bind(avisoViajes.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return avisoViajes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos UI a rellenar
        public TextView textViewAvisoViajePresentacion;
        public TextView textViewAvisoViajeRecurso;
        public TextView textViewAvisoViajeCaptura;
        public TextView textViewAvisoViajePrecio;
        public TextView textViewAvisoViajeNoPiezas;
        public TextView lblViewAvisoViajeNoPiezas;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAvisoViajePresentacion = (TextView) itemView.findViewById(R.id.txtAvisoViajePresentacion);
            textViewAvisoViajeRecurso = (TextView) itemView.findViewById(R.id.txtAvisoViajeRecurso);
            textViewAvisoViajeCaptura = (TextView) itemView.findViewById(R.id.txtAvisoViajeCaptura);
            textViewAvisoViajePrecio = (TextView) itemView.findViewById(R.id.txtAvisoViajePrecio);
            textViewAvisoViajeNoPiezas = (TextView) itemView.findViewById(R.id.txtAvisoViajeNoPiezas);
            lblViewAvisoViajeNoPiezas = (TextView)itemView.findViewById(R.id.lblAvisoViajeNoPiezas);
        }

        public void bind(final AvisoViajeEntity avisoViaje, final AvisoViajeAdaptador.OnItemClickListener listener) {
            textViewAvisoViajeRecurso.setText("" + avisoViaje.getAvisoViajeRecurso());
            textViewAvisoViajePresentacion.setText("" + avisoViaje.getAvisoViajePresentacion());
            textViewAvisoViajeCaptura.setText("" + avisoViaje.getAvisoViajeCaptura() + "Kgs");
            textViewAvisoViajePrecio.setText("$" + avisoViaje.getAvisoViajePrecio());
            if(("" + avisoViaje.getAvisoViajeRecurso()).indexOf(Constantes.RECURSO_NO_PIEZAS) >= 0){
                lblViewAvisoViajeNoPiezas.setVisibility(View.VISIBLE);
            }
            else{
                lblViewAvisoViajeNoPiezas.setVisibility(View.INVISIBLE);
            }
            textViewAvisoViajeNoPiezas.setText("" + avisoViaje.getAvisoViajeNoPiezas() );
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(avisoViaje, getAdapterPosition());
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los método/s a implementar
    public interface OnItemClickListener {
        void onItemClick(AvisoViajeEntity avisoViaje, int position);
    }
}
