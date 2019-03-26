package com.treinamento.mdomingos.startapp.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.utils.Validator;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText usernane, passowrd;
    private RelativeLayout botaoLogin, botaoFacebook,  botaoTwitter;
    private TextView botaoTextEsqueceuSenha, botaoCadastrese;

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
        botaoTextEsqueceuSenha = findViewById(R.id.esqueceu_senha);
        botaoFacebook = findViewById(R.id.botao_facebook_id);
        botaoTwitter = findViewById(R.id.botao_twitter_id);
        botaoCadastrese = findViewById(R.id.faca_cadastro_text_id);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Alterar buttons + functions
        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();

                String email = usernane.getText().toString();
                String senha = passowrd.getText().toString();

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if (activeNetwork != null) { // conectado a internet

                    if (Validator.stringEmpty(email)) {
                        usernane.setError("Insira seu email");
                        onResume();

                    } else if (Validator.stringEmpty(senha)) {
                        passowrd.setError("Insira sua senha");
                        onResume();

                    } else {

                        //progressBar

                        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (firebaseUser.isEmailVerified()) {
                                        Intent intent = new Intent(LoginActivity.this, SlidesPosCadastroActivity.class);
                                        Log.i("userLogado", "Logado com sucesso!!!");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        //progressBar ivisible
                                        Toast.makeText(LoginActivity.this, "Verifique seu email", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Log.i("userLogado", "Falha ao Logar!!!");
                                    new AlertDialog.Builder(LoginActivity.this).setTitle("Falha ao Logar").
                                            setMessage("Usuário ou senha incorretos.").setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                //progressBar invisible
                            }
                        });

                    }

                } else {
                    Log.i("sem internet", "sem conexao");
                    Toast.makeText(LoginActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botaoFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        botaoTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        botaoTextEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoTextEsqueceuSenha.setTextColor(Color.parseColor("#0289BE"));

            }
        });

        botaoCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoCadastrese.setTextColor(Color.parseColor("#0289BE"));
                Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        botaoTextEsqueceuSenha.setTextColor(Color.parseColor("#A7A7A7"));
        botaoCadastrese.setTextColor(Color.parseColor("#A7A7A7"));

    }
}
