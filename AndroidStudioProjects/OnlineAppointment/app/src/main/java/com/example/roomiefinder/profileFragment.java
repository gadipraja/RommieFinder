package com.example.roomiefinder;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

    private String TAG = "profileFragment";
    private String text;
    private String status = "Add";
    private String t,d,p,a;
    private DocumentReference docref;

    private TextView listing;
    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button sign_out = view.findViewById(R.id.signout);
        final Button edit = view.findViewById(R.id.edit_button);
        listing = view.findViewById(R.id.listing);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user != null)
        {
            text = "You haven't list anything, click EDIT to add listing";
            String name = user.getDisplayName();
            TextView username = view.findViewById(R.id.name_placeholder);
            username.setText(name);

            sign_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(view.getContext(),loginActivity.class);
                    startActivity(intent);
                }
            });
            db.collection("list")
                    .whereEqualTo("posted_by",user.getEmail())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            t = document.get("title").toString();
                            d = document.get("desc").toString();
                            p = document.get("price").toString();
                            a = document.get("addr").toString();
                            text = "Title: " + t + "\n"+
                                    "Description: " + d + "\n" +
                                    "Address: " + a + "\n" +
                                    "Price: $" + p + "\n";
                            status = "Edit";
                            docref = document.getReference();
                        }
                        listing.setText(text);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        LayoutInflater inflater = requireActivity().getLayoutInflater();

                        final View editor = inflater.inflate(R.layout.listing,null);

                        builder.setView(editor);
                        final EditText title = editor.findViewById(R.id.title);
                        final EditText desc = editor.findViewById(R.id.description);
                        final EditText addr = editor.findViewById(R.id.address);
                        final EditText price = editor.findViewById(R.id.price);

                        if(status.equals("Edit"))
                        {
                            title.setText(t);
                            desc.setText(d);
                            addr.setText(a);
                            price.setText(p);

                        }

                        // Set up the buttons
                        builder.setPositiveButton(status, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (title.getText().toString().trim().length() > 0 &&
                                        addr.getText().toString().trim().length() > 0 &&
                                        desc.getText().toString().trim().length() > 0 &&
                                        price.getText().toString().trim().length() > 0 ) {
                                    Map<String, Object> nr = new HashMap<>();
                                    nr.put("title", title.getText().toString());
                                    nr.put("desc", desc.getText().toString());
                                    nr.put("addr", addr.getText().toString());
                                    nr.put("price", price.getText().toString());
                                    nr.put("posted_by", user.getEmail());
                                    if(status.equals("Add")) {
                                        db.collection("list")
                                                .add(nr);
                                    }
                                    else{
                                        docref.update(nr);
                                    }
                                    db.collection("list")
                                            .whereEqualTo("posted_by",user.getEmail())
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    text = "Title: " + document.get("title") + "\n"+
                                                            "Description: " + document.get("desc") + "\n" +
                                                            "Address: " + document.get("addr") + "\n" +
                                                            "Price: $" + document.get("price") + "\n";
                                                    status = "Edit";
                                                }
                                                listing.setText(text);
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        }
                                    });
                                }
                                else {
                                    Context context = view.getContext();
                                    CharSequence text = "Error: You have some empty box";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                docref.delete();
                                text = "You haven't list anything, click EDIT to add listing";
                                listing.setText(text);
                            }
                        });

                        builder.show();
                    }
                });
        }
        return view;
    }



}
