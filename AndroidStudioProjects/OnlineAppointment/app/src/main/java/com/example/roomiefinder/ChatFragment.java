package com.example.roomiefinder;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private recycler_adapter_chat list;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Query query = FirebaseFirestore.getInstance()
                .collection("chat")
                .orderBy("time")
                .limit(100);
        FirestoreRecyclerOptions<chat> options = new FirestoreRecyclerOptions.Builder<chat>()
                .setQuery(query, chat.class)
                .build();
        list = new recycler_adapter_chat(options);
        RecyclerView recyclerView = rootView.findViewById(R.id.queuelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(list);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        list.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        list.stopListening();
    }

}
