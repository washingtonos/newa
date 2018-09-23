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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.fiap.letsclean.entity.Atividade;
import br.com.fiap.letsclean.entity.Comodo;
import br.com.fiap.letsclean.entity.Usuario;
import cz.msebera.android.httpclient.Header;

public class CadastrarAtividadeActivity extends AppCompatActivity {

    private Long grupoId, userId2, userAtividade, idComodo, admUser;
    private ArrayList<Comodo> listComodo;
    private ArrayList<Usuario> listUsuario;
    TextInputLayout txt_input_nomeAtividade, txt_input_descAtividade;
    Spinner spn_comodo, spn_usuario;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_atividade);

        //capturar extras de MenuActivity E Criando comodo
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId2 = extras.getLong("userId2");
            grupoId = extras.getLong("grupoId");
            admUser = extras.getLong("admUser");
        }
        txt_input_nomeAtividade = findViewById(R.id.txt_input_nomeAtividade);
        txt_input_descAtividade = findViewById(R.id.txt_input_descAtividade);

        client = new AsyncHttpClient();
        spn_comodo  = findViewById(R.id.spn_comodo);
        spn_usuario = findViewById(R.id.spn_usuario);
        ilenarSpinner();
        ilenarUsuarioSpinner();

    }


    /*
     *
     * Metodo para cadastrar Atividade
     */
    public void cadastrarAtividade(View view) {
        if(validateNomeAndDesc()){
            // Pegar o item selecionado no spinner
            String comodo = spn_comodo.getSelectedItem().toString();
            String  usuario = spn_usuario.getSelectedItem().toString();

            // Buscando o id do item selecionado no spinner
            pegarIdComodo(comodo);
            pegarIdUsuario(usuario);

            // Populando Atividade
            Atividade atividade = new Atividade();
            atividade.setNome(txt_input_nomeAtividade.getEditText().getText().toString());
            atividade.setDesc(txt_input_descAtividade.getEditText().getText().toString());
            atividade.setComodoId(idComodo);
            atividade.setUserId(userAtividade);
            atividade.setGrupoId(grupoId);
            atividade.setStatus(Long.valueOf(0));

            // Fazendo o cadastro da atividade
            CadastroAtividadeTask task = new CadastroAtividadeTask();
            task.execute(atividade);

        }
    }

    /*
    *
    * Metodo para cadastrar atividade POST
    */
    private class CadastroAtividadeTask extends AsyncTask<Atividade,Void,Integer>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CadastrarAtividadeActivity.this, "Aguarde..", "Enviando  dados para o servidor");
        }

        @Override
        protected Integer doInBackground(Atividade... atividades) {
            try{
                URL url = new URL("http://www.letscleanof.com/api/atividade");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParamsAtividade = new JSONObject();
                jsonParamsAtividade.put("nome",atividades[0].getNome());
                jsonParamsAtividade.put("descricao",atividades[0].getDesc());
                jsonParamsAtividade.put("userId", atividades[0].getUserId());
                jsonParamsAtividade.put("grupoId", atividades[0].getGrupoId());
                jsonParamsAtividade.put("comodoId", atividades[0].getComodoId());
                jsonParamsAtividade.put("status", atividades[0].getStatus());

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonParamsAtividade.toString());
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
                Toast.makeText(CadastrarAtividadeActivity.this, "Tente Novamente mais tarde", Toast.LENGTH_LONG).show();
            }
            if (integer == 500){
                Toast.makeText(CadastrarAtividadeActivity.this, "Internal Server Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atividade")
                .setMessage("Cadastrada com Sucesso")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAtividade(dialog);
                    }
                });
        builder.show();
    }

    // Abrir AtividadeActivity
    private void openAtividade(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(CadastrarAtividadeActivity.this, AtividadeActivity.class);
        intent.putExtra("grupoId", grupoId);
        intent.putExtra("userId2",userId2);
        intent.putExtra("admUser",admUser);
        startActivity(intent);
        finish();
    }

    /*
     *
     * Metodo para pegar ID do comodo pelo nome
     */
    private void pegarIdComodo(String comodo) {
        for (int i=0; i < listComodo.size(); i++){
            if (listComodo.get(i).getNome().equals(comodo) ){
                idComodo = listComodo.get(i).getId();
            }
        }
    }

    /*
     *
     * Metodo para pegar ID do comodo pelo nome
     */
    private void pegarIdUsuario(String usuario) {
        for (int i=0; i < listUsuario.size(); i++){
            if (listUsuario.get(i).getNome().equals(usuario) ){
                userAtividade = listUsuario.get(i).getId();
            }
        }
    }

    private void ilenarSpinner(){
        String url = "http://www.letscleanof.com/api/comodos/grupo/"+grupoId;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    carregarSpinnerComodo(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void ilenarUsuarioSpinner(){
        String url = "http://www.letscleanof.com/api/usuario/grupo/"+grupoId;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    carregarSpinnerUsuario(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void carregarSpinnerComodo(String s) {
        listComodo = new ArrayList<>();

            try{
                JSONArray jsonComodo = new JSONArray(s);
                for (int i=0; i < jsonComodo.length(); i++){
                    Comodo c = new Comodo();
                    c.setNome(jsonComodo.getJSONObject(i).getString("nome"));
                    c.setId(jsonComodo.getJSONObject(i).getLong("id"));

                    listComodo.add(c);
                }

                if(listComodo.isEmpty()){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarAtividadeActivity.this);
                    builder.setTitle("Atividade")
                            .setMessage("Para cadastrar uma Atividade, você precisa criar um comodo")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(CadastrarAtividadeActivity.this,CadastrarComodoActivity.class);
                                    intent.putExtra("userId2", userId2);
                                    intent.putExtra("grupoId", grupoId);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }else{
                    ArrayAdapter <Comodo> a = new ArrayAdapter<Comodo>(this,  android.R.layout.simple_dropdown_item_1line, listComodo);
                    spn_comodo.setAdapter(a);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void carregarSpinnerUsuario(String s) {
        listUsuario = new ArrayList<>();

        try{
            JSONArray jsonUsuario = new JSONArray(s);
            for (int i=0; i < jsonUsuario.length(); i++){
                Usuario u = new Usuario();
                u.setNome(jsonUsuario.getJSONObject(i).getString("nome"));
                u.setId(jsonUsuario.getJSONObject(i).getLong("id"));
                listUsuario.add(u);
            }
                ArrayAdapter <Usuario> a = new ArrayAdapter<Usuario>(this,  android.R.layout.simple_dropdown_item_1line, listUsuario);
                spn_usuario.setAdapter(a);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean validateNomeAndDesc() {
        String nomeInput = txt_input_nomeAtividade.getEditText().getText().toString().trim();
        String descInput = txt_input_descAtividade.getEditText().getText().toString().trim();

        if (nomeInput.isEmpty()){
            txt_input_nomeAtividade.setError("O campo nome deve estar preenchido");
        } else{
            txt_input_nomeAtividade.setError(null);
        }
        if (descInput.isEmpty()){
            txt_input_descAtividade.setError("O campo descrição deve estar preenchido");
            return false;
        }
        else{
            txt_input_descAtividade.setError(null);
            return true;
        }
    }
}
