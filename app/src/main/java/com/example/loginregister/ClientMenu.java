package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClientMenu extends AppCompatActivity {

    fragmentMap fragmentMap = new fragmentMap();
    fragmentOffers fragmentOffers = new fragmentOffers();
    fragmentRank fragmentRank = new fragmentRank();
    fragmentSettings fragmentSettings = new fragmentSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu);

        BottomNavigationView navigation = findViewById(R.id.bottom_navegation);
        loadFragment(fragmentMap);
        navigation.setOnNavigationItemSelectedListener(m0nNavigationItemSelectedLister);



    }

    private final BottomNavigationView.OnNavigationItemSelectedListener m0nNavigationItemSelectedLister = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.rankFragment:
                    loadFragment(fragmentRank);
                    return true;
                case R.id.offersFragment:
                    loadFragment(fragmentOffers);
                    return true;
                case R.id.mapFragment:
                    loadFragment(fragmentMap);
                    return true;
                case R.id.settingsFragment:
                    loadFragment(fragmentSettings);
                    return true;
            }

            return false;
        }
    };

    public void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_continer,fragment);
        transaction.commit();

    }
}