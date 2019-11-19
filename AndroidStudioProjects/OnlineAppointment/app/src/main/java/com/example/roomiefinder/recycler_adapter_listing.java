package com.example.roomiefinder;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class recycler_adapter_listing extends FirestoreRecyclerAdapter<lookup, recycler_adapter_listing.Holder> {

    private Uri u;

    public recycler_adapter_listing(@NonNull FirestoreRecyclerOptions<lookup> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int i, @NonNull lookup s) {
        holder.title.setText(String.valueOf(s.getTitle()));
        holder.price.setText("$"+String.valueOf(s.getPrice()));
        holder.addr.setText(String.valueOf(s.getAddr()));
        holder.desc.setText(String.valueOf(s.getDesc()));
        Picasso.with(holder.v.getContext()).load(s.getUrl()).into(holder.iv);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_listing,parent,false);
        return new Holder(v);
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView title;
        TextView price;
        TextView addr;
        TextView desc;
        ImageView iv;
        View v;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            addr = itemView.findViewById(R.id.address);
            desc = itemView.findViewById(R.id.description);
            iv = itemView.findViewById(R.id.image);
            v = itemView;
        }
    }
}