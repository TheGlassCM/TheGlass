package com.example.loginregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.loginregister.models.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class registerAdvanced extends AppCompatActivity {

    Button selectPhoto;
    Button next;
    ImageView image;

    ProgressBar progressBar;
    EditText username;
    TextView text;

    Uri uri = null;

    String uri_firebase;
    FirebaseDatabase database;

    FirebaseUser userAuth;
    DatabaseReference myRef;

    StorageReference storageReference;

    String storagePath = "photoUser/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_advanced);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

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
                    User user = new User(username.getText().toString(),uri_firebase.toString(),0F,0);
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

        if (Activity.RESULT_OK == resultCode){
            uri = data.getData();
            image.setImageURI(uri);

            String route = storagePath + "/" + userAuth.getUid();
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

                                }
                            });
                        }
                    }
                }
            });
        }


        ;


    }



    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser userDel = FirebaseAuth.getInstance().getCurrentUser();
        userDel.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("DEL USER","Usuario eliminado");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser userDel = FirebaseAuth.getInstance().getCurrentUser();
        userDel.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("DEL USER","Usuario eliminado");
                }
            }
        });
    }

    public void login(View view){

        FirebaseUser userDel = FirebaseAuth.getInstance().getCurrentUser();
        userDel.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("DEL USER","Usuario eliminado");
                }
            }
        });
        Intent switchLoginPage = new Intent(this, MainActivity.class);
        startActivity(switchLoginPage);
    }
}