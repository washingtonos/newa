package br.com.fiap.letsclean;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.letsclean.entity.Grupo;
import br.com.fiap.letsclean.entity.Usuario;

public class MenuActivity extends AppCompatActivity {

    private String userId, nomeUser;
    private Long admUser,grupoId, userId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Capturar id de Usuario  de activity anterior
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId = extras.getString("userId");
            userId2 = extras.getLong("userId2");
            admUser = extras.getLong("admUser");
            grupoId = extras.getLong("grupoId");
            nomeUser = extras.getString("nomeUser");
        }
    }

    public void group(View view) {
        MenuActivity.UsuarioTask taskUsuario = new MenuActivity.UsuarioTask();
        taskUsuario.execute();

        Intent intent = new Intent(this, GrupoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("grupoId", grupoId);
        startActivity(intent);
    }

    public void comodo(View view) {
        MenuActivity.UsuarioTask taskUsuario = new MenuActivity.UsuarioTask();
        taskUsuario.execute();

        Intent intent = new Intent(this, ComodoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userId2", userId2);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
        }
        startActivity(intent);
    }

    public void atividade(View view) {
        MenuActivity.UsuarioTask taskUsuario = new MenuActivity.UsuarioTask();
        taskUsuario.execute();

        Intent intent = new Intent(this, Atividade2Activity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("nomeUser", nomeUser);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
            intent.putExtra("userId2", userId2);
        }
        startActivity(intent);
    }

    public void relatorio(View view) {
        MenuActivity.UsuarioTask taskUsuario = new MenuActivity.UsuarioTask();
        taskUsuario.execute();

        Intent intent = new Intent(this, RelatorioActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("nomeUser", nomeUser);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
            intent.putExtra("userId2", userId2);
        }
        startActivity(intent);
    }

    private class GrupoTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://www.letscleanof.com/api/grupos/"+userId);
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

            if(s!=null && !s.equals("")){
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

                        grupoId = grup.getId();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }
    }

    private class UsuarioTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://letsclean.herokuapp.com/api/usuario/"+userId);
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
            JSONObject jsonResponse = null;
            if(s != null && !s.equals("")){
                try {

                    jsonResponse = new JSONObject(s);
                    Long admUser1 = jsonResponse.getLong("admUser");
                    Long idGroup = jsonResponse.getLong("grupoId");

                    Usuario user = new Usuario();
                    user.setAdmUser(admUser1);
                    user.setGrupoId(idGroup);

                    admUser = user.getAdmUser();
                    grupoId = user.getGrupoId();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
