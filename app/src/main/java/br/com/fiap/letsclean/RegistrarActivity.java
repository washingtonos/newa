package br.com.fiap.letsclean;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.fiap.letsclean.entity.Usuario;

public class RegistrarActivity extends AppCompatActivity {

    private TextInputLayout txt_input_nomeUsuario, txt_input_email, txt_input_senha ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txt_input_nomeUsuario = findViewById(R.id.txt_input_nomeUsuario);
        txt_input_email = findViewById(R.id.txt_input_email);
        txt_input_senha = findViewById(R.id.txt_input_senha);
    }

    private boolean validateNomeAndEmailAndSenha() {
        String nomeInput = txt_input_nomeUsuario.getEditText().getText().toString().trim();
        String emailInput = txt_input_email.getEditText().getText().toString().trim();
        String senhaInput = txt_input_senha.getEditText().getText().toString().trim();

        if (nomeInput.isEmpty()){
            txt_input_nomeUsuario.setError("O campo nome deve estar preenchido");
        } else{
            txt_input_nomeUsuario.setError(null);
        }
        if (emailInput.isEmpty()){
            txt_input_email.setError("O campo email deve estar preenchido");
        } else{
            txt_input_email.setError(null);
        }
        if (senhaInput.isEmpty()){
            txt_input_senha.setError("O campo senha deve estar preenchido");
            return false;
        }
        else{
            txt_input_senha.setError(null);
            return true;
        }

    }

    public void cadastrarUsuario(View v){
        if(validateNomeAndEmailAndSenha()){
            Usuario usuario = new Usuario();
            usuario.setNome(txt_input_nomeUsuario.getEditText().getText().toString());
            usuario.setEmail(txt_input_email.getEditText().getText().toString());
            usuario.setSenha(txt_input_senha.getEditText().getText().toString());
            usuario.setAdmUser(Long.valueOf(0));
            CadastrarUsuarioTask task = new CadastrarUsuarioTask();
            task.execute(usuario);
        }
    }

    private class CadastrarUsuarioTask extends AsyncTask<Usuario,Void,Integer>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegistrarActivity.this, "Aguarde..", "Criando Usuario..");
        }

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            try{
                URL url = new URL("http://www.letscleanof.com/api/usuario");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParamsGrupo = new JSONObject();
                jsonParamsGrupo.put("nome",usuarios[0].getNome());
                jsonParamsGrupo.put("email",usuarios[0].getEmail());
                jsonParamsGrupo.put("senha",usuarios[0].getSenha());
                jsonParamsGrupo.put("admUser",usuarios[0].getAdmUser().toString());
                jsonParamsGrupo.put("grupoId",usuarios[0].getAdmUser().toString());
                jsonParamsGrupo.put("grupoIdAntigo",usuarios[0].getAdmUser().toString());
                jsonParamsGrupo.put("comodoId",usuarios[0].getAdmUser().toString());

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonParamsGrupo.toString());
                osw.close();

                return connection.getResponseCode();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progressDialog.dismiss();

            if(integer == 200 || integer == 201){
                openDialog();
            } if (integer == 404){
                Toast.makeText(RegistrarActivity.this, "Tente Novamente mais tarde", Toast.LENGTH_LONG).show();
            }
            if (integer == 500){
                Toast.makeText(RegistrarActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuario")
                .setMessage("Cadastrado com Sucesso")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      limparActivity();

                    }
                });
        builder.show();
    }


    // Abir Login
    public void openLogin(View view) {
        Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void limparActivity() {
        txt_input_nomeUsuario.getEditText().setText("");
        txt_input_email.getEditText().setText("");
        txt_input_senha.getEditText().setText("");
        txt_input_nomeUsuario.setVisibility(View.VISIBLE);
    }

}