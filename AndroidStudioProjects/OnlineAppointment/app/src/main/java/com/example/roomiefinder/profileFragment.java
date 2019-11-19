package com.example.roomiefinder;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.tasks.Tasks.await;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

    private String TAG = "profileFragment";
    private String status;
    private DocumentReference docref;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri picture;
    private View editor;
    private recycler_adapter_listing list;
    private Uri url;
    private int progress = 100;
    private UploadTask uploadTask;
    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();


        Button sign_out = view.findViewById(R.id.signout);
        final Button edit = view.findViewById(R.id.edit_button);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (user != null)
        {

            Query query = FirebaseFirestore.getInstance()
                    .collection("list")
                    .whereEqualTo("posted_by",user.getEmail());
            FirestoreRecyclerOptions<lookup> options = new FirestoreRecyclerOptions.Builder<lookup>()
                    .setQuery(query, lookup.class)
                    .build();
            list = new recycler_adapter_listing(options);
            RecyclerView recyclerView = view.findViewById(R.id.myListing);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(list);
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

            edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        LayoutInflater inflater = requireActivity().getLayoutInflater();
                        status = "Add";

                        editor = inflater.inflate(R.layout.listing, null);

                        builder.setView(editor);
                        final EditText title = editor.findViewById(R.id.title);
                        final EditText desc = editor.findViewById(R.id.description);
                        final EditText addr = editor.findViewById(R.id.address);
                        final EditText price = editor.findViewById(R.id.price);
                        final ImageButton add = editor.findViewById(R.id.addimage);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                chooseImage();
                            }
                        });
                        db.collection("list")
                                .whereEqualTo("posted_by", user.getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            // Document found in the offline cache
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                title.setText(document.get("title").toString());
                                                desc.setText(document.get("desc").toString());
                                                addr.setText(document.get("addr").toString());
                                                price.setText(document.get("price").toString());
                                                status = "Edit";
                                                docref = document.getReference();
                                                Log.d(TAG,status);
                                            }
                                        }
                                    }
                                });

                        // Set up the buttons
                        builder.setPositiveButton(status, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (title.getText().toString().trim().length() > 0 &&
                                        addr.getText().toString().trim().length() > 0 &&
                                        desc.getText().toString().trim().length() > 0 &&
                                        price.getText().toString().trim().length() > 0 && progress == 100) {
                                    Map<String, Object> nr = new HashMap<>();
                                    if(picture != null && progress == 100)
                                    {
                                        nr.put("url",url.toString());
                                    }
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
                                }
                                else {
                                    uploadTask.cancel();
                                    Context context = view.getContext();
                                    CharSequence text = "Error: You didn't complete the listing";
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
                            }
                        });

                        builder.show();
                    }
                });
        }
        return view;
    }

    private void chooseImage()
    {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== 1  && resultCode == RESULT_OK) {
            picture = data.getData();
            ImageView imageView = editor.findViewById(R.id.imageView);
            uploading();
            imageView.setImageURI(picture);
        }
    }

    public void uploading()
    {
        final StorageReference ref = storageReference.child(UUID.randomUUID().toString());
        uploadTask = ref.putFile(picture);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                Context context = editor.getContext();
                CharSequence text = "Uploading image " + progress + "% complete";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    url = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

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
