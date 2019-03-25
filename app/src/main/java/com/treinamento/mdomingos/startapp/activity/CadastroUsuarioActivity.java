package com.treinamento.mdomingos.startapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.utils.Validator;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private RelativeLayout botaoCadastrar;
    private RelativeLayout botaoTwitter;
    private RelativeLayout botaoFacebook;
    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText confirmaSenha;
    int i = 0;
    final int [] trocaBotaoAoClicar = {
            R.drawable.button_ronded,
            R.drawable.button_custom_facebook,
            R.drawable.button_custom_twitter,
            R.drawable.button_rounded_precionado,
            R.drawable.button_rounded_precionado_red
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);


        //Instance
        botaoCadastrar = findViewById(R.id.botao_cadastrar_id);
        botaoTwitter = findViewById(R.id.botao_conecta_twitter_id);
        botaoFacebook = findViewById(R.id.botao__conecta_facebook_id);
        nome = findViewById(R.id.nome_cadastro_usuario_id);
        email = findViewById(R.id.email_cadastro_usuario_id);
        senha = findViewById(R.id.senha_cadastro_usuario_id);
        confirmaSenha = findViewById(R.id.comfirma_senha_cadastro_usuario_id);

        firebaseAuth = FirebaseAuth.getInstance();

        retornaButton();

        //Alterar buttons + functions
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { retornaButton();

           final String nomeRecebido = nome.getText().toString();
           final String emailRecebido = email.getText().toString();
           final String senhaRecebida = senha.getText().toString();
           final String confirmaSenhaRecebido = confirmaSenha.getText().toString();

         //Criando Usuario

                i = 4;
                botaoCadastrar.setBackgroundResource(trocaBotaoAoClicar[i]);

                if(Validator.stringEmpty(nomeRecebido)){
                    nome.setError("Insira seu nome");
                    retornaButton();
                }
                else if (Validator.stringEmpty(emailRecebido)){
                    email.setError("Insira seu email");
                    retornaButton();
                }
                else if (Validator.stringEmpty(senhaRecebida)){
                    senha.setError("Insira sua senha");
                    retornaButton();
                }
                else if (Validator.stringEmpty(confirmaSenhaRecebido)){
                        confirmaSenha.setError("Confirme sua senha");
                    retornaButton();
                }
                else if (confirmaSenhaRecebido.equals(senhaRecebida)){

                    firebaseAuth.createUserWithEmailAndPassword(emailRecebido, senhaRecebida)
                            .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        int createUser = Log.i("createUser", "Cadastrado com sucesso!!!");

                                        Intent intent = new Intent(CadastroUsuarioActivity.this, EnviandoEmailActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Log.i("createUser", "Falha ao cadastrar!!!");
                                        Toast.makeText(CadastroUsuarioActivity.this, "Falha ao cadastrar!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CadastroUsuarioActivity.this, "A senha não confere!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        botaoFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 3;
                botaoFacebook.setBackgroundResource(trocaBotaoAoClicar[i]);
            }
        });
        botaoTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 3;
                botaoTwitter.setBackgroundResource(trocaBotaoAoClicar[i]);
            }
        });
    }

    public void retornaButton(){
        botaoCadastrar.setBackgroundResource(trocaBotaoAoClicar[0]);
        botaoFacebook.setBackgroundResource(trocaBotaoAoClicar[1]);
        botaoTwitter.setBackgroundResource(trocaBotaoAoClicar[2]);
    }
}

