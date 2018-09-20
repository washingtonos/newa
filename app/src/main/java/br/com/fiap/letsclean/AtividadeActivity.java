package br.com.fiap.letsclean;

import android.app.ProgressDialog;
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
    String userId;
    private Long admUser,grupoId;
    private RecyclerView recyclerView;
    private Button btn_cadastrar_atividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);

        btn_cadastrar_atividade = findViewById(R.id.btn_cadastrar_atividade);
        formateFont(btn_cadastrar_atividade);

        //capturar extras de MenuActivity E Criando comodo
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId = extras.getString("userId");
            admUser = extras.getLong("admUser");
            grupoId = extras.getLong("grupoId");
        }

        btn_cadastrar_atividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AtividadeActivity.this,CadastrarAtividadeActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("grupoId", grupoId);
                startActivity(intent);
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
                URL url = new URL("http://www.letscleanof.com/api/atividade/usuario/"+userId);
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
                    List<Atividade> atividades = new ArrayList<>();
                    int index=0;
                    JSONArray jsonArray = new JSONArray(s);
                    for ( int i=0; i < jsonArray.length(); i++){

                        JSONObject atividObjc = (JSONObject) jsonArray.get(i);
                        // Recuperando valor da Atividade
                        Long cod = atividObjc.getLong("id");
                        String nome = atividObjc.getString("nome");
                        Long userId = atividObjc.getLong("userId");
                        Long grupoId = atividObjc.getLong("grupoId");
                        Long comodoId = atividObjc.getLong("comodoId");

                        // Instanciando um Comodo e add na lista
                        Atividade atividade =  new Atividade();
                        atividade.setId(cod);
                        atividade.setNome(nome);
                        atividade.setUserId(userId);
                        atividade.setGrupoId(grupoId);
                        atividade.setComodoId(comodoId);
                        atividades.add(atividade);
                    }

                    //creating recyclerview adapter
                    AtividadeAdapter adapter = new AtividadeAdapter(AtividadeActivity.this, (ArrayList<Atividade>) atividades);

                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(AtividadeActivity.this, "Erro ao carregar Atividades", Toast.LENGTH_LONG).show();
            }

        }
    }
}
