package br.com.fiap.letsclean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Atividade2Activity extends AppCompatActivity {

    private Long admUser,grupoId,userId2;
    private String nomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade2);

        //capturar extras de MenuActivity
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            userId2 = extras.getLong("userId2");
            admUser = extras.getLong("admUser");
            grupoId = extras.getLong("grupoId");
            nomeUser = extras.getString("nomeUser");
        }
    }

    public void atividade(View view) {
        Intent intent = new Intent(this, AtividadeActivity.class);
        intent.putExtra("nomeUser", nomeUser);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
            intent.putExtra("userId2", userId2);
        }
        startActivity(intent);
    }

    public void atividadeValidar(View view) {
        Intent intent = new Intent(this, ValidarAtividadeActivity.class);
        intent.putExtra("nomeUser", nomeUser);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
            intent.putExtra("userId2", userId2);
        }
        startActivity(intent);
    }
}
