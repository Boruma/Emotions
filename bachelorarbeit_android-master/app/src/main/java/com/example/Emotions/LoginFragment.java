package com.example.Emotions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    boolean returnvalue = false;
    private List<User> UserList = new ArrayList<User>();
    boolean returnval = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get Storage
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(getString(R.string.user_data), null);
        User u1 = new Gson().fromJson(user, new TypeToken<User>() {
        }.getType());

        //Check if Userdata is stored in Storage
        if (u1 != null) {
            Log.d("user u1", u1.toString());
            //Userdata is in Storage => Navigate to Map
            if ((u1.getName().equals("null") == false && u1.getCountry().equals("null") == false)) {
                Log.d("User_Storage 1", u1.toString());
                Toast.makeText(getContext(), "Wilkommen zurück " + u1.getName(), Toast.LENGTH_LONG).show();
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mapFragment);
            }

            //Userdata is not in Storage => Navigate to Landing
            if ((u1.getCountry().equals("null") == true)) {
                Log.d("User_Storage 2", u1.toString());
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_landingScreen);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Get Elements from View
        final EditText editMail = view.findViewById(R.id.et_email);
        final EditText editPassword = view.findViewById(R.id.et_password);
        final Button buttonLogin = view.findViewById(R.id.btn_login);
        final Button buttonResetPW = view.findViewById(R.id.btn_resetPWLink);
        final ImageView buttonRegiser = view.findViewById(R.id.switchToReg);

        //Disable Backpress after the Logout
        OnBackPressedCallback callback1 = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback1);

        //Navigation to RegisterFragment
        buttonRegiser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        //Navigation to ResetPWFragment
        buttonResetPW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_resetPwEmail);
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String Mail = editMail.getText().toString();
                String PW = editPassword.getText().toString();
                //Validation for Inputs
                if (Mail.length() == 0) {
                    editMail.requestFocus();
                    editMail.setError(getString(R.string.errorEmpty));
                } else if (!Mail.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
                    editMail.requestFocus();
                    editMail.setError("Bitte Valide E-Mail eingben");
                }
                if (PW.length() == 0) {
                    editPassword.requestFocus();
                    editPassword.setError(getString(R.string.errorEmpty));
                }
                if (editMail.getError() == null && editPassword.getError() == null) {
                    LoginUser(Mail, PW);
                }
            }
        });
        return view;
    }


    //Login User and get Token from Server
    public void LoginUser(final String Mail, final String PW) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlPost = getString(R.string.ServerIP) + "/Bachelorarbeit/public/api/auth/login";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            Log.d("JSONException", String.valueOf(e));
                        }
                        String access_token;
                        // Check if Respone has an  Bearer Token
                        if (json.has("access_token")) {
                            try {
                                access_token = json.getString("access_token");
                                //Save Token to Storage
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.access_token), access_token);
                                editor.commit();
                                //Get Token from Storage
                                String Beaerer_Token = sharedPref.getString(getString(R.string.access_token), null);
                                Log.d("Token_Storage", Beaerer_Token);
                                getUserData(Mail);
                            } catch (JSONException e) {
                                Log.d("JSONException", String.valueOf(e));
                            }
                        } else {
                            Log.d("Token", "kein Token");
                        }
                    }
                },
                //Errorhandling for Request
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        //No User for submited Data
                        if (error.networkResponse.statusCode == 401) {
                            Toast.makeText(getContext(), "Kein Nutzer mit diesen Daten gefunden", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Fehler bei Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            //Add Parameters to Reqeust
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", Mail);
                params.put("password", PW);
                return params;
            }

            //Add Headers to Reqeust
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
        queue.add(postRequest);
    }


    //Get Data for User from Server
    public void getUserData(String mail) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP) + "/Bachelorarbeit/public/api/auth/user/" + mail;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        User u1 = new Gson().fromJson(json, new TypeToken<User>() {
                        }.getType());
                        //Save User to Storage
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.user_data), u1.toString());
                        editor.commit();

                        if (u1.getCountry() == null) {
                            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_landingScreen);
                        } else {
                            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_mapFragment);
                        }
                    }
                }, new Response.ErrorListener() {

            //Errorhandling
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }

        }) {
            //Add Headers to Reqeust
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


