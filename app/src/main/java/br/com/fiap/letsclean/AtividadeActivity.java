package br.com.fiap.letsclean;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.letsclean.adapter.AtividadeAdapter;
import br.com.fiap.letsclean.entity.Atividade;

public class AtividadeActivity extends AppCompatActivity {

    //the recyclerview
    private Long admUser,grupoId,userId2;
    private RecyclerView recyclerView;
    private Button btn_cadastrar_atividade;
    private String nomeUser;
    private List<Atividade> atividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);

        btn_cadastrar_atividade = findViewById(R.id.btn_cadastrar_atividade);
        formateFont(btn_cadastrar_atividade);

        //capturar extras de MenuActivity
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId2 = extras.getLong("userId2");
            admUser = extras.getLong("admUser");
            grupoId = extras.getLong("grupoId");
            nomeUser = extras.getString("nomeUser");
        }

        btn_cadastrar_atividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grupoId != null){
                    if(grupoId != Long.valueOf(0)){
                        if(admUser != Long.valueOf(0)){
                            Intent intent = new Intent(AtividadeActivity.this,CadastrarAtividadeActivity.class);
                            intent.putExtra("userId2",userId2);
                            intent.putExtra("grupoId", grupoId);
                            intent.putExtra("admUser", admUser);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AtividadeActivity.this);
                            builder.setTitle("Atividade")
                                    .setMessage("Apenas o administrador do grupo pode criar atividades")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.show();
                        }

                    }
                    else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AtividadeActivity.this);
                        builder.setTitle("Atividade")
                                .setMessage("Para cadastrar uma Atividade, você precisa criar um grupo")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    }
                }

            }
        });

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AtividadeActivity.AtividadeTask task = new AtividadeActivity.AtividadeTask();
        task.execute();
    }

    private void formateFont(Button x){
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotoRegular.ttf");
        x.setTypeface(typeface);
    }

    private class AtividadeTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AtividadeActivity.this, "Aguarde..", "Carregando Atividades");
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://www.letscleanof.com/api/atividade/usuario/"+userId2);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                if(connection.getResponseCode()==200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder resposta = new StringBuilder();
                    String linha = "";

                    while((linha=reader.readLine())!=null){
                        resposta.append(linha);
                    }

                    connection.disconnect();
                    return resposta.toString();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s!=null){
                try {
                    atividades = new ArrayList<>();
                    int index=0;
                    JSONArray jsonArray = new JSONArray(s);
                    for ( int i=0; i < jsonArray.length(); i++){

                        JSONObject atividObjc = (JSONObject) jsonArray.get(i);
                        // Recuperando valor da Atividade
                        Long cod = atividObjc.getLong("id");
                        String nome = atividObjc.getString("nome");
                        String desc = atividObjc.getString("descricao");
                        Long userId = atividObjc.getLong("userId");
                        Long grupoId = atividObjc.getLong("grupoId");
                        Long comodoId = atividObjc.getLong("comodoId");
                        Long status  = atividObjc.getLong("status");

                        // Instanciando um Comodo e add na lista
                        Atividade atividade =  new Atividade();
                        atividade.setId(cod);
                        atividade.setNome(nome);
                        atividade.setDesc(desc);
                        atividade.setUserId(userId);
                        atividade.setGrupoId(grupoId);
                        atividade.setComodoId(comodoId);
                        atividade.setStatus(status);
                        atividade.setNomeResponsavel(nomeUser);
                        atividade.setAdmUser(admUser);
                        atividades.add(atividade);
                    }

                    if(!atividades.isEmpty()){
                        List<Atividade> atividadesPendentes = new ArrayList<>();
                        for ( int i=0; i < atividades.size(); i++){
                            if(atividades.get(i).getStatus() == Long.valueOf(0)){
                                //creating recyclerview adapter
                                atividadesPendentes.add(atividades.get(i));
                            }
                        }

                        AtividadeAdapter adapter = new AtividadeAdapter(AtividadeActivity.this, (ArrayList<Atividade>) atividadesPendentes);
                        //setting adapter to recyclerview
                        recyclerView.setAdapter(adapter);

                        // Fazer um Alert com todas  atividades concluidas
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AtividadeActivity.this, "Erro ao carregar Atividades", Toast.LENGTH_LONG).show();
            }

        }
    }
}
