package com.treinamento.mdomingos.startapp.activity.startup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentStartup;
import com.treinamento.mdomingos.startapp.model.CEP;
import com.treinamento.mdomingos.startapp.model.InvestidorResponse;
import com.treinamento.mdomingos.startapp.model.Startup;
import com.treinamento.mdomingos.startapp.model.StartupResponse;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.HttpService;
import com.treinamento.mdomingos.startapp.utils.MaskFormatter;
import com.treinamento.mdomingos.startapp.utils.Validator;

import java.util.concurrent.ExecutionException;

public class EditarPerfilStartup extends AppCompatActivity {

    private EditText razaoSocial, nomeFantasia, email, telefone, cep, cnpj, rua, cidade, bairro, estado, bio;
    private RelativeLayout botaoConcluir;
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private ImageView foto, icone;
    private StorageReference mStorage;


    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StartupResponse startup = dataSnapshot.getValue(StartupResponse.class);
                nomeFantasia.setText(startup.getDetalhe_startup().getNomeFantasia());
                cidade.setText(startup.getDetalhe_startup().getCidade());
                razaoSocial.setText(startup.getDetalhe_startup().getRazaoSocial());
                email.setText(startup.getDetalhe_startup().getEmail());
                rua.setText(startup.getDetalhe_startup().getRua());
                bairro.setText(startup.getDetalhe_startup().getBairro());
                estado.setText(startup.getDetalhe_startup().getEstado());
                telefone.setText(startup.getDetalhe_startup().getTelefone());
                bio.setText(startup.getDetalhe_startup().getBiografia());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_startup);

        razaoSocial = findViewById(R.id.razao_editar_startup_id);
        nomeFantasia = findViewById(R.id.nome_editar_startup_id);
        email = findViewById(R.id.email_editar_startup_id);
        telefone = findViewById(R.id.telefone_editar_startup_id);
        cep = findViewById(R.id.cep_editar_startup_id);
        cnpj = findViewById(R.id.cnpj_editar_startup_id);
        rua = findViewById(R.id.rua_editar_startup_id);
        cidade = findViewById(R.id.cidade_editar_startup_id);
        bairro = findViewById(R.id.bairro_editar_startup_id);
        estado = findViewById(R.id.estado_editar_startup_id);
        bio = findViewById(R.id.biografia_editar_startup_id);
        botaoConcluir = findViewById(R.id.botao_concluir_edicao_startup_id);
        progressDialog = new ProgressDialog(EditarPerfilStartup.this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Mascara para cnpj
        MaskFormatter.simpleFormatterCnpj(cnpj);
        MaskFormatter.simpleFormatterCell(telefone);

        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    CEP retorno = new HttpService(s.toString()).execute().get();

                    if (retorno == null) {
                        cep.setError("Cep inválido");
                    } else {
                        if (retorno.getCidade() != null) {
                            cidade.setText(retorno.getCidade());
                            estado.setText(retorno.getEstado());
                            rua.setText(retorno.getLogradouro());
                            bairro.setText(retorno.getBairro());
                        } else {
                            cep.setError("Cep inválido");
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        botaoConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseConfig.firebaseConection()){

                    final String razaoSocialRecebido = razaoSocial.getText().toString();
                    final String nomeFantasiaRecebido = nomeFantasia.getText().toString();
                    final String emailRecebido = email.getText().toString();
                    final String telefoneRecebido = telefone.getText().toString();
                    final String cepRecebido = cep.getText().toString();
                    final String ruaRecebida = rua.getText().toString();
                    final String bairroRecebido = bairro.getText().toString();
                    final String cidadeRecebido = cidade.getText().toString();
                    final String estadoRecebido = estado.getText().toString();
                    final String cnpjRecebido = cnpj.getText().toString();
                    final String bioRecebida = bio.getText().toString();

                    String cnpjSemFormatacao = cnpjRecebido.replace(".", "");
                    cnpjSemFormatacao = cnpjSemFormatacao.replace("/", "");
                    cnpjSemFormatacao = cnpjSemFormatacao.replace("-", "");

                    if (Validator.stringEmpty(bioRecebida)) {
                        bio.setError("Insira uma descrição");

                    }else if (Validator.stringEmpty(razaoSocialRecebido)) {
                        razaoSocial.setError("Insira sua Razão Social");

                    } else if (Validator.stringEmpty(nomeFantasiaRecebido)) {
                        nomeFantasia.setError("Insira o nome da empresa");

                    } else if (Validator.stringEmpty(emailRecebido)) {
                        email.setError("Insira seu email");

                    } else if (Validator.validateEmailFormat(emailRecebido) ==  false) {
                        email.setError("Insira um email válido");

                    } else if (Validator.stringEmpty(telefoneRecebido)) {
                        telefone.setError("Insira um número para contato");

                    } else if (Validator.stringEmpty(cepRecebido)) {
                        cep.setError("Insira seu cep");

                    } else if (Validator.stringEmpty(ruaRecebida)) {
                        rua.setError("Insira seu logradouro");

                    } else if (Validator.stringEmpty(bairroRecebido)) {
                        bairro.setError("Insira seu bairro");

                    } else if (Validator.stringEmpty(cidadeRecebido)) {
                        cidade.setError("Insira sua cidade");

                    } else if (Validator.stringEmpty(estadoRecebido)) {
                        estado.setError("Insira seu estado");

                    } else if (Validator.stringEmpty(cnpjRecebido)) {
                        cnpj.setError("Insira o CNPJ da empresa");

                    } else if (Validator.isCNPJ(cnpjSemFormatacao) ==  false) {
                        cnpj.setError("Insira um CNPJ da válido");

                    } else {

                        Startup startup = new Startup(razaoSocialRecebido, nomeFantasiaRecebido, emailRecebido, telefoneRecebido, cepRecebido, ruaRecebida, bairroRecebido,cidadeRecebido, estadoRecebido, cnpjRecebido);
                        startup.salvarStartup(firebaseUser.getUid());
                        Startup startup1 = new Startup(bioRecebida);
                        startup1.salvarBioStartup(firebaseUser.getUid());
                        progressDialog.setMessage("Guardando dados...");
                        progressDialog.show();
                        Toast.makeText(EditarPerfilStartup.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditarPerfilStartup.this, BaseFragmentStartup.class));
                        finish();
                    }

                }else {
                    Log.i("sem internet", "sem conexao");
                    Toast.makeText(EditarPerfilStartup.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

 }

