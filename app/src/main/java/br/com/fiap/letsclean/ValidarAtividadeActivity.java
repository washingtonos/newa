package br.com.fiap.letsclean;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ValidarAtividadeActivity extends AppCompatActivity {

    //the recyclerview
    private Long admUser,grupoId,userId2;
    private RecyclerView recyclerView;
    private String nomeUser;
    private List<Atividade> atividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_atividade);

        //capturar extras de MenuActivity
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId2 = extras.getLong("userId2");
            admUser = extras.getLong("admUser");
            grupoId = extras.getLong("grupoId");
            nomeUser = extras.getString("nomeUser");
        }


        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ValidarAtividadeActivity.AtividadeTask task = new ValidarAtividadeActivity.AtividadeTask();
        task.execute();
    }

    private class AtividadeTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ValidarAtividadeActivity.this, "Aguarde..", "Carregando Atividades");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://www.letscleanof.com/api/atividade/grupo/"+grupoId);
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
                        atividade.setAdmUser(admUser);
                        atividade.setUserIdLOGADO(userId2);
                        atividades.add(atividade);
                    }

                    if(!atividades.isEmpty()){
                        List<Atividade> atividadesPendentes = new ArrayList<>();
                        for ( int i=0; i < atividades.size(); i++){
                            if(atividades.get(i).getStatus() == Long.valueOf(1)){
                                //creating recyclerview adapter
                                atividadesPendentes.add(atividades.get(i));
                            }
                        }
                        AtividadeAdapter adapter = new AtividadeAdapter(ValidarAtividadeActivity.this, (ArrayList<Atividade>) atividadesPendentes);
                        //setting adapter to recyclerview
                        recyclerView.setAdapter(adapter);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ValidarAtividadeActivity.this, "Erro ao carregar Atividades", Toast.LENGTH_LONG).show();
            }

        }
    }
}
