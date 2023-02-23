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
                            error.setText("* Autenticación fallida");
                            error.setVisibility(View.VISIBLE);
                            Toast.makeText(registerActivity.this, "Aunthentication failed", Toast.LENGTH_SHORT);
                        }
                    }
                });

    }

    public void buttonPress(View view){

        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();
        String passRe = passConEditText.getText().toString();

        if(!email.isEmpty()&&!pass.isEmpty()&&!passRe.isEmpty()){



            if(pass.equals(passRe)){

                if(pass.length()<6){
                    Toast.makeText(this, "La contraseña debe contener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
                    error.setText("* La contraseña debe contener al menos 6 carácteres");
                    error.setVisibility(View.VISIBLE);
                }else{
                    createAccount(email,pass);
                }

            }else {
                Toast.makeText(this,"La contraseña no coincide",Toast.LENGTH_SHORT).show();
                error.setText("* Las contraseñas no coinciden");
                error.setVisibility(View.VISIBLE);
            }

        }else {
            Toast.makeText(this,"No puedes dejar campos vacíos",Toast.LENGTH_SHORT).show();
        }

    }
}