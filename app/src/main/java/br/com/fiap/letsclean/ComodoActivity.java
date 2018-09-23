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

import br.com.fiap.letsclean.adapter.ComodoAdapter;
import br.com.fiap.letsclean.entity.Comodo;

public class ComodoActivity extends AppCompatActivity {

    //the recyclerview
    private Long admUser,grupoId, userId2;
    private RecyclerView recyclerView;
    private Button btn_cadastrar_comodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comodo);

        btn_cadastrar_comodo = findViewById(R.id.btn_cadastrar_comodo);
        formateFont(btn_cadastrar_comodo);

        //capturar extras de MenuActivity E Criando comodo
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId2 = extras.getLong("userId2");
            admUser = extras.getLong("admUser");
            grupoId = extras.getLong("grupoId");
        }

        btn_cadastrar_comodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grupoId != null){
                    if(grupoId != Long.valueOf(0)){
                        Intent intent = new Intent(ComodoActivity.this,CadastrarComodoActivity.class);
                        intent.putExtra("userId2", userId2);
                        intent.putExtra("grupoId", grupoId);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ComodoActivity.this);
                        builder.setTitle("Comodo")
                                .setMessage("Para cadastrar um Comodo, você precisa criar um grupo")
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
                URL url = new URL("http://www.letscleanof.com/api/comodos/grupo/"+grupoId);
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

                        JSONObject comodoObjc = (JSONObject) jsonArray.get(i);
                        // Recuperando valor do Comodo
                        Long cod = comodoObjc.getLong("id");
                        String nome = comodoObjc.getString("nome");
                        Long idUsuario = comodoObjc.getLong("userId");
                        Long grupoId = comodoObjc.getLong("grupoId");
                        // Instanciando um Comodo e add na lista
                        Comodo comodo = new Comodo();
                        comodo.setId(cod);
                        comodo.setNome(nome);
                        comodo.setUserId(idUsuario);
                        comodo.setGrupoId(grupoId);
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
