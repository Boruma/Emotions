package com.example.Emotions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;


public class profileFragment extends Fragment {



    public profileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(getString(R.string.user_data), null);

        User u1  = new Gson().fromJson(user, new TypeToken<User>() {}.getType());
        Log.d("USer in storage", u1.toString());

        TextView usernameTextView = view.findViewById(R.id.user_data_name);
        TextView userMailTextView = view.findViewById(R.id.user_data_mail);
        TextView userCountryTextView = view.findViewById(R.id.user_data_country);
        TextView userAgeTextView = view.findViewById(R.id.user_data_age);
        TextView userEducationTextView = view.findViewById(R.id.user_data_education );

        usernameTextView.setText(u1.getName());
        userMailTextView.setText(u1.getEmail());
        userCountryTextView.setText(u1.getCountry());
        userAgeTextView.setText(String.valueOf(u1.getAge()));
        userEducationTextView.setText(u1.getEducation());

        EditText editTextName = view.findViewById(R.id.editName);
        EditText editTextCountry = view.findViewById(R.id.filled_exposed_dropdown_countries);
        EditText editTextAge = view.findViewById(R.id.editAge);
        EditText editTextEducation = view.findViewById(R.id.filled_exposed_dropdown_education);

        com.google.android.material.textfield.TextInputLayout dropdownEdu = view.findViewById(R.id.editEducation);
        com.google.android.material.textfield.TextInputLayout dropdownCountry = view.findViewById(R.id.editCountry);

        Button editFinish = view.findViewById(R.id.button_editFinish);
        Button editCancel= view.findViewById(R.id.button_editcancel);
        ActionBarDrawerToggle mDrawerToggle;
        DrawerLayout mDrawer;

