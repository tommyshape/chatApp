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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Collator;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    FriendsAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    CollectionReference experiment;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = v.findViewById(R.id.friendsRecyclerView);

        userID = fAuth.getCurrentUser().getUid();

        experiment = db.collection("Terminal-user-credentials").document(userID).collection("friends-of");

        setUpRecyclerView();

        return v;
    }

    public void setUpRecyclerView(){

        Query query = experiment.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<People> options = new FirestoreRecyclerOptions.Builder<People>()
                .setQuery(query, People.class)
                .build();
        adapter = new FriendsAdapter(options);
        //recyclerView = v.findViewById(R.id.discoverRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

        //on click

        final People people = documentSnapshot.toObject(People.class);
        final String name = people.getName();
        final String email = people.getEmail();
        final String Uid = people.getUid();




                /*ChatFragment.user_chat.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String emailKeeper = "";

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                            People people1 = documentSnapshot.toObject(People.class);

                            String emaill = people1.getEmail();

                            emailKeeper += emaill + " ";

                        }

                        if (!emailKeeper.contains(email)){
                            ChatFragment.user_chat.add(new People(name, email, Uid))
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getActivity(), "Added to friends", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "Failed to add friend", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(getActivity(), "Already in chat list", Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/


                Intent intent = new Intent(getActivity(), Bubble.class);

                String myUid = userID;
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
