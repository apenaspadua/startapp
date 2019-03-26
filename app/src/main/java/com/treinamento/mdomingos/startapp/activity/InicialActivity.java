package com.treinamento.mdomingos.startapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;


import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.treinamento.mdomingos.startapp.R;


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
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(InicialActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);

            }
        });

        jaPossuoConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jaPossuoConta.setTextColor(Color.parseColor("#0289BE"));
                Intent intent = new Intent(InicialActivity.this, LoginActivity.class);
                startActivity(intent);

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
}
