package com.example.terminal;

import android.app.Person;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

public class DiscoverFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference allUsers = db.collection("Terminal-user-credentials");
    CollectionReference ownFriends;

    DiscoverAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    FirebaseAuth fAuth;

    public static String filterText = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_discover, container, false);

        fAuth = FirebaseAuth.getInstance();

        String userID = fAuth.getCurrentUser().getUid();

        ownFriends = db.collection("Terminal-user-credentials").document(userID).collection("friends-of");


        recyclerView = v.findViewById(R.id.discoverRecyclerView);
        searchView = v.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterText = newText;
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        setUpRecyclerView();



        return v;
    }

    public void setUpRecyclerView(){

        Query query = allUsers.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<People> options = new FirestoreRecyclerOptions.Builder<People>()
                .setQuery(query, People.class)
                .build();
        adapter = new DiscoverAdapter(options);
        //recyclerView = v.findViewById(R.id.discoverRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DiscoverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

            final People people = documentSnapshot.toObject(People.class);
            final String name = people.getName();
            final String email = people.getEmail();
            final String Uid = people.getUid();

            //final String email = people.getEmail();//arkadas olarak ekleyecegi kisinin email adresi
            //friends listte var mi diye kontrol etmemiz gerek.
            ownFriends.get()
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
                        ownFriends.add(new People(name, email, Uid))
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
                        Toast.makeText(getActivity(), "Already in friends list", Toast.LENGTH_SHORT).show();
                    }

                    }
                });




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
