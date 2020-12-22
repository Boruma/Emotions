package com.example.Emotions;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.example.Emotions.Services.GeofenceBroadcastReceiver;
import com.example.Emotions.models.User;
import com.google.android.gms.tasks.OnFailureListener;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.Point;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private List<Point> PointList = new ArrayList<Point>();

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawer;
    TextView messageTextView;
    NavigationView navigationView;


    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    //GEOFENCING
    private static final int LOC_PERM_REQ_CODE = 1;
    //meters
    private static final int GEOFENCE_RADIUS = 250;
    //in milli seconds
    private static final int GEOFENCE_EXPIRATION = 600000;
    //GeofencingClient
    private GeofencingClient geofencingClient;
    //List of all Geofences in the Fragment
    private List<Geofence> GeofenceList = new ArrayList<Geofence>();
    // This is the Notification Channel ID. More about this in the next section
    public static final String NOTIFICATION_CHANNEL_ID = "channel_not";
    //User visible Channel Name
    public static final String CHANNEL_NAME = "Notification Channel";
    // Importance applicable to all the notifications in this Channel
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    //ID of the Notification that is send when the User enters a Geofence
    public static final int NOTIFICATION_ID = 102;


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    public MapFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Disable Backpress after the Login Flow
        OnBackPressedCallback callback1 = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback1);


        //Location client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        //Map Object in Fragment
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        geofencingClient.removeGeofences(getGeofencePendingIntent());
        //Get all Points from DB
        GetAllPoints();


        Intent intent = new Intent(getActivity(), GeofenceBroadcastReceiver.class);
        PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Notification channel should only be created for devices running Android 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[]{
                    500,
                    500,
                    500,
                    500,
                    500
            });

            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //Nav to Leaderboard
                    case R.id.action_leaderboard:
                        Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_leaderboardFragment);
                        break;
                    //Stay in current Fragment => Do nothing
                    case R.id.action_map:

                        break;
                    //Nav to Scanner
                    case R.id.action_scanner:
                        Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_QR_Scanner);
                        break;

                }
                return true;
            }
        });

        //Burgermenu Toggle
        ActionBarDrawerToggle mDrawerToggle;
        //Slideout Menu
        DrawerLayout mDrawer;
        MaterialToolbar mToolbar = view.findViewById(R.id.my_toolbar);
        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        //Add Menu to Burger Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, mToolbar, R.string.app_name, R.string.app_name);

        //Open/Close Drawer on Click
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }

            }
        });

        //Navigation in Drawer
        NavigationView navigationView = view.findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //Nav to Settings
                    case R.id.nav_settings:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_settings);
                        break;
                    //Nav to Profile
                    case R.id.nav_profile:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_profileFragment);
                        break;
                    //Just close Drawer => stay in current Fragment
                    case R.id.nav_map:
                        mDrawer.closeDrawers();
                        break;
                    //Nav to Scanner
                    case R.id.nav_scanner:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_QR_Scanner);
                        break;
                    //Nav to Leaderboard
                    case R.id.nav_leaderborad:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_leaderboardFragment);
                        break;
                    //Nav to Roadmap
                    case R.id.nav_roadmap:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_roadmap);
                        break;

                }
                return true;
            }
        });

        //Add UserData to DrawerHeader
        navigationView = view.findViewById(R.id.left_drawer);
        View headerView = navigationView.getHeaderView(0);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(getString(R.string.user_data), null);
        if (user != null) {
            //Get User from Storage
            Gson gson = new Gson();
            User u1 = gson.fromJson(user, User.class);
            TextView navUsername = (TextView) headerView.findViewById(R.id.username_header);
            navUsername.setText(u1.getName());
            TextView navMail = (TextView) headerView.findViewById(R.id.mail_header);
            navMail.setText(u1.getEmail());
        }
        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(6000); // 6 sec interval
        mLocationRequest.setFastestInterval(1500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                    }
                });
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                mLastLocation = location;



                //current location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //Add clickevent to circle Objects
                mGoogleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                    @Override
                    public void onCircleClick(Circle circle) {
                        float[] distance = new float[2];

                        Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                                circle.getCenter().latitude, circle.getCenter().longitude, distance);

                        //current location is within circle
                        if (distance[0] < circle.getRadius()) {
                            // Create a Uri from an intent string. Use the result to create an Intent.
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(circle.getCenter().latitude) + "," + String.valueOf(circle.getCenter().longitude));
                            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            // Make the Intent explicit by setting the Google Maps package
                            mapIntent.setPackage("com.google.android.apps.maps");
                            // Attempt to start an activity that can handle the Intent
                            startActivity(mapIntent);

                            //Create Dialog to show after Navigation has ended
                            new MaterialAlertDialogBuilder(getContext())
                                    .setTitle("Fertig?")
                                    .setMessage("Sind Sie am Punkt angekommen.")
                                    .setIcon(R.drawable.ic_logo)
                                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //Nav to Scanner Fragment
                                            Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_to_QR_Scanner);
                                        }
                                    })
                                    .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //Close the Dialog
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            //Current Pos is not in the Circle
                            Toast.makeText(getContext(), "Sie befinden sich nicht in diesem Kreis", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //Check if the User has given the necessary Permissions
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                                Navigation.findNavController(getView()).navigate(R.id.action_mapFragment_self);

                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(getActivity(), GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    //Add A Geofenca and a Circle to the Map
    private Geofence addGeofence(double lat, double lang) {
        LatLng latLng = new LatLng(lat, lang);
        //Add the Geofence
        Geofence gf = new Geofence.Builder()
                .setRequestId(UUID.randomUUID().toString())
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();

        //Add the Circle as a Graphic Representation
        Circle circle = mGoogleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(GEOFENCE_RADIUS)
                .strokeWidth(10)
                .fillColor(0x220000FF)
                .strokeColor(Color.rgb(112, 112, 112))
                .clickable(true));


        //Add to the list of Geofences
        this.GeofenceList.add(gf);
        return gf;
    }

    private void addLocationAlert(double lat, double lng) {
        //if permissions granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //add geofence
            Geofence geofence = addGeofence(lat, lng);
            geofencingClient.addGeofences(getGeofencingRequest(geofence),
                    getGeofencePendingIntent())
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Toast.makeText(getContext(), "Location alter has been added", Toast.LENGTH_SHORT).show();
                                Log.d("GEOFENCESLIST", GeofenceList.toString());
                            }
                        }
                    });
        } else {
            Log.e("KEINE PERMISSION", "KEINE PERMISSION");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    // Do the location-related task
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Get Proints from Server and convert them into Java Objects
    public void GetAllPoints() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP) + "/Bachelorarbeit/public/api/auth/points";
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        PointList.clear();
                        String json = response.toString();
                        //Create Java Objects from JSON via Gson
                        List<Point> tempPoints = new Gson().fromJson(json, new TypeToken<List<Point>>() {
                        }.getType());
                        for (Point point : tempPoints) {
                            PointList.add(point);
                        }
                        //Add Geofences and so on
                        for (Point point : PointList) {
                            addLocationAlert(point.getLatitude(), point.getLongitude());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            //Add Headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Requested-With", "XMLHttpRequest");
                //Get Token from Storage
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String Beaerer_Token = sharedPref.getString(getString(R.string.access_token), null);
                params.put("Authorization", "Bearer " + Beaerer_Token);
                return params;
            }
        };
        queue.add(getRequest);
    }

}
