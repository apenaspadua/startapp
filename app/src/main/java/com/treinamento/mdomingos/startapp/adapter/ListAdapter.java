package com.treinamento.mdomingos.startapp.adapter;

/**
 * Created by Matheus de Padua on 27/03/2019.
 * mdomingos@luxfacta.com
 * For Luxfacta Soluções de TI
 * {@see more in https://www.luxfacta.com}
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.treinamento.mdomingos.startapp.R;


//public class ListAdapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
//    private Context context;
//    private List<Despesas> despesasLista;
//    private static Adapter adapter;
//
//    public static synchronized Adapter getInstance(){
//        if( adapter == null) adapter = new Adapter();
//
//        return adapter;
//    }
//
//    public Adapter(){};
//
//
//    public Adapter(Context context, List<Despesas> despesasLista){
//        this.context = context;
//        this.despesasLista = despesasLista;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//        View itemView = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
//        ViewHolder viewHolder = new ViewHolder(itemView);
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
//        final Despesas despesas = despesasLista.get(position);
//
//        viewHolder.descricao.setText(despesas.getDescricao());
//        viewHolder.tipo.setText(despesas.getTipo());
//        viewHolder.valor.setText("R$ " + despesas.getValor().toString());
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DetalhesActivity.class);
//                intent.putExtra("expense", despesas);
//                context.startActivity(intent);
//            }
//        });
//
//
//        viewHolder.menuEditar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, EditarActivity.class);
//                intent.putExtra("expense", despesas);
//                context.startActivity(intent);
//                ((Activity)context).finish();
//            }
//        });
//        viewHolder.menuApagar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Database db = Database.getInstance(context);
//                db.despesaDAO().deleteExpense(despesas);
//
//                despesasLista.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, despesasLista.size());
//            }
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return despesasLista.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView descricao, tipo, valor;
//        public ImageView menuEditar, menuApagar;
//
//
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            descricao = itemView.findViewById(R.id.item_descricao);
//            tipo = itemView.findViewById(R.id.item_tipo);
//            valor = itemView.findViewById(R.id.item_valor);
//            menuEditar = itemView.findViewById(R.id.menu_editarr);
//            menuApagar = itemView.findViewById(R.id.menu_apagar);
//
//        }
//    }
//
//
//}
