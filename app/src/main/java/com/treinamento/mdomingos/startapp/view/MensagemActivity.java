package com.treinamento.mdomingos.startapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.adapter.MessageAdapter;
import com.treinamento.mdomingos.startapp.model.Chat;
import com.treinamento.mdomingos.startapp.model.Usuarios;
import com.treinamento.mdomingos.startapp.service.APIService;
import com.treinamento.mdomingos.startapp.service.notifications.Client;
import com.treinamento.mdomingos.startapp.service.notifications.Data;
import com.treinamento.mdomingos.startapp.service.notifications.MyResponse;
import com.treinamento.mdomingos.startapp.service.notifications.Sender;
import com.treinamento.mdomingos.startapp.service.notifications.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MensagemActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    ImageView back;

    FirebaseUser user;
    DatabaseReference databaseReference;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;

    ValueEventListener seenListener;

    String userid;

    APIService apiService;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        Toolbar toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.image_profile_message_chat);
        username = findViewById(R.id.username_message);
        back = findViewById(R.id.back_chat);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        user = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(user.getUid(), userid, msg);
                } else {
                    Toast.makeText(MensagemActivity.this, "Digite algo", Toast.LENGTH_SHORT).show();
                }

                text_send.setText("");
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                username.setText(usuarios.getNome());

                if (usuarios.getPerfil() == 1) {
                    if (usuarios.getFoto_perfil() == null) {
                        profile_image.setImageResource(R.drawable.investidor_icon2);
                    } else {
                        Glide.with(getApplicationContext()).load(usuarios.getFoto_perfil()).into(profile_image);
                    }

                } else if (usuarios.getPerfil() == 2) {

                    if (usuarios.getFoto_perfil() == null) {
                        profile_image.setImageResource(R.drawable.startup_icon2);
                    } else {
                        Glide.with(getApplicationContext()).load(usuarios.getFoto_perfil()).into(profile_image);
                    }
                }

                readMessage(user.getUid(), userid, usuarios.getFoto_perfil());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        seenMessage(userid);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void seenMessage(final String userid){

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(user.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendMessage(String sender, final String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(user.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(user.getUid());
        chatRefReceiver.child("id").setValue(user.getUid());

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                if (notify) {
                    sendNotifiaction(receiver, usuarios.getNome(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                   Token token = snapshot.getValue(Token.class);
                    Data data = new Data(user.getUid(), R.mipmap.ic_logo_app, username+": "+message, "Você tem uma nova mensagem",
                            userid);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MensagemActivity.this, "Falha!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void readMessage(final String myid, final String userid, final String imageurl){

        mchat = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MensagemActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
    }
}
