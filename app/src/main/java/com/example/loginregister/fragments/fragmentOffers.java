package com.example.loginregister.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginregister.ListAdapterOffer;
import com.example.loginregister.R;
import com.example.loginregister.models.Offer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragmentOffers extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference BASE_DE_DATOS;

    private ListAdapterOffer listAdapter;

    private List<Offer> listaUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("Offers");

        listaUsers = new ArrayList<>();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_offers, container, false);

        Query prediccionesPorClaveHija =
                FirebaseDatabase.getInstance().getReference("Offers");

        prediccionesPorClaveHija.addValueEventListener(valueEventListener);

        //listaUsers.add(new User("user1","file:///storage/emulated/0/Android/data/com.example.loginregister/files/DCIM/IMG_20230305_175036506.jpg",1,2));
        listAdapter = new ListAdapterOffer(listaUsers, container.getContext());
        RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(listAdapter);


        return root;
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            listaUsers.clear();
            if(dataSnapshot.exists()){
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String username = ""+snapshot.child("userName").getValue();
                    String photoString = ""+snapshot.child("offerDatas").getValue();
                    Offer offer = new Offer(username,photoString);
                    listaUsers.add(offer);
                }
                listAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



}