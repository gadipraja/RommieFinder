package com.example.roomiefinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class recycler_adapter_todo extends FirestoreRecyclerAdapter<errand, recycler_adapter_todo.Holder>{

    public recycler_adapter_todo(@NonNull FirestoreRecyclerOptions<errand> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int i, @NonNull final errand s) {
        holder.errand.setText(String.valueOf(s.getErrand()));
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .build();
                db.setFirestoreSettings(settings);
                db.collection("Todo_list")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.get("errand").toString().equals(String.valueOf(s.getErrand())))
                                        {
                                            document.getReference()
                                                    .delete();
                                            Map<String, Object> errand = new HashMap<>();
                                            errand.put("errand", String.valueOf(s.getErrand()));

                                            db.collection("Completed_list")
                                                    .add(errand);
                                            break;
                                        }
                                    }
                                }
                            }
                        });
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row,parent,false);
        return new Holder(v);
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView errand;
        Button done;
        public Holder(@NonNull View itemView) {
            super(itemView);
            errand = itemView.findViewById(R.id.row_items);
            done = itemView.findViewById(R.id.button);
        }
    }
}
