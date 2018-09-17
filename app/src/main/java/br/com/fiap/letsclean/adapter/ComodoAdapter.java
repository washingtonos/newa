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

import br.com.fiap.letsclean.R;
import br.com.fiap.letsclean.entity.Comodo;
import br.com.fiap.letsclean.ComodoDetalheActivity;

public class ComodoAdapter extends RecyclerView.Adapter<ComodoAdapter.ComodoViewHolder>{

    private Context mContext;
    private List<Comodo> comodos = new ArrayList<>();

    public ComodoAdapter(Context mContext, ArrayList<Comodo> comodos) {
        this.mContext = mContext;
        this.comodos = comodos;
    }

    @NonNull
    @Override
    public ComodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_comodo_layout, parent,false);
        return new ComodoViewHolder(view ,mContext, (ArrayList<Comodo>) comodos);
    }

    @Override
    public void onBindViewHolder(@NonNull ComodoViewHolder holder, int position) {
        //getting the group of the specified position
        Comodo  comodo = comodos.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(comodo.getNome());
    }

    @Override
    public int getItemCount() {
        return comodos.size();
    }

    class  ComodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ArrayList<Comodo> comodos = new ArrayList<>();
        TextView textViewTitle;
        Context  mContext;
        public ComodoViewHolder(@NonNull View itemView, Context mContext, ArrayList<Comodo> comodos) {
            super(itemView);
            this.comodos = comodos;
            this.mContext  = mContext;
            itemView.setOnClickListener(this);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }

        @Override
        public void onClick(View v) {
            int position  = getAdapterPosition();
            Comodo comodo = this.comodos.get(position);
            Intent intent = new Intent(this.mContext , ComodoDetalheActivity.class);
            intent.putExtra("id_comodo", comodo.getId());
            intent.putExtra("id", comodo.getIdUsuario());
            intent.putExtra("id_grupo", comodo.getIdGrupo());
            intent.putExtra("id_user", comodo.getIdGrupo());
            this.mContext.startActivity(intent);
        }
    }
}
