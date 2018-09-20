package br.com.fiap.letsclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Grupo_Detalhe extends AppCompatActivity {

    private  String nome;
    private Long  userId,grupoId;
    private Button btn_add_integrante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_detail);

        //Capturar extras de GrupoActivity
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            nome = extras.getString("nome");
            userId = extras.getLong("userId");
            grupoId = extras.getLong("grupoId");
        }

        btn_add_integrante = findViewById(R.id.btn_add_integrante);
     }

    public void openDialogCadastrar(View view) {
        Intent intent = new Intent(this, AddIntegranteActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("grupoId", grupoId);
        startActivity(intent);
    }
}