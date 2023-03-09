package com.example.loginregister;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class fragmentSettings extends Fragment {

    private Button logout,changeUsername, changePhoto;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference BASE_DE_DATOS;
    private ImageView qr;
    private EditText textChangeUsername;

    private TextView succes;
    private String photo,username;

    private Integer rank;

    private Float points;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("Users");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        logout = root.findViewById(R.id.logoutButtonSettings);
        changePhoto = root.findViewById(R.id.changePhoto);
        changeUsername = root.findViewById(R.id.changeUsername);
        textChangeUsername = root.findViewById(R.id.textChangeUsername);
        qr = root.findViewById(R.id.imageQR);
        succes = root.findViewById(R.id.success);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent switchLoginPage = new Intent(getActivity(), MainActivity.class);
                startActivity(switchLoginPage);
            }
        });

        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = textChangeUsername.getText().toString();
                if(!username.isEmpty()){
                    Map<String,Object> userMap = new HashMap<>();
                    userMap.put("username",username);
                    userMap.put("photo",photo);
                    userMap.put("points",points);
                    userMap.put("rank",rank);

                    BASE_DE_DATOS.child(user.getUid()).updateChildren(userMap);
                    succes.setBackgroundResource(R.drawable.succes_box);
                    succes.setText(R.string.usernameChangesucces);
                    succes.setVisibility(View.VISIBLE);
                }else {
                    succes.setBackgroundResource(R.drawable.error_box);
                    succes.setText(R.string.usernameEmpty);
                    succes.setVisibility(View.VISIBLE);
                }

            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),changePhoto.class));
                succes.setBackgroundResource(R.drawable.succes_box);

            }
        });

        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                photo = snapshot.child("photo").getValue()+"";
                username = snapshot.child("username").getValue()+"";
                points = Float.parseFloat(snapshot.child("points").getValue()+"");
                rank = Integer.parseInt(snapshot.child("rank").getValue()+"");

                textChangeUsername.setText(username);

                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(user.getUid().toString(), BarcodeFormat.QR_CODE,600,600);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);

                    qr.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }




}