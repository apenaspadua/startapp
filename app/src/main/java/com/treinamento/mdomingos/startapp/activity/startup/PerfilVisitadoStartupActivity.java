package com.treinamento.mdomingos.startapp.activity.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.Publicacao;
import com.treinamento.mdomingos.startapp.model.StartupResponse;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilVisitadoStartupActivity extends AppCompatActivity{

    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Task<Uri> storageReference;
    private FirebaseUser user;
    private String imageURL;
    private ProgressBar progressBar, progresso;
    private TextView nome, cidade, razao, email, rua, bairro, estado, telefone, bio, apresentacao, link, meta, investido;
    private CircleImageView foto;
    private RelativeLayout editarVideo;
    private FirebaseStorage storage;
    private String data;
    private int investidoInt = 0, metaInt = 0, cont = 0;
    private Handler hdlr = new Handler();

    private VideoView videoView;
    private Uri videoUri;
    private MediaController mediaController;

    @Override
    protected void onStart() {
        super.onStart();
        loadUserInformation();
        progressBar.setVisibility(View.VISIBLE);
        loadVideo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_visitado_startup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();

        nome = findViewById(R.id.nome_perfilVisitado_startup_id);
        cidade = findViewById(R.id.text_cidade_perfilVisitado_startup);
        razao = findViewById(R.id.text_razao_perfilVisitado_startup);
        email = findViewById(R.id.text_email_perfilVisitado_startup);
        rua = findViewById(R.id.rua_perfilVisitado_startup_id);
        bairro = findViewById(R.id.bairro_perfilVisitado_startup_id);
        estado = findViewById(R.id.estado_perfilVisitado_startup_id);
        telefone = findViewById(R.id.telefone_perfilVisitado_startup_id);
        bio = findViewById(R.id.text_biografia_perfilVisitado_startup);
        apresentacao = findViewById(R.id.text_apresentacao_perfilVisitado_startup);
        link = findViewById(R.id.text_link_perfilperfilVisitado_startup);
        foto = findViewById(R.id.foto_perfilVisitado_startup_id);
        videoView = findViewById(R.id.upload_video_perfilVisitado_id);
        progressBar = findViewById(R.id.progressBar_perfilVisitado_startup);
        meta = findViewById(R.id.meta_progressbar_perfilVisitado_startup);
        investido = findViewById(R.id.conquistado_progressbar_perilVisitado_startup);
        progresso = findViewById(R.id.progressbar_progresso_perfilVisitado_startup);
        progressDialog = new ProgressDialog(this);


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController = new MediaController(PerfilVisitadoStartupActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });
        videoView.start();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            data = extras.getString("publicacoes");

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuarios").child(data).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    StartupResponse startup = dataSnapshot.getValue(StartupResponse.class);
                    nome.setText(startup.getDetalhe_startup().getNomeFantasia());
                    cidade.setText(startup.getDetalhe_startup().getCidade());
                    razao.setText(startup.getDetalhe_startup().getRazaoSocial());
                    email.setText(startup.getDetalhe_startup().getEmail());
                    rua.setText(startup.getDetalhe_startup().getRua());
                    bairro.setText(startup.getDetalhe_startup().getBairro());
                    estado.setText(startup.getDetalhe_startup().getEstado());
                    telefone.setText(startup.getDetalhe_startup().getTelefone());
                    bio.setText(startup.getDetalhe_startup().getBiografia());
                    apresentacao.setText(startup.getDetalhe_startup().getApresentacao());
                    link.setText(startup.getDetalhe_startup().getLink());

                    if(startup.getProgresso_startup() == null){
                        return;
                    } else {
                        meta.setText("R$ " + startup.getProgresso_startup().getMeta());

                        investido.setText("R$ " + startup.getProgresso_startup().getInvestido());
                    }
                    if (startup.getProgresso_startup().getInvestido() != null) {
                        try {
                            investidoInt = Integer.parseInt(startup.getProgresso_startup().getInvestido());
                            metaInt =  Integer.parseInt(startup.getProgresso_startup().getMeta());
                        } catch(NumberFormatException e) {
                            investidoInt = 0;
                            metaInt = 0;
                        }
                    }
                    loadProgress(investidoInt, metaInt);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }
    }

    private void loadProgress(final int dado, final int dadoMax){
        cont = progresso.getProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(cont < dadoMax) {
                    cont += 1;
                    hdlr.post(new Runnable() {
                        public void run() {
                            progresso.setMax(dadoMax);
                            progresso.setProgress(dado);
                        }
                    });
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private void loadUserInformation() {
        storageReference = FirebaseStorage.getInstance().getReference().child("foto_perfil").
                child(data).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                imageURL = uri.toString();
                Glide.with(PerfilVisitadoStartupActivity.this).load(imageURL).into(foto);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadVideo(){
        storageReference = FirebaseStorage.getInstance().getReference().child("video_publicacao").
                child(data).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {

                videoUri = Uri.parse(uri.toString());
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

}




