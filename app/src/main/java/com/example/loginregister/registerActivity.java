package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passEditText;
    private EditText passConEditText;

    private ProgressBar progressBar;

    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailRegisterEditText);
        passEditText = findViewById(R.id.passRegisterEditText);
        passConEditText = findViewById(R.id.passConRegisterEditText);
        progressBar = findViewById(R.id.progressBarRegister);

        error = findViewById(R.id.errorRegister);

        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void createAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            signInWithEmailAndPassword(email,password);
                        }else{
                            error.setText(R.string.failedAutentication);
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    public void signInWithEmailAndPassword(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Usuario:", "signInWithEmail:success");
                            startActivity(new Intent(registerActivity.this, registerAdvanced.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Usuario:", "signInWithEmail:failure", task.getException());
                            error.setText(R.string.errorLogin);
                            error.setVisibility(View.VISIBLE);

                            //updateUI(null);
                        }
                    }
                });
    }

    public void buttonPress(View view){

        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();
        String passRe = passConEditText.getText().toString();

        if(!email.isEmpty()&&!pass.isEmpty()&&!passRe.isEmpty()){

            if(!email.contains("@")){
                emailEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
                error.setText(R.string.errorEmail);
                error.setVisibility(View.VISIBLE);
            }else {


                if (pass.equals(passRe)) {

                    if (pass.length() < 6) {
                        Toast.makeText(this, R.string.errorPassLenght, Toast.LENGTH_SHORT).show();
                        error.setText(R.string.errorPassLenght);
                        error.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        createAccount(email, pass);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Toast.makeText(this, R.string.errorRegisterPass, Toast.LENGTH_SHORT).show();
                    error.setText(R.string.errorRegisterPass);
                    error.setVisibility(View.VISIBLE);
                }
            }
        }else {
            emailEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
            passEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
            passConEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
            error.setText(R.string.fillInuts);
            error.setVisibility(View.VISIBLE);
        }

    }
}