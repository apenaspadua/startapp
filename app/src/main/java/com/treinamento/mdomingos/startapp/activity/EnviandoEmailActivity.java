package com.treinamento.mdomingos.startapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;

public class EnviandoEmailActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private TextView jaValidei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviando_email);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        jaValidei = findViewById(R.id.ja_validei_id);

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Log.i("Email", "Email enviado!");
                } else {
                    Log.i("Email", "Falha ao enviar email");
                }
            }
        });

        jaValidei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jaValidei.setTextColor(Color.parseColor("#0289BE"));

                Intent intent = new Intent(EnviandoEmailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}