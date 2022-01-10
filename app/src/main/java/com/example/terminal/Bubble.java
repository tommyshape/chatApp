package com.example.terminal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Bubble extends AppCompatActivity {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView toolbarTitle;
    private String name;
    private String uid;
    private String email;
    static String myUid;
    private String listenPlace;

    private Button sendButton;
    private EditText messageInput;

    private RecyclerView recyclerView;
    private BubbleAdapter adapter;
    private CollectionReference listen_place;

    private CollectionReference user_chat, opposite_chat;

    private DocumentReference user_name_reference;
    private String name_my, email_my;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        toolbarTitle = findViewById(R.id.toolbarTitle);
        myUid = fAuth.getCurrentUser().getUid();
        recyclerView = findViewById(R.id.BubbleRecyclerView);

        //tikladigi kisinin bilgileri
        Intent intent = getIntent();
        name = intent.getStringExtra("receiver_name");
        uid = intent.getStringExtra("receiver_uid");
        email = intent.getStringExtra("receiver_email");
        listenPlace = intent.getStringExtra("listenPlace");

        toolbarTitle.setText(name);
        //Toast.makeText(this, listenPlace, Toast.LENGTH_SHORT).show();

        //dinleyecegi adresi gonder alfabetik olarak sirala

        listen_place = db.collection(listenPlace);

        setUpRecyclerView();



        user_name_reference = db.collection("Terminal-user-credentials").document(myUid);

        user_name_reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            name_my = (String) documentSnapshot.get("name");
                            email_my = documentSnapshot.get("email").toString();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to get information", Toast.LENGTH_SHORT).show();
                    }
                });






        sendButton = findViewById(R.id.sendButton);
        messageInput = findViewById(R.id.messageInput);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                String todaysDate;

                String second = Integer.toString(c.get(Calendar.SECOND));
                String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
                String minute = Integer.toString(c.get(Calendar.MINUTE));
                if (second.length() == 1){
                    second = "0"+second;
                }
                if (minute.length()==1){
                    minute = "0"+minute;
                }

                if (hour.equals("0")){
                    hour = "24";
                }


                todaysDate = c.get(Calendar.YEAR) +"/"+ (c.get(Calendar.MONTH)+1) +"/"+ c.get(Calendar.DAY_OF_MONTH)+"x"
                        +hour +":"+ minute+ "x"+ second+"/"+c.get(Calendar.MILLISECOND);


                Toast.makeText(getApplicationContext(), todaysDate, Toast.LENGTH_LONG).show();

                String mes = messageInput.getText().toString();
                if (mes.isEmpty()){
                    return;
                }
                else {
                    listen_place.add(new BubbleKeeper(mes, todaysDate, myUid))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                }
                            });
                }

                recyclerView.scrollToPosition(adapter.getItemCount()-1);

                //burada chat fragmentine kisayol ekleme olacak
                //karsi tarafin chat listener'inada eklenecek
                //chat fragmentinde kisayol zaten varsa eklenmeyecek

                user_chat = db.collection("Terminal-user-credentials").document(myUid).collection("Chats lister");
                opposite_chat = db.collection("Terminal-user-credentials").document(uid).collection("Chats lister");

                //-----------
                opposite_chat.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String emailKeeper = "";

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                            People people1 = documentSnapshot.toObject(People.class);

                            String emaill = people1.getEmail();

                            emailKeeper += emaill + " ";

                        }

                        if (!emailKeeper.contains(email_my)){
                            opposite_chat.add(new People(name_my, email_my, myUid))//buraya cihaz sahibinin bilgileri gelir
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Added to opposite friends", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Failed to add to opposite friend", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Already in opposite chat list", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //-----------

                //user chat ekranina kisayol ekleme baslangic
                ChatFragment.user_chat.get()
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
                            ChatFragment.user_chat.add(new People(name, email, uid))
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Added to friends", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Failed to add friend", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Already in chat list", Toast.LENGTH_SHORT).show();
                        }

                        //user chat ekranina ekleme bitis

                    }
                });

                
                messageInput.setText("");

            }



        });


    }

    public void setUpRecyclerView(){
        Query query = listen_place.orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<BubbleKeeper> options = new FirestoreRecyclerOptions.Builder<BubbleKeeper>()
                .setQuery(query, BubbleKeeper.class)
                .build();
        adapter = new BubbleAdapter(options);
        //recyclerView = v.findViewById(R.id.discoverRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

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