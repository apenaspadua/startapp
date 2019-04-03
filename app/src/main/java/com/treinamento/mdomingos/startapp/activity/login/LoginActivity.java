package com.treinamento.mdomingos.startapp.activity.login;


import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.treinamento.mdomingos.startapp.R;

import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentInvestidor;
import com.treinamento.mdomingos.startapp.activity.investidor.CadastroInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.startup.CadastroStartupActivity;
import com.treinamento.mdomingos.startapp.model.Usuarios;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.Validator;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText usernane, passowrd;
    private RelativeLayout botaoLogin, botaoFacebook,  botaoTwitter;
    private TextView botaoTextEsqueceuSenha, botaoCadastrese;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;


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
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressBar = findViewById(R.id.progressBarLogin);


        firebaseAuth = FirebaseAuth.getInstance();




        //Alterar buttons + functions
        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();

                final String email = usernane.getText().toString();
                final String senha = passowrd.getText().toString();

                if(FirebaseConection()) {
                    if (Validator.stringEmpty(email)) {
                        usernane.setError("Insira seu email");
                        onResume();

                    } else if (Validator.stringEmpty(senha)) {
                        passowrd.setError("Insira sua senha");
                        onResume();

                    } else {

                        progressDialog.setMessage("Logando...");
                        progressDialog.show();

                        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    if (firebaseUser != null && firebaseUser.isEmailVerified()) {

                                        DatabaseReference databaseReference = FirebaseConfig.getFirebase();
                                        databaseReference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                Usuarios usuario =  dataSnapshot.getValue(Usuarios.class);

                                                if(usuario.getPerfil() == 1){
                                                    if(usuario.getDetalhes_completo() == 0 ) {
                                                        Intent intent = new Intent(LoginActivity.this, CadastroInvestidorActivity.class);
                                                        startActivity(intent);
                                                        progressDialog.dismiss();
                                                        finish();
                                                    } else {
                                                        Intent intent = new Intent(LoginActivity.this, BaseFragmentInvestidor.class);
                                                        startActivity(intent);
                                                        progressDialog.dismiss();
                                                        finish();
                                                    }
                                                } else if (usuario.getPerfil() == 2){
                                                    Intent intent = new Intent(LoginActivity.this, CadastroStartupActivity.class);
                                                    startActivity(intent);
                                                    progressDialog.dismiss();
                                                    finish();
                                                }
                                                else {

                                                    new AlertDialog.Builder(LoginActivity.this).setTitle("Alerta").
                                                            setMessage("Sem perfil.").setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            progressDialog.dismiss();
                                                        }
                                                    }).show();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        new AlertDialog.Builder(LoginActivity.this).setTitle("Você ainda não validou sua conta").
                                                setMessage("Vá ao seu email para realizar a confirmação.").setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                progressDialog.dismiss();
                                            }
                                        }).show();
                                    }
                                } else {
                                    Log.i("userLogado", "Falha ao Logar!!!");
                                    new AlertDialog.Builder(LoginActivity.this).setTitle("Falha ao Logar").
                                            setMessage("Usuário ou senha incorretos.").setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            progressDialog.dismiss();
                                        }
                                    }).show();
                                }

                           }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
                    }

                } else { // sem conexao
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

                if(!usernane.getText().toString().equals("")){
                    final AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog));
                    alerta.setMessage("Há limites de vezes para alteração da senha. Deseja continuar?");
                    alerta.setCancelable(true);
                    alerta.setPositiveButton(getResources().getString(R.string.alertSim), new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            usernane.setEnabled(false);
                            progressBar.setVisibility(View.VISIBLE);
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.sendPasswordResetEmail(usernane.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Email de redefinição enviado", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        usernane.setEnabled(true);

                                        onResume();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Falha na solicitação deste email", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        usernane.setEnabled(true);
                                        onResume();
                                    }
                                }
                            });
                        }
                    });

                    alerta.setNegativeButton(getResources().getString(R.string.alertNao), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onResume();
                            return;
                        }
                    });

                    AlertDialog alertDialog = alerta.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(LoginActivity.this, "Insira seu email", Toast.LENGTH_SHORT).show();
                    onResume();
                }
            }
        });

        botaoCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botaoCadastrese.setTextColor(Color.parseColor("#0289BE"));
                Intent intent = new Intent(LoginActivity.this, CadastroLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        botaoTextEsqueceuSenha.setTextColor(Color.parseColor("#A7A7A7"));
        botaoCadastrese.setTextColor(Color.parseColor("#A7A7A7"));

    }

    public boolean FirebaseConection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) // conectado a internet
            return true;
        return false; // nao conectado
    }
}
