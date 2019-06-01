package com.treinamento.mdomingos.startapp.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.InvestidorResponse;
import com.treinamento.mdomingos.startapp.model.Usuarios;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilVisitadoInvestidorActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Task<Uri> storageReference;
    private FirebaseUser user;
    private String imageURL;
    private ProgressBar progressBar;
    private TextView nome, cidade, razao, email, rua, bairro, estado, telefone, bio, apresentacao, text;
    private CircleImageView foto;
    private RelativeLayout iniciarConversa, chamarAtencao, sim, nao;
    private FirebaseStorage storage;
    private String id;
    private String pegaNome;

    @Override
    protected void onStart() {
        super.onStart();
        loadUserInformation();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_visitado_investidor);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();

        nome = findViewById(R.id.nome_perfilperfilVistado_investidor_id);
        cidade = findViewById(R.id.text_cidade_perfilVistado_investidor);
        razao = findViewById(R.id.text_empresa_perfilVistado_investidor);
        email = findViewById(R.id.text_apresentacao_perfilVistado_investidor);
        rua = findViewById(R.id.rua_perfilVistado_investidor_id);
        bairro = findViewById(R.id.bairro_perfilVistado_investidor_id);
        estado = findViewById(R.id.estado_perfilVistado_investidor_id);
        telefone = findViewById(R.id.telefone_perfilVistado_investidor_id);
        bio = findViewById(R.id.text_biografia_perfilVistado_investidor);
        apresentacao = findViewById(R.id.text_apresentacao_perfilVistado_investidor);
        foto = findViewById(R.id.foto_perfilVistado_investidor_id);
        progressBar = findViewById(R.id.progressBar_perfilVistado_investidor);
        progressDialog = new ProgressDialog(this);
        iniciarConversa = findViewById(R.id.botao_iniciar_conversa_perfilVisitado_investidor_id);
        chamarAtencao = findViewById(R.id.botao_chamar_atencao_investidor_id);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = extras.getString("notificacaoInvestidor");

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    InvestidorResponse investidorResponse = dataSnapshot.getValue(InvestidorResponse.class);
                    nome.setText(investidorResponse.getDetalhe_investidor().getNome());
                    pegaNome = investidorResponse.getDetalhe_investidor().getNome();
                    cidade.setText(investidorResponse.getDetalhe_investidor().getCidade());
                    razao.setText(investidorResponse.getDetalhe_investidor().getEmpresa());
                    email.setText(investidorResponse.getDetalhe_investidor().getEmail());
                    rua.setText(investidorResponse.getDetalhe_investidor().getRua());
                    bairro.setText(investidorResponse.getDetalhe_investidor().getBairro());
                    estado.setText(investidorResponse.getDetalhe_investidor().getEstado());
                    telefone.setText(investidorResponse.getDetalhe_investidor().getTelefone());
                    bio.setText(investidorResponse.getDetalhe_investidor().getBiografia());
                    apresentacao.setText(investidorResponse.getDetalhe_investidor().getApresentacao());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });
        }

        iniciarConversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilVisitadoInvestidorActivity.this, UsersChatActivity.class);
                intent.putExtra("conversa", pegaNome);
                startActivity(intent);
            }
        });

        chamarAtencao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Usuarios").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);

                        if (usuarios.getPerfil() == 2) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(PerfilVisitadoInvestidorActivity.this);
                            final View view = LayoutInflater.from(PerfilVisitadoInvestidorActivity.this).inflate(R.layout.alert_dialog_interesse, null);

                            sim = view.findViewById(R.id.botao_sim);
                            nao = view.findViewById(R.id.botao_nao);
                            text = view.findViewById(R.id.textInteresse);
                            text.setText("Deseja mandar uma notificação?");

                            sim.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(PerfilVisitadoInvestidorActivity.this, EnvioNotifyInvestidorActivity.class);
                                    intent.putExtra("notificacaoInvestidor", id);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            nao.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });

                            builder.setView(view);
                            builder.show();
                        }
                        else {
                            startActivity(new Intent(PerfilVisitadoInvestidorActivity.this, SejaUmInvestidorActivity.class));
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    private void loadUserInformation() {
        storageReference = FirebaseStorage.getInstance().getReference().child("foto_perfil").
                child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                imageURL = uri.toString();
                Glide.with(PerfilVisitadoInvestidorActivity.this).load(imageURL).into(foto);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
