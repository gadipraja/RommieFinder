package com.example.roomiefinder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        final String title = s.getTitle();
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                CharSequence text = title + " is pressed";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
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
        CardView card;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            addr = itemView.findViewById(R.id.addr);
            card = itemView.findViewById(R.id.card);
        }
    }
}