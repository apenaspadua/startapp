package com.treinamento.mdomingos.startapp.activity.startup;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.home.BaseFragmentStartup;
import com.treinamento.mdomingos.startapp.model.Publicacao;
import com.treinamento.mdomingos.startapp.utils.UploadStorage;

public class EnviaArquivosActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 3;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Uri videoUri;

    private RelativeLayout botaoSalvar, pegaFoto, pegaVideo;
    private FirebaseAuth firebaseAuth;
    private TextView nomeVideo;

    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private ImageView foto;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Task<Uri> storageReferenceUp;
    private StorageReference storageReferenceVideo;
    private String imageURL;
    private String videoName;

    @Override
    protected void onResume() {
        super.onResume();

        loadUserInformation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envia_arquivos);

        botaoSalvar = findViewById(R.id.botao_salvar_arquivos_id);
        pegaFoto = findViewById(R.id.seleciona_imagem_envia_arquivo_id);
        pegaVideo = findViewById(R.id.adicionar_video_publicacao_id);
        nomeVideo = findViewById(R.id.nome_video_id);
        foto = findViewById(R.id.imagem_envia_aquivos_id);
        progressBar = findViewById(R.id.progressBar_envia_imagem_id);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        storageReferenceVideo = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        pegaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        pegaVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo(v);
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Salvando alterações...");
                startActivity(new Intent(EnviaArquivosActivity.this, BaseFragmentStartup.class));
                finish();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void uploadVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona um video"), PICK_VIDEO_REQUEST);
    }

    public void uploadToServer(View view){
        progressDialog.setTitle("Aguarde enquanto carregamos seu vídeo...");
        progressDialog.setMessage("A conexao por wifi pode ajudar nesse processo");
        progressDialog.show();

        StorageReference fileReference = storageReferenceVideo.child("video_publicacao/" + firebaseUser.getUid());
        fileReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                if (urlTask.isSuccessful()) {

                    Uri downloadUrl = urlTask.getResult();
                    final String downUrl = String.valueOf(downloadUrl);
                    UploadStorage uploadStorage = new UploadStorage(downUrl);
                  //  databaseReference.child("Usuarios").child(firebaseUser.getUid()).child("detalhe_startup").child("video_publicado").setValue(uploadStorage);

                }
            }

        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    nomeVideo.setText(videoName);
                    nomeVideo.setTextColor(Color.parseColor("#0289BE"));
                    UploadTask.TaskSnapshot downloadUri = task.getResult();
                    Toast.makeText(EnviaArquivosActivity.this, "Video adicionado", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    if (downloadUri == null)
                        progressDialog.dismiss();
                        return ;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EnviaArquivosActivity.this, "Falha ao upar video!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.setTitle("Aguarde enquanto carregamos seu vídeo...");
                progressDialog.show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         View view = null;

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData() != null){
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(foto);
            uploadFile();
        }

        if(requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null){
            videoUri = data.getData();
            videoName = getFileName(videoUri);
            uploadToServer(null);
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
            StorageReference fileReference = storageReference.child("foto_publicacao/"+ firebaseUser.getUid());
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    final String downUrl = String.valueOf(downloadUrl);
//                    UploadStorage uploadStorage = new UploadStorage(downUrl);
//                    databaseReference.child("Publicacoes").child(firebaseUser.getUid()).child("foto_publicacao").setValue(uploadStorage);

                    Publicacao publicacao = new Publicacao();
                    publicacao.setFotoPublicacao(downUrl);
                    publicacao.salvarFotoPublicacao(firebaseUser.getUid());

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EnviaArquivosActivity.this, "Imagem Adicionada", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EnviaArquivosActivity.this,"Erro ao alterar imagem!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
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

    public String getFileName(Uri uri){
        String result = null;

        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try{
                if(cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if(result == null){

            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return  result;
    }

    private void loadUserInformation() {
        progressBar.setVisibility(View.VISIBLE);
        storageReferenceUp = FirebaseStorage.getInstance().getReference().child("foto_publicacao").
                child(firebaseUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageURL = uri.toString();
//                Glide.with(EnviaArquivosActivity.this).load(imageURL).into(foto);
                Picasso.with(EnviaArquivosActivity.this).load(imageURL).fit().centerCrop().into(foto);
                progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }
}
