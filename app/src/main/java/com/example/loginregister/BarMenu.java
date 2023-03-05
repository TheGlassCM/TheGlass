package com.example.loginregister;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

public class BarMenu extends AppCompatActivity {

    fragmentScanner fragmentScanner = new fragmentScanner();
    fragmentSettingsBar fragmentSettings = new fragmentSettingsBar();

    NavigationBarView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_menu);
        nav = findViewById(R.id.bottom_navegation_bar);

        if(savedInstanceState==null){
            replaceFragment(fragmentScanner);
            nav.setSelectedItemId(R.id.mapFragment);
        }
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.scannerFragment:
                        replaceFragment(fragmentScanner);
                        return true;
                    case R.id.settingsBarFragment:
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
