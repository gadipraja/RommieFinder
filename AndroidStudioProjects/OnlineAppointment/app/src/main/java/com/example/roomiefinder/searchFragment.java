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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class searchFragment extends Fragment {

    private recycler_adapter list_adapter;

    public searchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Query query = FirebaseFirestore.getInstance()
                .collection("list");
        FirestoreRecyclerOptions<lookup> options = new FirestoreRecyclerOptions.Builder<lookup>()
                .setQuery(query, lookup.class)
                .build();
        list_adapter = new recycler_adapter(options);
        RecyclerView recyclerView = rootView.findViewById(R.id.queuelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(list_adapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        list_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        list_adapter.stopListening();
    }
}
