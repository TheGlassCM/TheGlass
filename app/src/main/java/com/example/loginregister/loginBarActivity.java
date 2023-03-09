package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class loginBarActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView error;

    private Boolean res = false;

    private DatabaseReference BASE_DE_DATOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bar);

        emailEditText = findViewById(R.id.emailLoginBarEditText);
        passwordEditText = findViewById(R.id.passwordLoginBarEditText);
        error = findViewById(R.id.errorBar);

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("Bar");

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

                            res = user.getEmail().contains("@theglass.com")?true:false;
                            Log.d("Es bar?",res.toString());
                            if(res){
                                startActivity(new Intent(loginBarActivity.this, BarMenu.class));
                                finish();
                            }else {
                                emailEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
                                passwordEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
                                error.setText(R.string.loginBarClient);
                                error.setVisibility(View.VISIBLE);
                                FirebaseAuth.getInstance().signOut();
                                res = false;
                            }

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
            signInWithEmailAndPassword(email,password);
        }else{
            emailEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
            passwordEditText.setBackgroundResource(R.drawable.rounded_error_edittext);
            error.setText(R.string.fillInuts);
            error.setVisibility(View.VISIBLE);
        }


    }
}