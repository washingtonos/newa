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

import br.com.fiap.letsclean.Grupo_Detalhe;
import br.com.fiap.letsclean.R;
import br.com.fiap.letsclean.entity.Grupo;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder>{

    private Context mContext;
    private List<Grupo> grupos = new ArrayList<>();

    public GrupoAdapter(Context mContext, ArrayList<Grupo> grupos) {
        this.mContext = mContext;
        this.grupos = grupos;
    }

    @NonNull
    @Override
    public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_group_layout, parent,false);
        return new GrupoViewHolder(view ,mContext, (ArrayList<Grupo>) grupos);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoViewHolder holder, int position) {
        //getting the group of the specified position
        Grupo grupo = grupos.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(grupo.getNome());
        holder.textViewShortDesc.setText(grupo.getDescricao());

    }

    @Override
    public int getItemCount() {
        return grupos.size();
    }

    class  GrupoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ArrayList<Grupo> grupos = new ArrayList<>();
        TextView textViewTitle, textViewShortDesc;
        Context  mContext;
        public GrupoViewHolder(@NonNull View itemView, Context mContext, ArrayList<Grupo> grupos) {
            super(itemView);
            this.grupos = grupos;
            this.mContext  = mContext;
            itemView.setOnClickListener(this);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
        }

        @Override
        public void onClick(View v) {
            int position  = getAdapterPosition();
            Grupo grupo = this.grupos.get(position);
            Intent intent = new Intent(this.mContext , Grupo_Detalhe.class);
            intent.putExtra("id_grupo", grupo.getId());
            intent.putExtra("id_user", grupo.getIdUser());
            this.mContext.startActivity(intent);
        }
    }
}
