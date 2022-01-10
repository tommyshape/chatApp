package com.example.terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    Button logOutBtn;
    Button goToMaps;
    static TextView nameViewSettings, emailViewSettings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static DocumentReference user_name_reference;
    static String name;
    static String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        logOutBtn = v.findViewById(R.id.logOutBtn);
        goToMaps = v.findViewById(R.id.button_maps);
        nameViewSettings = v.findViewById(R.id.nameViewSettings);
        emailViewSettings = v.findViewById(R.id.emailViewSettings);

        String userID = Register.fAuth.getCurrentUser().getUid();

        user_name_reference = db.collection("Terminal-user-credentials").document(userID);

        user_name_reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            name = (String) documentSnapshot.get("name");
                            email = documentSnapshot.get("email").toString();
                            emailViewSettings.setText(email);
                            nameViewSettings.setText(name);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to get information", Toast.LENGTH_SHORT).show();
                    }
                });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }
        });

        goToMaps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               //Go to maps activity

                /*Intent k = new Intent(getActivity(), MapsActivity.class);
                startActivity(k);*/


            }
        });

        return v;
    }
}
