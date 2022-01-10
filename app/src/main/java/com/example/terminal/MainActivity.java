package com.example.terminal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.terminal.maps.MapsFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.terminal.Register.userID;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new DiscoverFragment())
                .add(R.id.fragment_container, new FriendsFragment())
                .add(R.id.fragment_container, new SettingsFragment())
                .add(R.id.fragment_container, new ChatFragment())
                .add(R.id.fragment_container, new MapsFragment())

                .hide(new DiscoverFragment())
                .hide(new FriendsFragment())
                .hide(new SettingsFragment())
                .hide(new MapsFragment())

                .commit();





    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_chat:
                        selectedFragment = new ChatFragment();
                        break;
                    case R.id.nav_discover:
                        selectedFragment = new DiscoverFragment();
                        break;
                    case R.id.nav_friends:
                        selectedFragment = new FriendsFragment();
                        break;
                    case R.id.nav_maps:
                        selectedFragment = new MapsFragment();
                        break;
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();


                /*if (new DiscoverFragment().isHidden()){
                    getSupportFragmentManager().beginTransaction().hide(new ChatFragment()).commit();
                    getSupportFragmentManager().beginTransaction().show(new DiscoverFragment()).commit();


                }

                getSupportFragmentManager().beginTransaction().show(selectedFragment).commit();

                if (new DiscoverFragment().isHidden() && selectedFragment != new ChatFragment()){
                    getSupportFragmentManager().beginTransaction().show(new DiscoverFragment()).commit();

                }


                if(FragmentB.isHidden()){
                    fragmentManager.beginTransaction()
                            .show(FragmentB).commit();

                } else {
                    fragmentManager.beginTransaction()
                            .hide(FragmentB).commit();
                }*/




                return true;
            }
        };

}