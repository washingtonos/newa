package br.com.fiap.letsclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private String userId;
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
        }
    }

    public void group(View view) {
        Intent intent = new Intent(this, GrupoActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("grupoId", grupoId);
        startActivity(intent);
    }

    public void comodo(View view) {
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
        Intent intent = new Intent(this, AtividadeActivity.class);
        intent.putExtra("userId", userId);
        if(grupoId != null){
            intent.putExtra("admUser", admUser);
            intent.putExtra("grupoId", grupoId);
            intent.putExtra("userId2", userId2);
        }
        startActivity(intent);
    }
}
