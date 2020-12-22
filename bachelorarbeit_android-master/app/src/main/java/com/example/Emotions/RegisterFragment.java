package com.example.Emotions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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

import com.android.volley.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {

    public RegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Get Elements from View
        final EditText editName = view.findViewById(R.id.et_name);
        final EditText editMail = view.findViewById(R.id.et_email);
        final EditText editPassword = view.findViewById(R.id.et_password);
        final EditText editRepassword = view.findViewById(R.id.et_repassword);
        final ImageView switchBTN = view.findViewById(R.id.switchToLogin);

        switchBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        final Button button = view.findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = editName.getText().toString();
                String Mail = editMail.getText().toString();
                String PW = editPassword.getText().toString();
                String RPW = editRepassword.getText().toString();

                //Validation
                if (name.length() == 0) {
                    editName.requestFocus();
                    editName.setError(getString(R.string.errorEmpty));
                } else if (!name.matches("[a-zA-Z ]+")) {
                    editName.requestFocus();
                    editName.setError("Bitte nur Buchstaben nutzen");
                }

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
                if (PW.length() < 7 ) {
                    editPassword.requestFocus();
                    editPassword.setError("Das Passwort muss mindestens 7 Zeichen lang sein");
                }
                if (RPW.length() == 0) {
                    editRepassword.requestFocus();
                    editRepassword.setError(getString(R.string.errorEmpty));
                }

                if (RPW.length() < 7) {
                    editRepassword.requestFocus();
                    editRepassword.setError("Das Passwort muss mindestens 7 Zeichen lang sein");
                }
                if (editMail.getError() == null && editName.getError() == null && editPassword.getError() == null && editRepassword.getError() == null) {
                    if (PW.equals(RPW)) {
                        RegisterUser(name, Mail, PW, RPW);
                    } else {
                        editPassword.requestFocus();
                        editPassword.setError("Passwörter müssen gleich sein");
                        editRepassword.requestFocus();
                        editPassword.setError("Passwörter müssen gleich sein");
                    }
                }
            }
        });

        return view;
    }

    //Register User in the Backend
    public void RegisterUser(final String name, final String email, final String PW, final String RPW) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlPost = getString(R.string.ServerIP) + "/Bachelorarbeit/public/api/auth/signup";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Toast.makeText(getContext(), "Erfogreich Registiert! Bitte E-Mail bestätigen!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_loginFragment);
                    }
                },
                //Errorhandling
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            //Add Parameters to Reqeust
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", PW);
                params.put("password_confirmation", RPW);
                return params;
            }
            //Add Headers to Reqeust
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Requested-With", "XMLHttpRequest");
                return params;
            }
        };
        queue.add(postRequest);
    }
}