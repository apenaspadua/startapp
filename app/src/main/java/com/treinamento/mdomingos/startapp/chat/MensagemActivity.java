package com.treinamento.mdomingos.startapp.chat;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.Usuarios;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensagemActivity extends AppCompatActivity {

    CircleImageView imagemPerfil;
    TextView username;
    ImageView back;

    FirebaseUser user;
    DatabaseReference databaseReference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imagemPerfil = findViewById(R.id.image_profile_message_chat);
        username = findViewById(R.id.username_message);
        back = findViewById(R.id.back_chat);

        intent = getIntent();
        String userid = intent.getStringExtra("userid");

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                username.setText(usuarios.getNome());

                if (usuarios.getPerfil() == 1) {
                    if (usuarios.getFoto_perfil() == null) {
                        imagemPerfil.setImageResource(R.drawable.investidor_icon2);
                    } else {
                        Glide.with(MensagemActivity.this).load(usuarios.getFoto_perfil()).into(imagemPerfil);
                    }

                } else if (usuarios.getPerfil() == 2) {

                    if (usuarios.getFoto_perfil() == null) {
                        imagemPerfil.setImageResource(R.drawable.startup_icon2);
                    } else {
                        Glide.with(MensagemActivity.this).load(usuarios.getFoto_perfil()).into(imagemPerfil);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
