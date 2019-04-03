package com.treinamento.mdomingos.startapp.activity.inicial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentInvestidor;
import com.treinamento.mdomingos.startapp.activity.login.CadastroLoginActivity;
import com.treinamento.mdomingos.startapp.activity.login.LoginActivity;


public class InicialActivity extends AppCompatActivity {

    private RelativeLayout botaoCadastrar;
    private TextView jaPossuoConta;
    private ProgressBar progressBar;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        //Instance
        botaoCadastrar = findViewById(R.id.inicial_botao_cadastrar_id);
        jaPossuoConta = findViewById(R.id.incial_text_id);
        progressBar = findViewById(R.id.inicial_progress_bar_id);
        videoView = findViewById(R.id.videoView2);

        //functions

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(InicialActivity.this, BaseFragmentInvestidor.class));
            finish();
            return;
        }


        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseConection()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(InicialActivity.this, CadastroLoginActivity.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(InicialActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        jaPossuoConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseConection()) {
                    jaPossuoConta.setTextColor(Color.parseColor("#0289BE"));
                    Intent intent = new Intent(InicialActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(InicialActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.videologin);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        jaPossuoConta.setTextColor(Color.parseColor("#ffffff"));
        progressBar.setVisibility(View.GONE);
    }

    public boolean FirebaseConection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) // conectado a internet
            return true;
        return false; // nao conectado
    }


}


