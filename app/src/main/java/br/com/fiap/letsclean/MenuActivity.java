package br.com.fiap.letsclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Capturar id de Usuario  de activity anterior
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idUsuario = extras.getString("idUser");
        }
    }

    public void group(View view) {
        Intent intent = new Intent(this, GrupoActivity.class);
        intent.putExtra("idUser", idUsuario);
        startActivity(intent);
    }

    public void comodo(View view) {
        Intent intent = new Intent(this, ComodoActivity.class);
        intent.putExtra("idUser", idUsuario);
        startActivity(intent);
    }
}
