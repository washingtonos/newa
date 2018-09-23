package br.com.fiap.letsclean.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.letsclean.AtividadeActivity;
import br.com.fiap.letsclean.R;
import br.com.fiap.letsclean.ValidarAtividadeActivity;
import br.com.fiap.letsclean.entity.Atividade;
import br.com.fiap.letsclean.service.ValidandoAtividadeTask;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeViewHolder>{

    private Context mContext;
    private List<Atividade> atividades = new ArrayList<>();
    private int position;


    public AtividadeAdapter(Context mContext, ArrayList<Atividade> atividades) {
        this.mContext = mContext;
        this.atividades = atividades;
    }

    @NonNull
    @Override
    public AtividadeAdapter.AtividadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_atividade_layout, parent,false);
        return new AtividadeAdapter.AtividadeViewHolder(view ,mContext, (ArrayList<Atividade>) atividades);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeAdapter.AtividadeViewHolder holder, int position) {
        //getting the group of the specified position
        Atividade  atividade = atividades.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(atividade.getNome());
        holder.txtViewDesc.setText(atividade.getDesc());
    }

    @Override
    public int getItemCount() {
        return atividades.size();
    }

    class  AtividadeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ArrayList<Atividade> atividades = new ArrayList<>();
        TextView textViewTitle, txtViewDesc;

        Context  mContext;
        public AtividadeViewHolder(@NonNull View itemView, Context mContext, ArrayList<Atividade> atividades) {
            super(itemView);
            this.atividades = atividades;
            this.mContext  = mContext;
            itemView.setOnClickListener(this);
            textViewTitle = itemView.findViewById(R.id.txtNome);
            txtViewDesc = itemView.findViewById(R.id.txtDesc);
        }

        @Override
        public void onClick(View v) {
            position  = getAdapterPosition();
            final Atividade atividade = this.atividades.get(position);
            if(atividade.getStatus() == Long.valueOf(0)){
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_atividade_detalhe);

                final TextView txtNome = dialog.findViewById(R.id.edtNomeAtividade);
                final TextView txtDesc = dialog.findViewById(R.id.edtDescAtividade);
                final TextView txtResponsavel = dialog.findViewById(R.id.edtResponsavelAtividade);
                final EditText edtObs = dialog.findViewById(R.id.edtObsAtividade);
                final Button btn_concluir = dialog.findViewById(R.id.btn_concluir_atividade);

                txtNome.setText(atividade.getNome());
                txtDesc.setText(atividade.getDesc());
                txtResponsavel.setText(atividade.getNomeResponsavel());
                atividade.setObs(edtObs.getText().toString());

                dialog.show();

                btn_concluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        atividade.setObs(edtObs.getText().toString());
                        //Parse da atividade
                        Atividade atv = new Atividade();
                        atv.setId(atividade.getId());
                        atv.setObs(atividade.getObs());

                        // Fazendo a atualização da atividade
                        AtividadeAdapter.AtualizarAtividadeTask task = new AtividadeAdapter.AtualizarAtividadeTask();
                        task.execute(atv);
                        dialog.dismiss();
                    }

                });
            }
            if(atividade.getStatus() == Long.valueOf(1)){
                // Atividades para serem validadas
                validarAtividade();
            }
        }

    }
    //Conclusão da ATIVIDADE
    private class AtualizarAtividadeTask extends AsyncTask<Atividade,Void,Integer> {

        @Override
        protected Integer doInBackground(Atividade... atividades) {
            try{
                URL url = new URL("http://www.letscleanof.com/api/atividade/status");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParamsAtividade = new JSONObject();
                jsonParamsAtividade.put("id",atividades[0].getId());
                jsonParamsAtividade.put("obs",atividades[0].getObs());

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonParamsAtividade.toString());
                osw.close();

                return connection.getResponseCode();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if(integer == 200){
                openDialog();
            } if (integer == 404){
                Toast.makeText(mContext, "Tente Novamente mais tarde", Toast.LENGTH_LONG).show();
            }
            if (integer == 500){
                Toast.makeText(mContext, "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Atividade")
                .setMessage("Concluida com Sucesso")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAtividade();
                    }
                });
        builder.show();
    }

    private void openAtividade() {
        Intent intent = new Intent(this.mContext , AtividadeActivity.class);
        intent.putExtra("userId2", atividades.get(position).getUserId());
        intent.putExtra("grupoId", atividades.get(position).getGrupoId());
        intent.putExtra("admUser", atividades.get(position).getAdmUser());
        this.mContext.startActivity(intent);

    }

    //Conclusão da validação da Ativação
    private void validarAtividade(){
        if(atividades.get(position).getUserId()  == atividades.get(position).getUserIdLOGADO()){
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Atividade")
                    .setIcon(R.drawable.alert)
                    .setMessage("'"+atividades.get(position).getNome().toString()+ "' não pode ser validada pelo proprietario")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
        if(atividades.get(position).getUserId()  != atividades.get(position).getUserIdLOGADO()){
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Atividade")
                    .setIcon(R.drawable.accept_approve_check_green_ok_yes_icon_256)
                    .setMessage("'"+atividades.get(position).getNome().toString()+ "' foi concluida?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                           // Fazer put validando a mensagem
                            ValidandoAtividadeTask task = new ValidandoAtividadeTask();
                            task.execute(atividades.get(position));
                            dialog.dismiss();
                            openValidacao();
                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                         }
            });

            builder.show();
        }
    }

    private void openValidacao(){
        Intent intent = new Intent(mContext, ValidarAtividadeActivity.class);
        intent.putExtra("grupoId", atividades.get(position).getGrupoId());
        intent.putExtra("userId2", atividades.get(position).getUserIdLOGADO());
        mContext.startActivity(intent);
    }

}
