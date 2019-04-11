package com.treinamento.mdomingos.startapp.activity.inicial;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentInvestidor;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentStartup;
import com.treinamento.mdomingos.startapp.activity.investidor.BioInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.investidor.CadastroInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.login.CadastroLoginActivity;
import com.treinamento.mdomingos.startapp.activity.login.LoginActivity;
import com.treinamento.mdomingos.startapp.activity.startup.CadastroStartupActivity;
import com.treinamento.mdomingos.startapp.model.Usuarios;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

public class InicialActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private RelativeLayout botaoCadastrar;
    private TextView jaPossuoConta;
    private ProgressBar progressBar;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        //Instance
        botaoCadastrar = findViewById(R.id.inicial_botao_cadastrar_id);
        jaPossuoConta = findViewById(R.id.incial_text_id);
        progressBar = findViewById(R.id.inicial_progress_bar_id);
        videoView = findViewById(R.id.videoView2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //functions

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseConfig.getFirebase();
            databaseReference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Usuarios usuario = dataSnapshot.getValue(Usuarios.class);

                    if (usuario.getPerfil() == 1) {
                        if (usuario.getDetalhes_completo() == 0) {
                            Intent intent = new Intent(InicialActivity.this, CadastroInvestidorActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(InicialActivity.this, BaseFragmentInvestidor.class);
                            startActivity(intent);
                            finish();
                        }
                    } else if (usuario.getPerfil() == 2) {
                        if (usuario.getDetalhes_completo() == 0) {
                            Intent intent = new Intent(InicialActivity.this, CadastroStartupActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(InicialActivity.this, BaseFragmentStartup.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {

            botaoCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (FirebaseConfig.firebaseConection()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(InicialActivity.this, CadastroLoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(InicialActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            jaPossuoConta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (FirebaseConfig.firebaseConection()) {
                        jaPossuoConta.setTextColor(Color.parseColor("#0289BE"));
                        Intent intent = new Intent(InicialActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(InicialActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
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


