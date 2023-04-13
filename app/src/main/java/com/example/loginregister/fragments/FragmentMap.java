package com.example.loginregister.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.loginregister.R;
import com.example.loginregister.models.Bar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FragmentMap extends Fragment implements OnMapReadyCallback{

    private Spinner spType;
    private Button btFind;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double currentLat = 0, currentLong = 0;
    private String locality;

    private FirebaseAuth firebaseAuth;

    private FirebaseUser user;

    private DatabaseReference databaseReference;

    private List<Bar> bars;

    private int timeStamp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Ask for the gps location
        getGPSPermission();
        spType = rootView.findViewById(R.id.sp_type);
        btFind = rootView.findViewById(R.id.bt_find);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        // Database connection
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Bar");
        bars = new ArrayList<>();

        getBars();
        // Bar types
        String[] placeTypeArray = {"bar", "night_club"};
        // Bar names
        String[] placeNameArray = {"Bar", "Pub/Discoteca"};

        // Setting the names that will appear in the adapter
        spType.setAdapter(new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_dropdown_item, placeNameArray));

        //Generating map fragment
        supportMapFragment.getMapAsync(this);


        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = spType.getSelectedItemPosition();

                new ParserTask().execute(placeTypeArray[i]);
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Enables the location from the user
        if (ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        map.setMyLocationEnabled(true);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this.requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    // Once we get the location
                    // Get current latitude
                    currentLat = location.getLatitude();
                    // Get current longitude
                    currentLong = location.getLongitude();
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(currentLat, currentLong, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        locality = address.getLocality();
                    }

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currentLong), 10));
                }
            }
        });
    }

    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String,String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            List<HashMap<String, String>> res = new ArrayList<>();

            if(bars!=null && !bars.isEmpty()){
                for(Bar bar:bars){
                    // Confirming that search type is correct and the locality as well
                    if(strings[0].equals(bar.getType()) && locality.equalsIgnoreCase(bar.getLocation())){
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("name",bar.getName());
                        hashMap.put("type",bar.getType());
                        hashMap.put("location",bar.getLocation());
                        hashMap.put("lat",bar.getLatitude().toString());
                        hashMap.put("lng",bar.getLongitude().toString());
                        hashMap.put("dto",bar.getDto().toString());
                        hashMap.put("dto_points",bar.getDto_points().toString());
                        hashMap.put("points",bar.getPoints().toString());
                        hashMap.put("schedule",bar.getSchedule());
                        res.add(hashMap);
                    }
                }
            }
            return res;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();
            // TimeStamp
            timeStamp = LocalDateTime.now().getHour();
            if(!hashMaps.isEmpty()){
                for(HashMap<String,String> hashMap : hashMaps){

                    String latString = hashMap.get("lat");
                    String lngString = hashMap.get("lng");
                    if(latString==null || lngString==null){
                        break;
                    }
                    // Getting latitude
                    double lat = Double.parseDouble(Objects.requireNonNull(hashMap.get("lat")));
                    // Getting longitude
                    double lng = Double.parseDouble(Objects.requireNonNull(hashMap.get("lng")));
                    // Getting name
                    String name = hashMap.get("name");
                    // Getting schedule
                    Integer[] schedule = getHours(hashMap.get("schedule"));
                    // Getting points
                    String points = hashMap.get("points");
                    // Concat latitude and longitude
                    LatLng latLng = new LatLng(lat,lng);
                    // Initialize marker options
                    MarkerOptions options = new MarkerOptions();

                    options.snippet(String.format("%s • %s",isOpen(schedule),points));
                    // Set position
                    options.position(latLng);
                    // Set title
                    options.title(name);
                    // Add marker on map
                    map.addMarker(options);
                }
            }

        }
    }
    private Integer[] getHours(String schedule){
        Integer hour1 = Integer.valueOf(schedule.split("-")[0].trim());
        Integer hour2 = Integer.valueOf(schedule.split("-")[1].trim());

        Integer[] hours = {hour1,hour2};
        return hours;
    }
    private String isOpen(Integer[] schedule){
        return schedule[0]<=timeStamp || schedule[1]>=timeStamp ? "Abierto" : "Cerrado";
    }

    private void getGPSPermission(){
        LocationManager locationManager = (LocationManager) this.requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.requireActivity());
            builder.setTitle("Ubicación desactivada");
            builder.setMessage("Por favor activa la ubicación para utilizar esta función");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // It opens the configuration view to enable the gps location
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void getBars(){
        Query query =  FirebaseDatabase.getInstance().getReference("Bar");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bars.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        snapshot.hasChild("latitude");
                        String name = (String) snapshot.child("name").getValue();
                        String type = (String) snapshot.child("type").getValue();
                        String location = (String) snapshot.child("location").getValue();
                        Integer dto = Integer.valueOf(Objects.requireNonNull(snapshot.child("dto").getValue()).toString());
                        Integer dto_points = Integer.valueOf(Objects.requireNonNull(snapshot.child("dto_points").getValue()).toString());
                        Integer points = Integer.valueOf(Objects.requireNonNull(snapshot.child("points").getValue()).toString());
                        Float latitude =  Float.valueOf(Objects.requireNonNull(snapshot.child("latitude").getValue()).toString());
                        Float longitude = Float.valueOf(Objects.requireNonNull(snapshot.child("longitude").getValue()).toString());
                        String schedule = (String) snapshot.child("schedule").getValue();
                        Bar bar = new Bar(name,type,location,schedule,dto,dto_points,points,latitude,longitude);
                        bars.add(bar);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"No es posible mostrar los bares en estos momentos",Toast.LENGTH_SHORT).show();
            }
        });
    }


}