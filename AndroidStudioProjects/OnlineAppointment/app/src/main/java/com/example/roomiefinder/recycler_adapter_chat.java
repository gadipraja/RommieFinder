package com.example.roomiefinder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class recycler_adapter_chat extends FirestoreRecyclerAdapter<chat, recycler_adapter_chat.Holder> {

    public recycler_adapter_chat(@NonNull FirestoreRecyclerOptions<chat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int i, @NonNull chat s) {
        holder.msg.setText(String.valueOf(s.getMsg()));
        holder.time.setText(String.valueOf(s.getTime()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if(Objects.equals(user.getEmail(), String.valueOf(s.getSender())))
        {
            holder.box.setCardBackgroundColor(ContextCompat.getColor(holder.box.getContext(), R.color.lime_green));
            holder.box.setLeft(70);
        }
        else
        {
            holder.box.setCardBackgroundColor(ContextCompat.getColor(holder.box.getContext(), R.color.ivory));
            holder.box.setRight(70);
        }

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row,parent,false);
        return new Holder(v);
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView msg;
        TextView time;
        CardView box;
        public Holder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.chat);
            time = itemView.findViewById(R.id.time);
            box = itemView.findViewById(R.id.chat_box);
        }
    }
}