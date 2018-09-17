package br.com.fiap.letsclean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GettingstartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gettingstarted);
    }

    public void haveAnAccount(View view) {
        Intent intent = new Intent(GettingstartedActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void sobre(View view) {
        Intent intent = new Intent(GettingstartedActivity.this,SobreActivity.class);
        startActivity(intent);
    }
}
