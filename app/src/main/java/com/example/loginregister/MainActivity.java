package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button logout;
    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logoutButton);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
        mAuth = FirebaseAuth.getInstance();

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if(currentUser!=null){
            logout.setEnabled(true);
        }else{
            login.setEnabled(true);
            register.setEnabled(true);
        }
    }

    public void login(View v) {
        Intent switchLoginPage = new Intent(this, loginActivity.class);
        startActivity(switchLoginPage);
    }

    public void register(View v) {
        Intent switchRegisterPage = new Intent(this, registerActivity.class);
        startActivity(switchRegisterPage);
    }

    public void close(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(getIntent());
    }

    public void clientMenu(View view){
        Intent switchClientMenu = new Intent(this, ClientMenu.class);
        startActivity(switchClientMenu);
    }

}