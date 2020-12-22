package com.example.Emotions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;


public class LandingScreen extends Fragment {

    public LandingScreen() {
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




        View view = inflater.inflate(R.layout.fragment_landing_screen, container, false);
        String[] sexes = new String[] {"Maennlich", "Weiblich", "Divers", "Andere"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        sexes);

        final AutoCompleteTextView editTextFilledExposedDropdown =
                view.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);


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

        String[] education = new String[] {"Ohne", "Hauptschulabschluss", "Realschulabschluss", "Fachhochschulreife" , "Meister-/Technikerausbildung " , "Fachhochschulabschluß", "Promotion" ,"keine Angabe"};


        ArrayAdapter<String> adapterEducation =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        education);

        final AutoCompleteTextView editTextFilledExposedDropdownEducation =
                view.findViewById(R.id.filled_exposed_dropdown_education);
        editTextFilledExposedDropdownEducation.setAdapter(adapterEducation);

        final TextInputEditText ageInput = view.findViewById(R.id.textFieldAgeInput);

        Button BtnCancle = view.findViewById(R.id.button_landingcancel);
        BtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Abbrechen")
                        .setMessage("Sind Sie sicher, dass sie fortfahren möchten? Sie können diese Informationen auch später in der Profilansicht hinzufügen.")
                        .setIcon(R.drawable.ic_logo)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Navigation.findNavController(getView()).navigate(R.id.action_landingScreen_to_mapFragment);
                            }
                        })
                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        Button Btncontinue = view.findViewById(R.id.button_landingcontinue);
        Btncontinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String sex = editTextFilledExposedDropdown.getText().toString();
                final String age = ageInput.getText().toString();
                final String education = editTextFilledExposedDropdownEducation.getText().toString();
                final String country = editTextFilledExposedDropdownCountries.getText().toString();

                if(age.length() == 0){
                    ageInput.requestFocus();
                    ageInput.setError(getString(R.string.errorEmpty));
                }
                if(sex.length() == 0){
                    editTextFilledExposedDropdown.requestFocus();
                    editTextFilledExposedDropdown.setError(getString(R.string.errorEmpty));
                }

                if(education.length() == 0){
                    editTextFilledExposedDropdownEducation.requestFocus();
                    editTextFilledExposedDropdownEducation.setError(getString(R.string.errorEmpty));
                }

                if(country.length() == 0){
                    editTextFilledExposedDropdownCountries.requestFocus();
                    editTextFilledExposedDropdownCountries.setError(getString(R.string.errorEmpty));
                }

                if(sex.length() != 0 && education.length() != 0 && age.length() != 0){
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Weiter")
                            .setMessage("Sind Sie sicher, dass sie fortfahren möchten?")
                            .setIcon(R.drawable.ic_logo)
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addAdditionalUserData(sex,education,age,country);

                                }
                            })
                            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
        return view;
    }


    public void addAdditionalUserData(final String sex, final String education,final String age, final String country)  {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(getString(R.string.user_data), null);
        User uo  = new Gson().fromJson(user, new TypeToken<User>() {}.getType());
        Log.d("user data vorher", uo.toString());
        final String urlPost = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/user/"+ uo.geteMail();

        sharedPref.edit().remove(getString(R.string.user_data)).commit();

        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        String json = response.toString();
                        User u1  = new Gson().fromJson(json, new TypeToken<User>() {}.getType());
                        //Save User to Storage
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        sharedPref.edit().remove(getString(R.string.user_data)).commit();
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.user_data),u1.toString());
                        editor.commit();
                        Navigation.findNavController(getView()).navigate(R.id.action_landingScreen_to_mapFragment);
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
                params.put("education", education);
                params.put("age", age);
                params.put("sex", sex);
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
