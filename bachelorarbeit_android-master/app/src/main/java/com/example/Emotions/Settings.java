package com.example.Emotions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Settings extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button logoutbtn = view.findViewById(R.id.button_logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                logoutUser();
            }
        });

        Button sharebtn = view.findViewById(R.id.button_share);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //TODO Add link to Download page
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, ich möchte dir diese App weiterempfehlen!");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_leaderboard:
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_leaderboardFragment);
                        break;
                    case R.id.action_map:
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_mapFragment);
                        break;
                    case R.id.action_scanner:
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_QR_Scanner);
                        break;

                }
                return true;
            }
        });


        ActionBarDrawerToggle mDrawerToggle;
        DrawerLayout mDrawer;
        MaterialToolbar mToolbar = view.findViewById(R.id.my_toolbar);

        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, mToolbar, R.string.app_name, R.string.app_name);

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

        NavigationView navigationView = view.findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_settings:
                        mDrawer.closeDrawers();
                        break;
                    case R.id.nav_profile:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_profileFragment);
                        break;
                    case R.id.nav_map:
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_mapFragment);

                        mDrawer.closeDrawers();
                        break;
                    case R.id.nav_scanner:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_QR_Scanner);
                        break;
                    case R.id.nav_leaderborad:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_leaderboardFragment);
                        break;
                    case R.id.nav_roadmap:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_QR_Scanner_to_roadmap);
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

    public void logoutUser() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP) + "/Bachelorarbeit/public/api/auth/logout";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        sharedPref.edit().remove(getString(R.string.user_data)).commit();
                        sharedPref.edit().remove(getString(R.string.access_token)).commit();
                        Navigation.findNavController(getView()).navigate(R.id.action_settings_to_LoginFlow);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }

        }) {
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
