package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailRegisterEditText);
        passEditText = findViewById(R.id.passRegisterEditText);
        passConEditText = findViewById(R.id.passConRegisterEditText);

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
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            startActivity(new Intent(registerActivity.this, MainActivity.class));
                        }else{
                            error.setText(R.string.failedAutentication);
                            error.setVisibility(View.VISIBLE);
                            Toast.makeText(registerActivity.this, R.string.failedAutentication, Toast.LENGTH_SHORT);
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
                        createAccount(email, pass);
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