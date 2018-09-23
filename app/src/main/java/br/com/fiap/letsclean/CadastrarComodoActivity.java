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

import br.com.fiap.letsclean.entity.Comodo;

public class CadastrarComodoActivity extends AppCompatActivity {

    private String userId;
    private Long grupoId, userId2;
    private TextInputLayout txt_input_nomeComodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_comodo);

        txt_input_nomeComodo = findViewById(R.id.txt_input_nomeComodo);

        //capturar extras de MenuActivity E Criando Comodo
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId = extras.getString("userId");
            userId2 = extras.getLong("userId2");
            grupoId = extras.getLong("grupoId");
        }
    }

    private boolean validateNome() {
        String nomeInput = txt_input_nomeComodo.getEditText().getText().toString().trim();

        if (nomeInput.isEmpty()){
            txt_input_nomeComodo.setError("O campo nome deve estar preenchido");
            return false;
        } else{
            txt_input_nomeComodo.setError(null);
            return true;
        }
    }

    /*
     *
     * Metodo para cadastrar Comodo
     */

    public void  cadastrarComodo(View v){
        if(validateNome()){
            Comodo comodo = new Comodo();
            comodo.setNome(txt_input_nomeComodo.getEditText().getText().toString());
            comodo.setUserId(userId2);
            comodo.setGrupoId(grupoId);

            // Recuperar informações de outra activity (Senha)
            CadastrarComodoActivity.CadastraCadastrarTask task = new CadastrarComodoActivity.CadastraCadastrarTask();
            task.execute(comodo);
        }
    }

    private class CadastraCadastrarTask extends AsyncTask<Comodo,Void,Integer> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CadastrarComodoActivity.this, "Aguarde..", "Enviando  dados para o servidor");
        }

        @Override
        protected Integer doInBackground(Comodo... comodos) {
            try{
                URL url = new URL("http://www.letscleanof.com/api/comodo");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParamsGrupo = new JSONObject();
                jsonParamsGrupo.put("nome",comodos[0].getNome());
                jsonParamsGrupo.put("userId", comodos[0].getUserId());
                jsonParamsGrupo.put("grupoId",comodos[0].getGrupoId().toString());

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

            if(integer == 201){
                openDialog();
            } if (integer == 404){
                Toast.makeText(CadastrarComodoActivity.this, "Tente Novamente mais tarde", Toast.LENGTH_LONG).show();
            }
            if (integer == 500){
                Toast.makeText(CadastrarComodoActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Comodo")
                .setMessage("Cadastrado com Sucesso")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGrupo(dialog);
                    }
                });
        builder.show();
    }

    // Abir comodo
    private void openGrupo(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(CadastrarComodoActivity.this, ComodoActivity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("grupoId", grupoId);
        startActivity(intent);
        finish();
    }
}
