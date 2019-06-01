package com.treinamento.mdomingos.startapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.Usuarios;

public class ConfigActivity extends AppCompatActivity {

    private RelativeLayout botaoDeletar;
    private String email, senha;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
        botaoDeletar = findViewById(R.id.botao_deletar_conta_id);

        botaoDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ConfigActivity.this).setTitle("Alerta").
                        setMessage("Deseja realmente apagar esta conta?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      deleteAccount();
                    }
                }).show();
            }
        });

    }

    private void deleteAccount() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Usuarios").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                email = usuarios.getEmail();
                senha = usuarios.getPassword();

                AuthCredential credential = EmailAuthProvider.getCredential(email, senha);

                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    mAuth.signOut();

                                    Toast.makeText(ConfigActivity.this, "Conta deletada!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ConfigActivity.this, InicialActivity.class));
                                    deleteUser();
                                    finish();
                                }else{
                                    Toast.makeText(ConfigActivity.this, "Falha ao deletar conta!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteUser() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Usuarios").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            deletePubli();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void deletePubli() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Publicacoes").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });
    }

}
