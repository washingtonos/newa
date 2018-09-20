package br.com.fiap.letsclean;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ComodoDetalheActivity extends AppCompatActivity {

    private  String nome;
    private Long  userId,grupoId;
    //private Button btn_add_atividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comodo_detalhe);

        //Capturar extras de ComodoActivity
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            nome = extras.getString("nome");
            userId = extras.getLong("userId");
            grupoId = extras.getLong("grupoId");
        }
    }
}
