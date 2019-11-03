package com.example.roomiefinder;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class calendarFragment extends Fragment {


    private static final String TAG = "MainActivity";
    private recycler_adapter_todo todo_adapter;
    private recycler_adapter_rlist rlist_adapter;

    public calendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        Button ar = rootView.findViewById(R.id.adderrand);
        Button clear = rootView.findViewById(R.id.clearerrand);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Query query = FirebaseFirestore.getInstance()
                .collection("Todo_list");
        FirestoreRecyclerOptions<errand> options = new FirestoreRecyclerOptions.Builder<errand>()
                .setQuery(query, errand.class)
                .build();
        todo_adapter = new recycler_adapter_todo(options);
        RecyclerView recyclerView = rootView.findViewById(R.id.todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(todo_adapter);


        Query query2 = FirebaseFirestore.getInstance()
                .collection("Completed_list")
                .limit(50);
        FirestoreRecyclerOptions<errand> options2 = new FirestoreRecyclerOptions.Builder<errand>()
                .setQuery(query2, errand.class)
                .build();
        rlist_adapter = new recycler_adapter_rlist(options2);
        RecyclerView recyclerView1 = rootView.findViewById((R.id.roommate_list));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView1.setAdapter(rlist_adapter);

        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setTitle("New Errand");

                // Set up the input
                final EditText input = new EditText(rootView.getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getText().toString().trim().length() <= 0)
                        {
                            Context context = view.getContext();
                            CharSequence text = "Error: new Errand is empty!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else {
                            Map<String, Object> nr = new HashMap<>();
                            nr.put("errand", input.getText().toString());
                            db.collection("Todo_list")
                                    .add(nr);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Completed_list")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                    }
                                }
                            }
                        });
            }
        });
         return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        todo_adapter.startListening();
        rlist_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        todo_adapter.stopListening();
        rlist_adapter.stopListening();
    }
}
