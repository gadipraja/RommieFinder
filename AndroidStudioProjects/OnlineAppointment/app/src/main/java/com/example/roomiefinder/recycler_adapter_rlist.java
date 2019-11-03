package com.example.roomiefinder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class recycler_adapter_rlist extends FirestoreRecyclerAdapter<errand, recycler_adapter_rlist.Holder> {

    public recycler_adapter_rlist(@NonNull FirestoreRecyclerOptions<errand> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int i, @NonNull errand s) {
        holder.errand.setText(String.valueOf(s.getErrand()));
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_text,parent,false);
        return new Holder(v);
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView errand;
        public Holder(@NonNull View itemView) {
            super(itemView);
            errand = itemView.findViewById(R.id.text_row);
        }
    }
}