package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
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

import java.io.ByteArrayOutputStream;


public class registerAdvanced extends AppCompatActivity {

    Button selectPhoto;
    Button next;
    ImageView image;

    ProgressBar progressBar;
    EditText username;
    TextView text;

    Uri uri = null;
    FirebaseDatabase database;

    FirebaseUser userAuth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_advanced);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        selectPhoto = findViewById(R.id.selecPhoto);
        image = findViewById(R.id.photo);
        next = findViewById(R.id.next);
        text = findViewById(R.id.errorRegisterAdvanced);
        username = findViewById(R.id.userName);
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = findViewById(R.id.progressBar);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri==null || username.getText().toString().isEmpty()){
                    if (uri==null)  text.setText(R.string.errorRegisterAdvanced);
                    else{
                        text.setText(R.string.errorUsername);
                        username.setBackgroundResource(R.drawable.rounded_error_edittext);
                    }

                    text.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    User user = new User(username.getText().toString(),uri.toString(),0,0);
                    myRef = database.getReference();
                    myRef.child("Users").child(userAuth.getUid()).setValue(user);
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(registerAdvanced.this, ClientMenu.class));

                }
            }
        });
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.Companion.with(registerAdvanced.this)
                        .crop()
                        .cropSquare()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();
        image.setImageURI(uri);

    }


    public void login(View view){
        Intent switchLoginPage = new Intent(this, MainActivity.class);
        startActivity(switchLoginPage);
    }
}