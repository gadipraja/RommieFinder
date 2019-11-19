package com.example.roomiefinder;


import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firestore.v1.DocumentTransform;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private recycler_adapter_chat list;
    private ImageButton send;
    private EditText msg;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        msg = rootView.findViewById(R.id.newmsg);
        send = rootView.findViewById(R.id.send);
        msg.setFocusableInTouchMode(true);
        Query query = FirebaseFirestore.getInstance()
                .collection("chat")
                .orderBy("timestamp")
                .limit(100);
        FirestoreRecyclerOptions<chat> options = new FirestoreRecyclerOptions.Builder<chat>()
                .setQuery(query, chat.class)
                .build();
        list = new recycler_adapter_chat(options);
        final RecyclerView recyclerView = rootView.findViewById(R.id.chatbox);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(list);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(list.getItemCount()-1);
            }
        }, 200);
        final View activityRootView = rootView.getRootView();
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                activityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(list.getItemCount()-1);
                        }
                    }, 200);                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(msg.getText().toString().trim().length() > 0) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String currentDateandTime = sdf.format(new Date());
                    Map<String, Object> nr = new HashMap<>();
                    nr.put("msg", msg.getText().toString());
                    nr.put("sender",user.getEmail());
                    nr.put("senderName",user.getDisplayName());
                    nr.put("time", currentDateandTime);
                    nr.put("group","group_test1");
                    nr.put("timestamp", Timestamp.now());
                    db.collection("chat")
                            .add(nr);

                    msg.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(list.getItemCount()-1);
                            Log.d("Hello World", String.valueOf(list.getItemCount()));
                        }
                    }, 200);
                }
            }
        });        return rootView;
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
