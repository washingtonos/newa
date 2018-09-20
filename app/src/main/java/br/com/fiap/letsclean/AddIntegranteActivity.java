package br.com.fiap.letsclean;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class AddIntegranteActivity extends AppCompatActivity {
    private Long userId,grupoId;
    private TextInputLayout txt_addEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_integrante);

        txt_addEmail = findViewById(R.id.txt_addEmail);

        //Capturar extras de GrupoActivity
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId = extras.getLong("userId");
            grupoId = extras.getLong("grupoId");
        }
    }

    /*
     *
     * Metodo para add Integrante
     */
    public void  addIntegranteBNT(View v){
        if(validateEmail()){

            // Populando novo objeto
            Usuario usuario = new Usuario();
            usuario.setEmail(txt_addEmail.getEditText().getText().toString());
            usuario.setGrupoId(grupoId);

            //Chamando ASK e enviando PUT
            AddIntegranteActivity.AtualizarUsuarioTask task = new AddIntegranteActivity.AtualizarUsuarioTask();
            task.execute(usuario);
        }
    }
    private boolean validateEmail() {
        String nomeInput = txt_addEmail.getEditText().getText().toString().trim();

        if (nomeInput.isEmpty()){
            txt_addEmail.setError("O campo email deve estar preenchido");
            return false;
        } else{
            txt_addEmail.setError(null);
            return true;
        }
    }

    private class AtualizarUsuarioTask extends AsyncTask<Usuario,Void,Integer> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddIntegranteActivity.this, "Aguarde..", "Inserindo Usuario..");
        }

        @Override
        protected Integer doInBackground(Usuario... usuarios) {
            try{
                URL url = new URL("http://www.letscleanof.com/api/usuario/grupo");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParamsGrupo = new JSONObject();
                jsonParamsGrupo.put("email",usuarios[0].getEmail());
                jsonParamsGrupo.put("grupoId",usuarios[0].getGrupoId());

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
                Toast.makeText(AddIntegranteActivity.this, "Tente Novamente mais tarde", Toast.LENGTH_LONG).show();
            }
            if (integer == 500){
                Toast.makeText(AddIntegranteActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void openDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usuario")
                .setMessage("Adicionado com Sucesso")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        limparActivity();
                    }
                });
        builder.show();
    }

    private void limparActivity() {
        txt_addEmail.getEditText().setText("");
    }
}
