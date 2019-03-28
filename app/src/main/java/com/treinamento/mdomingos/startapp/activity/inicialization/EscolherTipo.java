package com.treinamento.mdomingos.startapp.activity.inicialization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.treinamento.mdomingos.startapp.R;

import com.treinamento.mdomingos.startapp.activity.others.EnviandoEmailActivity;


public class EscolherTipo extends AppCompatActivity {

    private RelativeLayout botaoinvestidor, botaoStartup;
    private ProgressDialog progressDialog;
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference produtoReference = firebaseReference.child("produtos");
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_tipo);

        botaoinvestidor = findViewById(R.id.botao_sou_investidor_id);
        botaoStartup = findViewById(R.id.botao_sou_startup_id);
        progressDialog = new ProgressDialog(EscolherTipo.this);



        botaoinvestidor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (FirebaseConection()) {
                        progressDialog.setMessage("Quase lá...");
                        progressDialog.show();

                        Intent intent = new Intent(EscolherTipo.this, EnviandoEmailActivity.class);
                        startActivity(intent);
                        finish();

                    } else { // sem conexao
                        Log.i("sem internet", "sem conexao");
                    }   Toast.makeText(EscolherTipo.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            });

            botaoStartup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FirebaseConection()) {
                        progressDialog.setMessage("Quase lá...");
                        progressDialog.show();

                        Intent intent = new Intent(EscolherTipo.this, EnviandoEmailActivity.class);
                        startActivity(intent);
                        finish();

                    } else { // sem conexao
                        Log.i("sem internet", "sem conexao");
                    }   Toast.makeText(EscolherTipo.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                }

            });
    }

    public boolean FirebaseConection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) // conectado a internet
            return true;
        return false; // nao conectado
    }
}
