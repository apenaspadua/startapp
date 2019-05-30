package com.treinamento.mdomingos.startapp.activity.others;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentInvestidor;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentStartup;
import com.treinamento.mdomingos.startapp.activity.inicial.InicialActivity;
import com.treinamento.mdomingos.startapp.model.Usuarios;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;

public class SplashScrean extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screan);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        //functions
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseConfig.getFirebase();
            databaseReference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Usuarios usuario = dataSnapshot.getValue(Usuarios.class);
                    if(usuario != null){
                        if (usuario.getPerfil() == 1) {
                            if (usuario.getDetalhes_completo() == 0) {
                                Intent intent = new Intent(SplashScrean.this, SlidesPosCadastroActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(SplashScrean.this, BaseFragmentInvestidor.class);
                                startActivity(intent);
                                finish();
                            }
                        } else if (usuario.getPerfil() == 2) {
                            if (usuario.getDetalhes_completo() == 0) {
                                Intent intent = new Intent(SplashScrean.this, SlidesPosCadastroActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(SplashScrean.this, BaseFragmentStartup.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else {
                       return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            Intent intent = new Intent(SplashScrean.this, InicialActivity.class);
            startActivity(intent);
            finish();
        }

    }
}

