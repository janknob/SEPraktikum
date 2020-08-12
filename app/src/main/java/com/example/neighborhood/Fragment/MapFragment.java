package com.example.neighborhood.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.neighborhood.LocationHelper;
import com.example.neighborhood.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = MapFragment.class.getSimpleName();
    private CameraPosition cameraPosition;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    private DatabaseReference userRef;
    private FirebaseDatabase database;
    private static final String USER = "Users";
    private static final String FRIENDED = "Unbefreundet";
    private List<String> friendList;
    private DatabaseReference friendsReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //assign map variable
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        //initialize fused location
        client = LocationServices.getFusedLocationProviderClient(getActivity());


//check permission
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return view;
        } else {
            //when permission denied
            //request them
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        //clear friendList to update it
        friendList.clear();
        friendList = new ArrayList<>();



        return view;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(location!=null){
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("me").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).alpha(0.5f));
                    CameraPosition de = CameraPosition.builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(6).bearing(0).build();

                    //save current location in DB
                    saveCurrentLocation(location);

                    //show friends nearby
                    friendList = new ArrayList<>();
                    checkIfFriend();
                    showFriendsLocation();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 44: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.
                    return;
                } else {
                    // permission denied.
                    // tell the user the action is cancelled
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("Standort muss aktiviert sein");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return;
            }
        }
    }

        //saves current location of current user in DB to show up on friends map
        void saveCurrentLocation(Location location){


            LocationHelper helper = new LocationHelper(location.getLatitude(), location.getLongitude());

            //DB references
            database = FirebaseDatabase.getInstance();
            userRef = database.getReference(USER);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String USERID = user.getUid();

            userRef.child(USERID).child("location").setValue(helper);
        }

    private void checkIfFriend()
    {

        //DB references
        friendsReference = FirebaseDatabase.getInstance().getReference(FRIENDED).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Befreundet");



        friendsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                        friendList.add(snapshot.getKey());
                }

                //show friends
                System.out.println(friendList);

                for (Object o: friendList){

                    System.out.println("Show innenn: " + o.toString());


                    //get friend location
                    //DB Reference of Friend
                    final DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference(USER).child(o.toString());


                    friendRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if((snapshot.child("location").child("latitude").getValue() != null) && (snapshot.child("location").child("longitude") != null)) {


                                    LatLng friendLocation = new LatLng(Double.parseDouble(snapshot.child("location").child("latitude").getValue().toString()),
                                            Double.parseDouble(snapshot.child("location").child("longitude").getValue().toString()));

                                    System.out.println("friendlocation: " + friendLocation);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(friendLocation)).setTitle(snapshot.child("username").getValue().toString());
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }



                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("show friends geht: " + friendList);



    }

    private void showFriendsLocation(){



    }

}