package com.treinamento.mdomingos.startapp.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.treinamento.mdomingos.startapp.R;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        contatos = findViewById(R.id.floatingActionButton);

        contatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, ContatosChatActivity.class));
                finish();
            }
        });
    }

}
