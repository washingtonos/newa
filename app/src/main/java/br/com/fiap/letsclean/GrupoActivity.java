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

import br.com.fiap.letsclean.adapter.GrupoAdapter;
import br.com.fiap.letsclean.entity.Grupo;

public class GrupoActivity extends AppCompatActivity {

    //the recyclerview
    String userId;
    Long admUser,grupoId;
    RecyclerView recyclerView;
    Button btn_cadastrar_grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);

        btn_cadastrar_grupo = findViewById(R.id.btn_cadastrar_grupo);
        formateFont(btn_cadastrar_grupo);

        //capturar extras de MenuActivity E Criando grupo
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId = extras.getString("userId");
            admUser = extras.getLong("admUser");
            grupoId = extras.getLong("grupoId");
        }

        btn_cadastrar_grupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grupoId != null){
                    if(grupoId == Long.valueOf(0)){
                        Intent intent = new Intent(GrupoActivity.this,CadastroGrupoActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                    }
                    else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(GrupoActivity.this);
                        builder.setTitle("Grupo")
                                .setMessage("Grupo já cadastrado")
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

        GrupoTask task = new GrupoTask();
        task.execute();
    }

    private void formateFont(Button x){
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotoRegular.ttf");
        x.setTypeface(typeface);
    }

    private class GrupoTask extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(GrupoActivity.this, "Aguarde..", "Carregando Grupos");
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://www.letscleanof.com/api/grupos/"+grupoId);
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
                    List<Grupo> grupos = new ArrayList<>();
                    int index=0;
                    JSONArray jsonArray = new JSONArray(s);
                    for ( int i=0; i < jsonArray.length(); i++){

                        JSONObject grupo = (JSONObject) jsonArray.get(i);
                        // Recuperando valor do grupo
                        Long cod = grupo.getLong("id");
                        String nome = grupo.getString("nome");
                        String desc = grupo.getString("descricao");
                        Long idUsuario = grupo.getLong("userId");
                        // Instanciando um Grupo e add na lista
                        Grupo grup = new Grupo();
                        grup.setId(cod);
                        grup.setNome(nome);
                        grup.setDescricao(desc);
                        grup.setUserId(idUsuario);
                        grupos.add(grup);
                    }

                    //creating recyclerview adapter
                    GrupoAdapter adapter = new GrupoAdapter(GrupoActivity.this, (ArrayList<Grupo>) grupos);

                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(GrupoActivity.this, "Erro ao carregar grupos", Toast.LENGTH_LONG).show();
            }

        }
    }
}
