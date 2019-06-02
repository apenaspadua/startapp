package com.treinamento.mdomingos.startapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.Investidor;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.Validator;

public class BioInvestidorActivity extends AppCompatActivity {

    private EditText bio, apresentacao;
    private RelativeLayout botaoConcluir;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_investidor);

        botaoConcluir = findViewById(R.id.botao_bio_concluir_cadastro_login_id);
        progressDialog = new ProgressDialog(BioInvestidorActivity.this);
        apresentacao = findViewById(R.id.apresentacao_cadastro_investidor_id);
        bio = findViewById(R.id.biografia_cadastro_investidor_id);
        bio.setScroller(new Scroller(getApplicationContext()));
        bio.setVerticalScrollBarEnabled(true);
        bio.setMinLines(100);
        bio.setMaxLines(100);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        botaoConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Guardando dados...");
                progressDialog.show();

                if(FirebaseConfig.firebaseConection()) {
                    String bioRecebida = bio.getText().toString();
                    String apresentacaoRecebida = apresentacao.getText().toString();

                    if (Validator.stringEmpty(bioRecebida)) {
                        bio.setError("Insira uma breve descrição");
                    } else {
                        Investidor investidor = new Investidor(bioRecebida, apresentacaoRecebida);
                        investidor.salvarBioInvestidor(firebaseUser.getUid());
                        startActivity(new Intent(BioInvestidorActivity.this,  MainFragmentInvestidorActivity.class));
                        finish();
                    }
                } else {
                    Log.i("sem internet", "sem conexao");
                    Toast.makeText(BioInvestidorActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
