package br.com.fiap.letsclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RelatorioActivity extends AppCompatActivity {

    private String userId, nomeUser;
    private Long admUser,grupoId, userId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

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

    public void concluidas(View view) {
        Intent intent = new Intent(this, AtividadeConcluidasActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("nomeUser", nomeUser);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
            intent.putExtra("userId2", userId2);
        }
        startActivity(intent);
        finish();
    }

    public void pendentes(View view) {
        Intent intent = new Intent(this, AtividadesPendentesActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("nomeUser", nomeUser);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
            intent.putExtra("userId2", userId2);
        }
        startActivity(intent);
        finish();
    }

}
