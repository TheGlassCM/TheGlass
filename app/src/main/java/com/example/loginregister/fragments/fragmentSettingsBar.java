package com.example.loginregister.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Intent;

import com.example.loginregister.MainActivity;
import com.example.loginregister.R;
import com.google.firebase.auth.FirebaseAuth;

public class fragmentSettingsBar extends Fragment {

    private Button logout;
    private FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings_bar, container, false);
        logout = root.findViewById(R.id.logoutButtonSettingsBar);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent switchLoginPage = new Intent(getActivity(), MainActivity.class);
                startActivity(switchLoginPage);
            }
        });


        return root;
    }



}