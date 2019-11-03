package com.example.roomiefinder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class recycler_adapter extends FirestoreRecyclerAdapter<lookup, recycler_adapter.Holder> {

    public recycler_adapter(@NonNull FirestoreRecyclerOptions<lookup> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int i, @NonNull lookup s) {
        holder.title.setText(String.valueOf(s.getTitle()));
        holder.price.setText("$"+String.valueOf(s.getPrice()));
        holder.addr.setText(String.valueOf(s.getAddr()));
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ads,parent,false);
        return new Holder(v);
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView title;
        TextView price;
        TextView addr;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            addr = itemView.findViewById(R.id.addr);
        }
    }
}