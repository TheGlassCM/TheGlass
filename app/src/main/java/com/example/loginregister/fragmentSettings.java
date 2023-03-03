package com.example.loginregister;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

public class fragmentSettings extends Fragment {

    private Button logout;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference BASE_DE_DATOS;

    private FirebaseDatabase database;

    private TextView textPoints,textRank,textUsername;
    private ImageView qr,photo;

    private Uri uri;
    private Bitmap bitmap;
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
        textPoints = root.findViewById(R.id.points);
        textRank = root.findViewById(R.id.rank);
        textUsername = root.findViewById(R.id.username);
        qr = root.findViewById(R.id.imageQR);
        photo = root.findViewById(R.id.imagePhoto);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent switchLoginPage = new Intent(getActivity(), MainActivity.class);
                startActivity(switchLoginPage);
            }
        });

        BASE_DE_DATOS.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String points = ""+snapshot.child("points").getValue();
                String rank = ""+snapshot.child("rank").getValue();
                String username = ""+snapshot.child("username").getValue();
                String qrString = ""+snapshot.child("qr").getValue();
                String photoString = ""+snapshot.child("photo").getValue();

                textPoints.setText(points);
                textRank.setText(rank);
                textUsername.setText(username);

                uri = Uri.parse(photoString);
                photo.setImageURI(uri);

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

    private Bitmap getStringBitmap(String qrString) {

        try {

            byte [] encodeByte = Base64.getDecoder().decode(qrString);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;

        }catch (Exception e){
            return  null;
        }

    };


}