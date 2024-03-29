package com.treinamento.mdomingos.startapp.view;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.model.CEP;
import com.treinamento.mdomingos.startapp.model.Publicacao;
import com.treinamento.mdomingos.startapp.model.Startup;
import com.treinamento.mdomingos.startapp.model.StartupResponse;
import com.treinamento.mdomingos.startapp.model.Usuarios;
import com.treinamento.mdomingos.startapp.service.HttpService;
import com.treinamento.mdomingos.startapp.utils.FirebaseConfig;
import com.treinamento.mdomingos.startapp.utils.MaskFormatter;
import com.treinamento.mdomingos.startapp.utils.Validator;

import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilStartupActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private EditText razaoSocial, nomeFantasia, email, telefone, cep, cnpj, rua, cidade, bairro, estado, bio,  apresentacao, link;
    private RelativeLayout botaoConcluir;
    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private CircleImageView foto;
    private ImageView icone;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Task<Uri> storageReferenceUp;
    private String imageURL;


    @Override
    protected void onResume() {
        super.onResume();
        loadUserInformation();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Usuarios").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                apresentacao.setText(startup.getDetalhe_startup().getApresentacao());
                link.setText(startup.getDetalhe_startup().getLink());
                cep.setText(startup.getDetalhe_startup().getCep());
                cnpj.setText(startup.getDetalhe_startup().getCnpj());
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
        apresentacao = findViewById(R.id.apresentacao_editar_startup_id);
        link = findViewById(R.id.link_editar_startup_id);
        botaoConcluir = findViewById(R.id.botao_concluir_edicao_startup_id);
        progressDialog = new ProgressDialog(EditarPerfilStartupActivity.this);
        foto = findViewById(R.id.foto_editar_startup_id);
        icone = findViewById(R.id.icone_editar_startup_id);
        progressBar = findViewById(R.id.progressBar_editar_startup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressBar.setVisibility(View.VISIBLE);

        //Mascara para cnpj
        MaskFormatter.simpleFormatterCnpj(cnpj);
        MaskFormatter.simpleFormatterCell(telefone);

        //Pegar imagem de perfil
        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

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
                    uploadFile();
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
                    final String apresentacaoRecebido = apresentacao.getText().toString();
                    final String linkRecebido = link.getText().toString();

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
                        Startup startup1 = new Startup(bioRecebida, apresentacaoRecebido, linkRecebido);
                        startup1.salvarBioStartup(firebaseUser.getUid());

                        Usuarios usuarios = new Usuarios();
                        usuarios.setNome(nomeFantasiaRecebido);
                        usuarios.salvarMais(firebaseUser.getUid());

                        Publicacao publicacao = new Publicacao(nomeFantasiaRecebido,cidadeRecebido, estadoRecebido);
                        publicacao.salvarDados(firebaseUser.getUid());
                        publicacao.setDescricao(bioRecebida);
                        publicacao.salvarDescricao(firebaseUser.getUid());

                        progressDialog.setMessage("Guardando dados...");
                        progressDialog.show();
                        Toast.makeText(EditarPerfilStartupActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditarPerfilStartupActivity.this, MainFragmentStartupActivity.class));
                        finish();
                    }

                }else {
                    Log.i("sem internet", "sem conexao");
                    Toast.makeText(EditarPerfilStartupActivity.this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==  PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(foto);
            uploadFile();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

    private void uploadFile(){
        if(imageUri != null){
            progressBar.setVisibility(View.VISIBLE);
            StorageReference fileReference = storageReference.child("foto_perfil/"+ firebaseUser.getUid());
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    final String downUrl = String.valueOf(downloadUrl);

                    Usuarios usuarios = new Usuarios();
                    usuarios.setFoto_perfil(downUrl);
                    usuarios.salvarFotoPerfil(firebaseUser.getUid());

                    Publicacao publicacao = new Publicacao();
                    publicacao.setFotoPerfil(downUrl);
                    publicacao.salvarFotoPerfil(firebaseUser.getUid());

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditarPerfilStartupActivity.this, "Imagem alterada com sucesso", Toast.LENGTH_SHORT).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditarPerfilStartupActivity.this,"Erro ao alterar imagem!", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progress);
                }
            });

        } else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserInformation() {
        storageReferenceUp = FirebaseStorage.getInstance().getReference().child("foto_perfil").
                child(firebaseUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(foto);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainFragmentStartupActivity.class));
        finish();
    }
}

