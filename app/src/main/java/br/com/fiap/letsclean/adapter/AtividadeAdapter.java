package br.com.fiap.letsclean.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.letsclean.ComodoDetalheActivity;
import br.com.fiap.letsclean.R;
import br.com.fiap.letsclean.entity.Atividade;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeViewHolder>{

    private Context mContext;
    private List<Atividade> atividades = new ArrayList<>();

    public AtividadeAdapter(Context mContext, ArrayList<Atividade> atividades) {
        this.mContext = mContext;
        this.atividades = atividades;
    }

    @NonNull
    @Override
    public AtividadeAdapter.AtividadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_comodo_layout, parent,false);
        return new AtividadeAdapter.AtividadeViewHolder(view ,mContext, (ArrayList<Atividade>) atividades);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeAdapter.AtividadeViewHolder holder, int position) {
        //getting the group of the specified position
        Atividade  atividade = atividades.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(atividade.getNome());
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    class  AtividadeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ArrayList<Atividade> atividades = new ArrayList<>();
        TextView textViewTitle;
        Context  mContext;
        public AtividadeViewHolder(@NonNull View itemView, Context mContext, ArrayList<Atividade> atividades) {
            super(itemView);
            this.atividades = atividades;
            this.mContext  = mContext;
            itemView.setOnClickListener(this);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }

        @Override
        public void onClick(View v) {
            int position  = getAdapterPosition();
            Atividade atividade = this.atividades.get(position);
            Intent intent = new Intent(this.mContext , ComodoDetalheActivity.class);
            intent.putExtra("comodoId", atividade.getId());
            intent.putExtra("userId", atividade.getUserId());
            intent.putExtra("grupoId", atividade.getGrupoId());
            this.mContext.startActivity(intent);
        }
    }
}