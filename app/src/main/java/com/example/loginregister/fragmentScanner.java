package com.example.loginregister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class fragmentScanner extends Fragment {

    private Button scanner;
    private ImageView img,img3;


    private EditText totalCuenta;

    private Integer barPoints;

    private TextView error;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference BASE_DE_DATOS_USER, BASE_DE_DATOS_BAR;

    private String photo,username;

    private Integer rank;

    private Float points;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BASE_DE_DATOS_USER = FirebaseDatabase.getInstance().getReference("Users");
        BASE_DE_DATOS_BAR = FirebaseDatabase.getInstance().getReference("Bar");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_scanner, container, false);
        scanner = root.findViewById(R.id.buttonScanner);
        img = root.findViewById(R.id.imageViewScanner);
        img3 = root.findViewById(R.id.imageView3);
        totalCuenta = root.findViewById(R.id.editTotalCuentaNumber);
        error = root.findViewById(R.id.errorTotal);

        BASE_DE_DATOS_BAR.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("ERROR BAR",snapshot.child("points").getValue()+"" );
                barPoints = Integer.parseInt(snapshot.child("points").getValue()+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!totalCuenta.getText().toString().isEmpty()){
                    scanCode();
                    error.setVisibility(View.INVISIBLE);
                    img3.setVisibility(View.INVISIBLE);

                }else{
                    error.setText(R.string.errorTotal);
                    error.setBackgroundResource(R.drawable.error_box);
                    error.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.VISIBLE);
                    img3.setImageResource(R.drawable.cross);
                }
            }
        });


        return root;
    }

    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {

        if(result.getContents()!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            BASE_DE_DATOS_USER.child(result.getContents()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    photo = snapshot.child("photo").getValue()+"";
                    username = snapshot.child("username").getValue()+"";
                    points = Float.parseFloat(snapshot.child("points").getValue()+"");
                    rank = Integer.parseInt(snapshot.child("rank").getValue()+"");
                    addPoints(result.getContents());
                    img.setImageURI(Uri.parse(photo));
                    img3.setVisibility(View.VISIBLE);
                    img3.setImageResource(R.drawable.check);
                    error.setBackgroundResource(R.drawable.succes_box);
                    error.setVisibility(View.VISIBLE);
                    error.setText(R.string.addedPonints);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }

    });

    private void addPoints(String userId) {

        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = new Transaction(user.getUid(),userId,now.toString());

        FirebaseDatabase.getInstance().getReference().child("Transaction")
                .child("transactionX").setValue(transaction);

        Float addPoints = barPoints*Float.parseFloat(totalCuenta.getText().toString())/5 + points;

        Map<String,Object> userMap = new HashMap<>();
        userMap.put("username",username);
        userMap.put("photo",photo);
        userMap.put("points",addPoints);
        userMap.put("rank",rank);

        FirebaseDatabase.getInstance().getReference("Users").child(userId).updateChildren(userMap);

    }

}