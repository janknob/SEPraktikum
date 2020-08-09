package com.example.neighborhood.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            //When permission granted
            //call Method
            getCurrentLocation();
        } else {
            //when permission denied
            //request them
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }



        return view;
    }

    private void getCurrentLocation() {
        //initialize task location

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //when success
                if (location != null) {
                    //sync map
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //initialize lat lng
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            //create marker options
                            MarkerOptions options = new MarkerOptions().position(latLng).title("Me");
                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            //add marker on map
                            googleMap.addMarker(options);
                        }
                    });
                }

            }
        });

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
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("me"));
                    CameraPosition de = CameraPosition.builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(6).bearing(0).build();

                    //save current location in DB
                    saveCurrentLocation(location);

                    //show friends nearby
                    //for(int j = 0, i <= friends, j++){
                    //mMap.addMarker(new MarkerOptions().position(new LatLng(friendLatitude, FriendLongitude)).title("friendName"));
                    //}
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
                    getCurrentLocation();
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

        void saveCurrentLocation(Location location){
            //String msg = "Aktuelle Position: " + "," + Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude());
            //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

            LocationHelper helper = new LocationHelper(location.getLatitude(), location.getLongitude());

            //DB references
            database = FirebaseDatabase.getInstance();
            userRef = database.getReference(USER);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String USERID = user.getUid();

            userRef.child(USERID).child("location").setValue(helper);
        }




}