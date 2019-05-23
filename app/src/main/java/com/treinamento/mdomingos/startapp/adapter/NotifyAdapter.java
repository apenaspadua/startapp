package com.treinamento.mdomingos.startapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.treinamento.mdomingos.startapp.R;
import com.treinamento.mdomingos.startapp.activity.investidor.PerfilVisitadoInvestidorActivity;
import com.treinamento.mdomingos.startapp.activity.startup.PerfilVisitadoStartupActivity;
import com.treinamento.mdomingos.startapp.model.Usuarios;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matheus de Padua on 2019-05-23.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */
public class NotifyAdapter extends RecyclerView.ViewHolder {

    View mView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    public NotifyAdapter(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }


    public void setDetails(final Context context, String fotoPerfil, String descricao, final String id){

        TextView descri = mView.findViewById(R.id.nome_startup_notify_id);
        CircleImageView imageProfile = mView.findViewById(R.id.imageview_notify_startup_id);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //set data

        Picasso.with(context).load(fotoPerfil).into(imageProfile);
        descri.setText(descricao);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Usuarios").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);

                        if(usuarios.getPerfil() == 1){

                            Intent intent = new Intent(context, PerfilVisitadoInvestidorActivity.class);
                            intent.putExtra("notificacao",  id);
                            context.startActivity(intent);

                        } else {
                            Intent intent = new Intent(context, PerfilVisitadoStartupActivity.class);
                            intent.putExtra("notificacao",  id);
                            context.startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }


}
