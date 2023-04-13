package com.example.loginregister.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginregister.ListAdapter;
import com.example.loginregister.R;
import com.example.loginregister.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class fragmentRank extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference BASE_DE_DATOS;

    private ListAdapter listAdapter;

    private List<User> listaUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        BASE_DE_DATOS = FirebaseDatabase.getInstance().getReference("Users");

        listaUsers = new ArrayList<>();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rank, container, false);

        Query prediccionesPorClaveHija =
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("rank")
                        .limitToLast(15);

        prediccionesPorClaveHija.addValueEventListener(valueEventListener);

        listAdapter = new ListAdapter(listaUsers, container.getContext());
        RecyclerView recyclerView = root.findViewById(R.id.listUsersRanks);
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
                List<DataSnapshot> lista = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    lista.add(snapshot);
                }
                Collections.reverse(lista);
                for (DataSnapshot snapshot: lista){
                    Integer rank = Integer.parseInt(""+snapshot.child("rank").getValue());
                    String username = ""+snapshot.child("username").getValue();
                    Float points = Float.parseFloat(""+snapshot.child("points").getValue());
                    String photoString = ""+snapshot.child("photo").getValue();
                    User user = new User(username,photoString,points,rank);
                    listaUsers.add(user);
                }


            listAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}