package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.loginregister.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ClientMenu extends AppCompatActivity {

    fragmentMap fragmentMap = new fragmentMap();
    fragmentOffers fragmentOffers = new fragmentOffers();
    fragmentRank fragmentRank = new fragmentRank();
    fragmentSettings fragmentSettings = new fragmentSettings();

    NavigationBarView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu);
        nav = findViewById(R.id.bottom_navegation);

        if(savedInstanceState==null){
            replaceFragment(fragmentMap);
            nav.setSelectedItemId(R.id.mapFragment);
        }
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rankFragment:
                        replaceFragment(fragmentRank);
                        return true;
                    case R.id.offersFragment:
                        replaceFragment(fragmentOffers);
                        return true;
                    case R.id.mapFragment:
                        replaceFragment(fragmentMap);
                        return true;
                    case R.id.settingsFragment:
                        replaceFragment(fragmentSettings);
                        return true;
                }

                return false;
            }
        });



    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_continer,fragment);
        fragmentTransaction.commit();


    }

}