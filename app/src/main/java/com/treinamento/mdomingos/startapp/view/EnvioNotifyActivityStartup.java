package com.treinamento.mdomingos.startapp.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.InvestidorResponse;
import com.treinamento.mdomingos.startapp.model.Notificacao;
import com.treinamento.mdomingos.startapp.model.Salvos;
import com.treinamento.mdomingos.startapp.model.StartupResponse;
import com.treinamento.mdomingos.startapp.service.APIService;
import com.treinamento.mdomingos.startapp.service.notifications.Client;
import com.treinamento.mdomingos.startapp.service.notifications.Data;
import com.treinamento.mdomingos.startapp.service.notifications.MyResponse;
import com.treinamento.mdomingos.startapp.service.notifications.Sender;
import com.treinamento.mdomingos.startapp.service.notifications.Token;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnvioNotifyActivityStartup extends AppCompatActivity {

    private TextView text1, text2, text3, voltarHome;
    private ProgressBar progressBar;

    APIService apiService;

    Intent intent;
    String userid;
    FirebaseUser user;
    private String nomeInvestidor, fotoPerfil, nomeStartup, fotoStartup;
    private String idNotify;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio_notify_startup);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        intent = getIntent();
        userid = intent.getStringExtra("notificacao");
        user = FirebaseAuth.getInstance().getCurrentUser();

        text1 = findViewById(R.id.mensagem_envio_notification);
        text2 = findViewById(R.id.submensagem_envio_notification);
        text3 = findViewById(R.id.submensagem2_envio_notification);
        progressBar = findViewById(R.id.progressBar_notifify);
        voltarHome = findViewById(R.id.voltar_para_home_id);

        updateToken(FirebaseInstanceId.getInstance().getToken());


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                InvestidorResponse investidor = dataSnapshot.getValue(InvestidorResponse.class);

                nomeInvestidor = investidor.getDetalhe_investidor().getNome();
                fotoPerfil = investidor.getFoto_Perfil();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notify = true;

        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StartupResponse startup = dataSnapshot.getValue(StartupResponse.class);
                nomeStartup = startup.getDetalhe_startup().getNomeFantasia();
                fotoStartup = startup.getFoto_perfil();

                if (notify) {
                    sendNotifiaction(userid, nomeInvestidor, fotoPerfil);
                }
                notify = false;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            private void sendNotifiaction(String receiver, final String username, final  String fotoPerfil) {
                DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
                Query query = tokens.orderByKey().equalTo(receiver);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Token token = snapshot.getValue(Token.class);
                            Data data = new Data(user.getUid(), R.mipmap.ic_logo_app, username + " se interessou pelo seu projeto!", "Você tem um novo interesse", userid);
                            Sender sender = new Sender(data, token.getToken());

                            apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {

                                            Toast.makeText(EnvioNotifyActivityStartup.this, "Falha!", Toast.LENGTH_SHORT).show();
                                        }

                                            idNotify = UUID.randomUUID().toString();

                                            Notificacao notificacao = new Notificacao();
                                            notificacao.setDescricao(username + " mostrou um interesse pelo seu projeto. Entre em contato!");
                                            notificacao.setFotoPerfil(fotoPerfil);
                                            notificacao.setSenderId(user.getUid());
                                            notificacao.salvarNotificacao(userid, String.valueOf(idNotify), user.getUid());

                                            Salvos salvos = new Salvos();
                                            salvos.ListarSalvos(user.getUid(), idNotify, nomeStartup, fotoStartup);

                                            progressBar.setVisibility(View.GONE);
                                            text1.setText("Notificação enviada!");
                                            text2.setVisibility(View.VISIBLE);
                                            text3.setVisibility(View.VISIBLE);
                                            voltarHome.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        voltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarHome.setTextColor(Color.parseColor("#0289BE"));
                finish();
            }
        });

    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(user.getUid()).setValue(token1);
    }
}