        MaterialToolbar mToolbar = view.findViewById(R.id.my_toolbar);

        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, mToolbar, R.string.app_name, R.string.app_name);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDrawer.isDrawerOpen(GravityCompat.START)){
                    mDrawer.closeDrawer(GravityCompat.START);
                }else {
                    mDrawer.openDrawer(GravityCompat.START);
                }

            }
        });
        //Navigation via LeftDrawer
        NavigationView navigationView = view.findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_settings:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_settings);
                        break;
                    case R.id.nav_profile:
                        mDrawer.closeDrawers();
                        break;
                    case R.id.nav_map:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_mapFragment);
                        break;
                    case R.id.nav_scanner:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_QR_Scanner);
                        break;
                    case R.id.nav_leaderborad:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_leaderboardFragment);
                        break;
                    case R.id.nav_roadmap:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_roadmap);
                        break;

                }
                return true;
            }
        });

        //Add UserData to DrawerHeader
        navigationView = view.findViewById(R.id.left_drawer);
        View headerView = navigationView.getHeaderView(0);
        if (user != null) {
            //Get User from Storage
            Gson gson = new Gson();
            TextView navUsername = (TextView) headerView.findViewById(R.id.username_header);
            navUsername.setText(u1.getName());
            TextView navMail = (TextView) headerView.findViewById(R.id.mail_header);
            navMail.setText(u1.getEmail());
        }

        //Navigation via Bottom TabBar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_leaderboard:
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_leaderboardFragment);
                        break;
                    case R.id.action_map:
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_mapFragment);
                        break;
                    case R.id.action_scanner:
                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_QR_Scanner);
                        break;

                }
                return true;
            }
        });

        //Enable input when menu is triggerd via 3 dops in top bar
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_edit:
                        editTextName.setText(u1.getName());
                        editTextName.setVisibility(View.VISIBLE);
                        usernameTextView.setVisibility(View.INVISIBLE);

                        editTextCountry.setText(u1.getCountry());
                        editTextCountry.setVisibility(View.VISIBLE);
                        userCountryTextView.setVisibility(View.INVISIBLE);

                        editTextAge.setText(String.valueOf(u1.getAge()));
                        editTextAge.setVisibility(View.VISIBLE);
                        userAgeTextView.setVisibility(View.INVISIBLE);

                        editTextEducation.setText(u1.getEducation());
                        editTextEducation.setVisibility(View.VISIBLE);
                        userEducationTextView.setVisibility(View.INVISIBLE);

                        dropdownEdu.setVisibility(View.VISIBLE);
                        dropdownCountry.setVisibility(View.VISIBLE);

                        editFinish.setVisibility(View.VISIBLE);
                        editCancel.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });



        //finish ediditng an set inputs hidden
        editFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String Name = editTextName.getText().toString();
                String Country = editTextCountry.getText().toString();
                String Education = editTextEducation.getText().toString();
                String Age = editTextAge.getText().toString();

                if(Name.length()==0){
                    editTextName.requestFocus();
                    editTextName.setError(getString(R.string.errorEmpty));
                }
                if(Country.length()==0){
                    editTextCountry.requestFocus();
                    editTextCountry.setError(getString(R.string.errorEmpty));
                }
                if(Education.length()==0){
                    editTextEducation.requestFocus();
                    editTextEducation.setError(getString(R.string.errorEmpty));
                }
                if(Age.length()==0){
                    editTextAge.requestFocus();
                    editTextAge.setError(getString(R.string.errorEmpty));
                }

                if(editTextName.getError() == null  && editTextCountry.getError() == null && editTextEducation.getError() == null  && editTextAge.getError() == null  ){
                    updateUserData(String.valueOf(editTextName.getText()),
                            String.valueOf(editTextCountry.getText()),
                            String.valueOf(editTextEducation.getText()),
                            String.valueOf(editTextAge.getText()),
                            u1.getEmail());

                    editTextName.setVisibility(View.INVISIBLE);
                    usernameTextView.setVisibility(View.VISIBLE);

                    editTextCountry.setVisibility(View.INVISIBLE);
                    userCountryTextView.setVisibility(View.VISIBLE);

                    editTextAge.setVisibility(View.INVISIBLE);
                    userAgeTextView.setVisibility(View.VISIBLE);

                    editTextEducation.setVisibility(View.INVISIBLE);
                    userEducationTextView.setVisibility(View.VISIBLE);

                    editFinish.setVisibility(View.INVISIBLE);
                    editCancel.setVisibility(View.INVISIBLE);

                    dropdownEdu.setVisibility(View.INVISIBLE);
                    dropdownCountry.setVisibility(View.INVISIBLE);

                }
            }
        });

        editCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editTextName.setVisibility(View.INVISIBLE);
                usernameTextView.setVisibility(View.VISIBLE);

                editTextCountry.setVisibility(View.INVISIBLE);
                userCountryTextView.setVisibility(View.VISIBLE);

                editTextAge.setVisibility(View.INVISIBLE);
                userAgeTextView.setVisibility(View.VISIBLE);

                editTextEducation.setVisibility(View.INVISIBLE);
                userEducationTextView.setVisibility(View.VISIBLE);

                dropdownEdu.setVisibility(View.INVISIBLE);
                dropdownCountry.setVisibility(View.INVISIBLE);
            }
        });

        String[] education = new String[] {"Ohne", "Hauptschulabschluss", "Realschulabschluss", "Fachhochschulreife" , "Meister-/Technikerausbildung " , "Fachhochschulabschluß", "Promotion" ,"keine Angabe"};


        ArrayAdapter<String> adapterEducation =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        education);

        final AutoCompleteTextView editTextFilledExposedDropdownEducation =
                view.findViewById(R.id.filled_exposed_dropdown_education);
        editTextFilledExposedDropdownEducation.setAdapter(adapterEducation);


        String[] coutries = new String[] {
                "Belgien",
                "Bulgarien",
                "Dänemark",
                "Deutschland",
                "Estland",
                "Finnland",
                "Frankreich",
                "Griechenland",
                "Irland",
                "Italien",
                "Kroatien",
                "Lettland",
                "Litauen",
                "Luxemburg",
                "Malta",
                "Niederlande",
                "Österreich",
                "Polen",
                "Portugal",
                "Rumänien",
                "Schweden",
                "Slowakische Republik",
                "Slowenien",
                "Spanien",
                "Tschechien",
                "Ungarn",
                "Vereinigtes Königreich",
                "Zypern"};

        ArrayAdapter<String> adapterCountries =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        coutries);

        final AutoCompleteTextView editTextFilledExposedDropdownCountries =
                view.findViewById(R.id.filled_exposed_dropdown_countries);
        editTextFilledExposedDropdownCountries.setAdapter(adapterCountries);


        return view;
    }

    public void updateUserData(final String name,final String country, final String education,final String age,final String mail)  {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlPost = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/user/update/" + mail;

        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String json = response.toString();
                        User u1  = new Gson().fromJson(json, new TypeToken<User>() {}.getType());
                        Log.d("response_ user data", u1.toString());


                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.user_data),u1.toString());
                        editor.commit();

                        Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_self);

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", name);
                params.put("education", education);
                params.put("age", age);
                params.put("country", country);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Requested-With", "XMLHttpRequest");
                //Get Token from Storage
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String Beaerer_Token = sharedPref.getString(getString(R.string.access_token), null);
                params.put("Authorization", "Bearer " + Beaerer_Token );
                return params;
            }

        };
        queue.add(postRequest);
    }
}