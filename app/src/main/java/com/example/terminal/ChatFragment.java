package com.example.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    static CollectionReference user_chat;

    ChatAdapter adapter;
    RecyclerView recyclerView;
    String user_id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        user_id = fAuth.getCurrentUser().getUid();

        user_chat = fStore.collection("Terminal-user-credentials").document(user_id).collection("Chats lister");
        recyclerView = v.findViewById(R.id.chatRecyclerView);

        setUpRecyclerView();

        return v;
    }

    public void setUpRecyclerView(){
        Query query = user_chat.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<People> options = new FirestoreRecyclerOptions.Builder<People>()
                .setQuery(query, People.class)
                .build();
        adapter = new ChatAdapter(options);
        //recyclerView = v.findViewById(R.id.discoverRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {


                final People people = documentSnapshot.toObject(People.class);
                final String name = people.getName();
                final String email = people.getEmail();
                final String Uid = people.getUid();

                Intent intent = new Intent(getActivity(), Bubble.class);

                String myUid = user_id;
                String otherUid = Uid;

                int compare = myUid.compareTo(otherUid);
                String listenPlace = null;

                if (compare < 0){
                    //myUid is smaller
                    listenPlace = myUid + otherUid;
                }
                else if (compare > 0){
                    //myUid is larger
                    listenPlace = otherUid + myUid;
                }

                intent.putExtra("receiver_name", name);
                intent.putExtra("receiver_uid", Uid);
                intent.putExtra("listenPlace", listenPlace);
                intent.putExtra("receiver_email", email);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
