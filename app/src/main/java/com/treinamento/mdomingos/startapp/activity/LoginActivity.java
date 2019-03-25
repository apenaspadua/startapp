package com.treinamento.mdomingos.startapp.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.utils.Validator;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText usernane;
    private EditText passowrd;
    private RelativeLayout botaoLogin;
    private RelativeLayout botaoFacebook;
    private RelativeLayout botaoTwitter;
    private TextView botaoTextCadastrese;
    int i = 0;
    final int [] trocaBotaoAoClicar = {
            R.drawable.button_ronded,
            R.drawable.button_custom_facebook,
            R.drawable.button_custom_twitter,
            R.drawable.button_rounded_precionado,
            R.drawable.button_rounded_precionado_red
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.dropdown_menu_login, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instance
        usernane = findViewById(R.id.emailLogin_id);
        passowrd = findViewById(R.id.senhaLogin_id);
        botaoLogin = findViewById(R.id.botao_login_id);
        botaoTextCadastrese = findViewById(R.id.cadastrese_text_id);
        botaoFacebook = findViewById(R.id.botao_facebook_id);
        botaoTwitter = findViewById(R.id.botao_twitter_id);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


            retornaButton();

            //Alterar buttons + functions
            botaoLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retornaButton();

                    String email = usernane.getText().toString();
                    String senha = passowrd.getText().toString();

                    i = 4;
                    botaoLogin.setBackgroundResource(trocaBotaoAoClicar[i]);

                    if (Validator.stringEmpty(email)) {
                        usernane.setError("Insira seu email");
                        retornaButton();

                    } else if (Validator.stringEmpty(senha)) {
                        passowrd.setError("Insira sua senha");
                        retornaButton();

                    } else {

                        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                firebaseAuth = FirebaseAuth.getInstance();
                                firebaseUser = firebaseAuth.getCurrentUser();

                                if (task.isSuccessful()) {
                                    if (firebaseUser.isEmailVerified()) {
                                        Intent intent = new Intent(LoginActivity.this, SlidesPosCadastroActivity.class);
                                        Log.i("userLogado", "Logado com sucesso!!!");
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Verifique seu email", Toast.LENGTH_LONG).show();

                                    }


                                } else {
                                    Log.i("userLogado", "Falha ao Logar!!!");
                                    Toast.makeText(LoginActivity.this, "Usuario ou senha incorretos!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


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
            botaoTextCadastrese.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    botaoTextCadastrese.setTextColor(Color.parseColor("#0289BE"));
                    Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                    startActivity(intent);
                }
            });

    }


    public void retornaButton () {
        botaoLogin.setBackgroundResource(trocaBotaoAoClicar[0]);
        botaoFacebook.setBackgroundResource(trocaBotaoAoClicar[1]);
        botaoTwitter.setBackgroundResource(trocaBotaoAoClicar[2]);
    }
}
