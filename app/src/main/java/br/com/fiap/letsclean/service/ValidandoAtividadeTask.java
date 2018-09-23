package br.com.fiap.letsclean.service;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.fiap.letsclean.entity.Atividade;

public class ValidandoAtividadeTask extends AsyncTask<Atividade,Void,Integer> {

    @Override
    protected Integer doInBackground(Atividade... atividades) {
        try{
            URL url = new URL("http://www.letscleanof.com/api/atividade/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParamsAtividade = new JSONObject();
            jsonParamsAtividade.put("id",atividades[0].getId());
            jsonParamsAtividade.put("validada","SIM");

            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(jsonParamsAtividade.toString());
            osw.close();

            return connection.getResponseCode();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
