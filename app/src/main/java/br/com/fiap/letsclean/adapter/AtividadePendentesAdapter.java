package br.com.fiap.letsclean.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.letsclean.R;
import br.com.fiap.letsclean.entity.Atividade;

public class AtividadePendentesAdapter extends RecyclerView.Adapter<AtividadePendentesAdapter.AtividadeViewHolder>{

    private Context mContext;
    private List<Atividade> atividades = new ArrayList<>();
    private int position;


    public AtividadePendentesAdapter(Context mContext, ArrayList<Atividade> atividades) {
        this.mContext = mContext;
        this.atividades = atividades;
    }

    @NonNull
    @Override
    public AtividadePendentesAdapter.AtividadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_atividade_concluidas_layout, parent,false);
        return new AtividadePendentesAdapter.AtividadeViewHolder(view ,mContext, (ArrayList<Atividade>) atividades);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadePendentesAdapter.AtividadeViewHolder holder, int position) {
        //getting the group of the specified position
        Atividade  atividade = atividades.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(atividade.getNome());
        holder.txtViewDesc.setText(atividade.getDesc());
        if(atividade.getObs().equals("null")){
            holder.txtObs.setText("Atividade sem observações");
        }else {
            holder.txtObs.setText(atividade.getObs());
        }
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    class  AtividadeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ArrayList<Atividade> atividades = new ArrayList<>();
        TextView textViewTitle, txtViewDesc, txtObs;

        Context  mContext;
        public AtividadeViewHolder(@NonNull View itemView, Context mContext, ArrayList<Atividade> atividades) {
            super(itemView);
            this.atividades = atividades;
            this.mContext  = mContext;
            itemView.setOnClickListener(this);
            textViewTitle = itemView.findViewById(R.id.txtNome);
            txtViewDesc = itemView.findViewById(R.id.txtDesc);
            txtObs = itemView.findViewById(R.id.txtObs);
        }

        @Override
        public void onClick(View v) {
            position  = getAdapterPosition();
            final Atividade atividade = this.atividades.get(position);
        }
    }
}
