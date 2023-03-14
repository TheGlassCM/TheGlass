package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class changePhoto extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference BASE_DE_DATOS;

    StorageReference storageReference;
    private String photo,username;

    private Integer rank;

    private Float points;

    private ImageView img;

    private Button save, change;

    private Uri photoSelected;

    private String uri_firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_photo);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

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
                savePhoto(photoSelected);

            }
        });

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("Users");

        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                photo = snapshot.child("photo").getValue()+"";
                photoSelected = Uri.parse(photo);
                username = snapshot.child("username").getValue()+"";
                points = Float.parseFloat(snapshot.child("points").getValue()+"");
                rank = Integer.parseInt(snapshot.child("rank").getValue()+"");

                Picasso.with(img.getContext())
                        .load(photo)
                        .into(img);
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

    public void savePhoto(Uri uri){
        String route = "photoUser/*" + "/" + user.getUid();
        StorageReference reference = storageReference.child(route);
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()){
                    if(uriTask.isSuccessful()){
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uri_firebase = uri.toString();
                                Map<String,Object> userMap = new HashMap<>();
                                userMap.put("username",username);
                                userMap.put("photo",uri_firebase.toString());
                                userMap.put("points",points);
                                userMap.put("rank",rank);

                                BASE_DE_DATOS.child(user.getUid()).updateChildren(userMap);
                                startActivity(new Intent(changePhoto.this,ClientMenu.class));
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (Activity.RESULT_OK == resultCode) {
            photoSelected = data.getData();
            img.setImageURI(photoSelected);

        }

    }
}