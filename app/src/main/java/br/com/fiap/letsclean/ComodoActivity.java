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

import br.com.fiap.letsclean.adapter.ComodoAdapter;
import br.com.fiap.letsclean.entity.Comodo;

public class ComodoActivity extends AppCompatActivity {

    //the recyclerview
    String idUsuario;
    String idGrupo;
    RecyclerView recyclerView;
    Button btn_cadastrar_comodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comodo);

        btn_cadastrar_comodo = findViewById(R.id.btn_cadastrar_comodo);
        formateFont(btn_cadastrar_comodo);

        //capturar extras de MenuActivity E Criando grupo
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idUsuario = extras.getString("idUser");
            idGrupo = extras.getString("idGrupo");
        }

        btn_cadastrar_comodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComodoActivity.this,CadastrarComodoActivity.class);
                intent.putExtra("idUser",idUsuario);
                startActivity(intent);
            }
        });

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ComodoActivity.ComodoTask task = new ComodoActivity.ComodoTask();
        task.execute();
    }

    private class ComodoTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ComodoActivity.this, "Aguarde..", "Carregando Comodos");
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://www.letscleanof.com/api/comodos");
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
                    List<Comodo> comodos = new ArrayList<>();
                    int index=0;
                    JSONArray jsonArray = new JSONArray(s);
                    for ( int i=0; i < jsonArray.length(); i++){

                        JSONObject grupo = (JSONObject) jsonArray.get(i);
                        // Recuperando valor do Comodo
                        String cod = grupo.getString("id");
                        String nome = grupo.getString("nome");
                        String idGrupo = grupo.getString("id_grupo");
                        String idUsuario = grupo.getString("id_user");
                        // Instanciando um Comodo e add na lista
                        Comodo comodo = new Comodo();
                        comodo.setId(cod);
                        comodo.setNome(nome);
                        comodo.setIdGrupo(idGrupo);
                        comodo.setIdUsuario(idUsuario);
                        comodos.add(comodo);
                    }

                    //creating recyclerview adapter
                    ComodoAdapter adapter = new ComodoAdapter(ComodoActivity.this, (ArrayList<Comodo>) comodos);

                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ComodoActivity.this, "Erro ao carregar comodos", Toast.LENGTH_LONG).show();
            }

        }
    }
    private void formateFont(Button x){
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/robotoRegular.ttf");
        x.setTypeface(typeface);
    }
}
