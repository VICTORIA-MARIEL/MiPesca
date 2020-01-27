package mx.com.sit.pesca.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmResults;
import mx.com.sit.pesca.R;
import mx.com.sit.pesca.entity.ViajeRecursoEntity;
import mx.com.sit.pesca.util.Constantes;

public class ViajeRecursoAdaptador extends RecyclerView.Adapter<ViajeRecursoAdaptador.ViewHolder>{

    private RealmResults<ViajeRecursoEntity> viajeRecursos;
    private int layout;
    private ViajeRecursoAdaptador.OnItemClickListener itemClickListener;

    private Context context;

    public ViajeRecursoAdaptador(RealmResults<ViajeRecursoEntity> viajeRecursos, int layout, ViajeRecursoAdaptador.OnItemClickListener listener) {
        this.viajeRecursos = viajeRecursos;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    @Override
    public ViajeRecursoAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        ViajeRecursoAdaptador.ViewHolder vh = new ViajeRecursoAdaptador.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViajeRecursoAdaptador.ViewHolder holder, int position) {
        holder.bind(viajeRecursos.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return viajeRecursos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Elementos UI a rellenar
        public TextView textViewViajeRecurso;
        public TextView textViewViajePresentacion;
        public TextView textViewViajeCaptura;
        public TextView textViewViajePrecio;
        public TextView textViewViajeNoPiezas;
        public TextView lblViewViajeNoPiezas;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewViajeRecurso = (TextView) itemView.findViewById(R.id.txtViajeRecurso);
            textViewViajePresentacion = (TextView) itemView.findViewById(R.id.txtViajePresentacion);
            textViewViajeCaptura = (TextView) itemView.findViewById(R.id.txtViajeCaptura);
            textViewViajePrecio = (TextView) itemView.findViewById(R.id.txtViajePrecio);
            textViewViajeNoPiezas = (TextView) itemView.findViewById(R.id.txtViajeNoPiezas);
            lblViewViajeNoPiezas = (TextView)itemView.findViewById(R.id.lblViajeNoPiezas);
        }

        public void bind(final ViajeRecursoEntity viajeRecurso, final ViajeRecursoAdaptador.OnItemClickListener listener) {
            textViewViajeRecurso.setText("" + viajeRecurso.getRecursoDescripcion());
            textViewViajePresentacion.setText("" + viajeRecurso.getPresentacionDescripcion());
            textViewViajeCaptura.setText("" + viajeRecurso.getVeCaptura() + "Kgs");
            textViewViajePrecio.setText("$" + viajeRecurso.getVePrecio());
            if(("" + viajeRecurso.getRecursoDescripcion()).indexOf(Constantes.RECURSO_NO_PIEZAS) >= 0){
                lblViewViajeNoPiezas.setVisibility(View.VISIBLE);
                textViewViajeNoPiezas.setVisibility(View.VISIBLE);
            }
            else{
                lblViewViajeNoPiezas.setVisibility(View.INVISIBLE);
                textViewViajeNoPiezas.setVisibility(View.INVISIBLE);
            }
            textViewViajeNoPiezas.setText("" + viajeRecurso.getVeNoPiezas() );

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(viajeRecurso, getAdapterPosition());
                }
            });
        }
    }

    // Declaramos nuestra interfaz con el/los método/s a implementar
    public interface OnItemClickListener {
        void onItemClick(ViajeRecursoEntity viajeRecurso, int position);
    }
}
