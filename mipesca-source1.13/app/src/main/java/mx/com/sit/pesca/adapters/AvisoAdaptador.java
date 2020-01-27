package mx.com.sit.pesca.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;



import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.AvisoEntity;

import mx.com.sit.pesca.util.Constantes;
import mx.com.sit.pesca.util.Util;

public class AvisoAdaptador extends RecyclerView.Adapter<AvisoAdaptador.ViewHolder>{

    private RealmResults<AvisoEntity> avisos;
    private int layout;
    private AvisoAdaptador.OnItemClickListener itemClickListener;

    private Context context;

    public AvisoAdaptador(RealmResults<AvisoEntity> avisos, int layout, AvisoAdaptador.OnItemClickListener listener) {
        this.avisos = avisos;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public AvisoAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        AvisoAdaptador.ViewHolder vh = new AvisoAdaptador.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AvisoAdaptador.ViewHolder holder, int position) {
        holder.bind(avisos.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        if(avisos!=null){
            return avisos.size();
        }
        else{
            return 0;
        }
    }


    public void updateList(RealmResults<AvisoEntity> avisos){
        this.avisos = avisos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos UI a rellenar
        //public TextView textViewAvisoFolio;
        public TextView textViewAvisoEstatus;
        public TextView textViewAvisoSitio;
        public TextView textViewAvisoOfnaPesca;
        public TextView textViewAvisoPeriodo;
        public TextView textViewAvisoFhRegistro;
        public TextView textViewAvisoRecursos;

        public ViewHolder(View itemView) {
            super(itemView);
            //textViewAvisoFolio = (TextView) itemView.findViewById(R.id.txtAvisoFolio);
            textViewAvisoEstatus = (TextView) itemView.findViewById(R.id.txtAvisoEstatus);
            textViewAvisoSitio = (TextView) itemView.findViewById(R.id.txtAvisoSitio);
            textViewAvisoOfnaPesca = (TextView) itemView.findViewById(R.id.txtAvisoOfnaPesca);
            textViewAvisoPeriodo = (TextView) itemView.findViewById(R.id.txtAvisoPeriodo);
            textViewAvisoFhRegistro = (TextView) itemView.findViewById(R.id.txtAvisoFhRegistro);
            textViewAvisoRecursos = (TextView) itemView.findViewById(R.id.txtAvisoRecursos);
        }

        public void bind(final AvisoEntity aviso, final AvisoAdaptador.OnItemClickListener listener) {
            //textViewAvisoFolio.setText("" + aviso.getAvisoFolio());
            if(aviso.getAvisoEstatus()==0){
                textViewAvisoEstatus.setText("INICIO");
            }
            else if(aviso.getAvisoEstatus()==1){
                textViewAvisoEstatus.setText("SIN RECURSOS");
            }
            else if(aviso.getAvisoEstatus()==2){
                textViewAvisoEstatus.setText("FINALIZADO");
            }
            textViewAvisoSitio.setText("" + aviso.getSitioDescripcion());
            textViewAvisoOfnaPesca.setText("" + aviso.getOfnapescaDescripcion());
            textViewAvisoPeriodo.setText("" + Util.convertDateToString( aviso.getAvisoPeriodoInicio(), Constantes.formatoFecha) + " al " + Util.convertDateToString( aviso.getAvisoPeriodoFin(), Constantes.formatoFecha));
            textViewAvisoFhRegistro.setText( Util.convertDateToString( aviso.getAvisoFhRegistro(), Constantes.formatoFecha));
            textViewAvisoRecursos.setText("" + aviso.getAvisoViajes().size() +" recursos.");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(aviso, getAdapterPosition());
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los método/s a implementar
    public interface OnItemClickListener {
        void onItemClick(AvisoEntity aviso, int position);
    }
}
