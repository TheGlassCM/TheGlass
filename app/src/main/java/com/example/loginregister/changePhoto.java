package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

import java.util.HashMap;
import java.util.Map;

public class changePhoto extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference BASE_DE_DATOS;

    private String photo,username;

    private Integer points,rank;

    private ImageView img;

    private Button save, change;

    private Uri photoSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_photo);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        img = findViewById(R.id.photoChangeActivity);
        save = findViewById(R.id.savePhoto);
        change = findViewById(R.id.selecPhotoChange);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("username",username);
                userMap.put("photo",photoSelected.toString());
                userMap.put("points",points);
                userMap.put("rank",rank);

                BASE_DE_DATOS.child(user.getUid()).updateChildren(userMap);
                startActivity(new Intent(changePhoto.this,ClientMenu.class));
                finish();
            }
        });

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("Users");

        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                photo = snapshot.child("photo").getValue()+"";
                photoSelected = Uri.parse(photo);
                username = snapshot.child("username").getValue()+"";
                points = Integer.parseInt(snapshot.child("points").getValue()+"");
                rank = Integer.parseInt(snapshot.child("rank").getValue()+"");

                img.setImageURI(Uri.parse(photo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selectPhoto() {
        ImagePicker.Companion.with(changePhoto.this)
                .crop()
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        photoSelected = data.getData();

        //image.setImageURI(uri);

    }
}