package br.com.fiap.letsclean;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.fiap.letsclean.entity.Grupo;

public class CadastroGrupoActivity extends AppCompatActivity {

    String idUsuario;
    TextInputLayout txt_input_nome, txt_input_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);

        txt_input_nome = findViewById(R.id.txt_input_nome);
        txt_input_desc = findViewById(R.id.txt_input_desc);
         //capturar extras de MenuActivity E Criando grupo

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idUsuario = extras.getString("idUser");
        }
    }

    private boolean validateNomeAndDesc() {
        String nomeInput = txt_input_nome.getEditText().getText().toString().trim();
        String descInput = txt_input_desc.getEditText().getText().toString().trim();

        if (nomeInput.isEmpty()){
            txt_input_nome.setError("O campo nome deve estar preenchido");
        } else{
            txt_input_nome.setError(null);
        }
        if (descInput.isEmpty()){
            txt_input_desc.setError("O campo descrição deve estar preenchido");
            return false;
        }
        else{
            txt_input_desc.setError(null);
            return true;
        }
    }

    /*
     *
     * Metodo para cadastrar Grupo
     */

    public void  cadastrarGrupo(View v){
        if(validateNomeAndDesc()){
            Grupo grupo = new Grupo();
            grupo.setNome(txt_input_nome.getEditText().getText().toString());
            grupo.setDescricao(txt_input_desc.getEditText().getText().toString());
            grupo.setIdUser(idUsuario);

            // Recuperar informações de outra activity (Senha)
            CadastraGrupoTask task = new CadastraGrupoTask();
            task.execute(grupo);
        }
    }

    private void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Grupo cadastrado com Sucesso");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private class CadastraGrupoTask extends AsyncTask<Grupo,Void,Integer>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CadastroGrupoActivity.this, "Aguarde..", "Enviando  dados para o servidor");
        }

        @Override
        protected Integer doInBackground(Grupo... grupos) {
            try{
                URL url = new URL("http://www.letscleanof.com/api/grupo");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParamsGrupo = new JSONObject();
                jsonParamsGrupo.put("nome",grupos[0].getNome());
                jsonParamsGrupo.put("descricao",grupos[0].getDescricao());
                jsonParamsGrupo.put("id_user", grupos[0].getIdUser());
                jsonParamsGrupo.put("id_adm", '1');

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
                Toast.makeText(CadastroGrupoActivity.this, "Tente Novamente mais tarde", Toast.LENGTH_LONG).show();
            }
            if (integer == 500){
                Toast.makeText(CadastroGrupoActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Abir grupo
    private void openGrupo(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(CadastroGrupoActivity.this, GrupoActivity.class);
        startActivity(intent);
    }

    private void openDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Grupo")
                .setMessage("Cadastrado com Sucesso")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGrupo(dialog);
                    }
                });
        builder.show();
    }
}
