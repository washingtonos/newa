package br.com.fiap.letsclean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.fiap.letsclean.entity.Usuario;

public class LoginActivity extends Activity {


    private TextInputLayout txt_input_emailLogin, txt_input_senhaLogin;
    private boolean userEtIsEmpty;
    private boolean passwordEtIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_input_emailLogin = findViewById(R.id.txt_input_emailLogin);
        txt_input_senhaLogin = findViewById(R.id.txt_input_senhaLogin);

        loadSharedPreferences();
    }



    public void registrar(View view) {
        Intent intent = new Intent(LoginActivity.this,RegistrarActivity.class);
        startActivity(intent);
    }

    public void doLogin(View view) {
        if(validateEmailAndSenha()){
            ValidateLogin validateLogin = new ValidateLogin();
            Usuario usuario = new Usuario();

            usuario.setEmail(txt_input_emailLogin.getEditText().getText().toString());
            usuario.setSenha(txt_input_senhaLogin.getEditText().getText().toString());
            userEtIsEmpty=false;
            passwordEtIsEmpty=false;
            validateLogin.execute(usuario.getEmail());

        }
    }

    private class ValidateLogin extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this,getString(R.string.aguarde),getString(R.string.realizando_login));
        }

        @Override
        protected String doInBackground(String... strings) {
            String email = txt_input_emailLogin.getEditText().getText().toString();
            try{
                URL url = new URL("http://www.letscleanof.com/api/usuario/email/" + email);
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
            JSONObject jsonResponse = null;
            if(s != null && !s.equals("")){
                try {

                    jsonResponse = new JSONObject(s);
                        // Recuperando valor do usuario
                        Long cod = jsonResponse.getLong("id");
                        String nome = jsonResponse.getString("nome");
                        String email = jsonResponse.getString("email");
                        String senha = jsonResponse.getString("senha");
                        Long admUser = jsonResponse.getLong("admUser");
                        Long grupoId = jsonResponse.getLong("grupoId");
                        // Instanciando um Grupo e add na lista
                        Usuario us = new Usuario();
                        us.setId(cod);
                        us.setNome(nome);
                        us.setEmail(email);
                        us.setSenha(senha);
                        us.setAdmUser(admUser);
                        us.setGrupoId(grupoId);

                        validarUser(us);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Erro ao encontrar usuario", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void validarUser(Usuario us) {
        if(us != null){

            if(txt_input_emailLogin.getEditText().getText().toString().equals(us.getEmail()) && txt_input_senhaLogin.getEditText().getText().toString().equals(us.getSenha())){
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                intent.putExtra("nomeUser",us.getNome().toString());
                intent.putExtra("userId", us.getId().toString());
                intent.putExtra("userId2", us.getId());
                if(us.getGrupoId() != null){
                    intent.putExtra("admUser", us.getAdmUser());
                    intent.putExtra("grupoId", us.getGrupoId());
                }
                startActivity(intent);
                finish();

            } else {
                final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Login");
                alert.setMessage("Email ou Senha Incorreto!");
                alert.setCancelable(true);

                alert.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.show();
            }

        }
    }

    private boolean validateEmailAndSenha() {
        String emailInput = txt_input_emailLogin.getEditText().getText().toString().trim();
        String senhaInput = txt_input_senhaLogin.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()){
            txt_input_emailLogin.setError("O campo email devem ser preenchido");
        } else{
            txt_input_emailLogin.setError(null);
        }
        if (senhaInput.isEmpty()){
            txt_input_senhaLogin.setError("O campo senha devem ser preenchido");
            return false;
        }
        else{
            txt_input_senhaLogin.setError(null);
            return true;
        }
    }

    private void loadSharedPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = sp.getString("email",null);
        String senha = sp.getString("senha",null);

        if(email!=null){
            txt_input_emailLogin.getEditText().setText(email);
            txt_input_senhaLogin.getEditText().setText(senha);
        }else {
            userEtIsEmpty=true;
            passwordEtIsEmpty=true;
        }
    }
}
