package com.example.loginregister.services.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.loginregister.ClientMenu;
import com.example.loginregister.MainActivity;
import com.example.loginregister.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class loginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;

    private TextView error;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailLoginEditText);
        passwordEditText = findViewById(R.id.passwordLoginEditText);
        error = findViewById(R.id.error);
        progressBar = findViewById(R.id.progressBarLogin);


        mAuth = FirebaseAuth.getInstance();

    }

    public void onStart() {
        super.onStart();
    }

    public void signInWithEmailAndPassword(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Usuario:", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(loginActivity.this, ClientMenu.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Usuario:", "signInWithEmail:failure", task.getException());
                            error.setText(R.string.errorLogin);
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void buttonPress(View view){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(!email.isEmpty()&&!password.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            signInWithEmailAndPassword(email,password);
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            emailEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
            passwordEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
            error.setText(R.string.fillInuts);
            error.setVisibility(View.VISIBLE);
        }


    }

    public void login(View view){
        Intent switchLoginPage = new Intent(this, MainActivity.class);
        startActivity(switchLoginPage);
    }
}