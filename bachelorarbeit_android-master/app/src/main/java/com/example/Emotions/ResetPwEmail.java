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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ResetPwEmail extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_pw_email, container, false);

        final EditText editMail = view.findViewById(R.id.et_email_resetPW);


        Button btn = view.findViewById(R.id.btn_ResetPWMail);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String Mail = editMail.getText().toString();
                if(Mail.length()!=0) {
                    resetPW(Mail);
                }
                if(Mail.length() == 0){
                    editMail.requestFocus();
                    editMail.setError(getString(R.string.errorEmpty));
                }
            }
        });

        return view;
    }

    public void resetPW(String mail){
       RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
       final String urlResetPW = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/password/create";

        // prepare the Request
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlResetPW,
                new Response.Listener<String>(){
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

                        String message;
                        if (json.has("token")) {
                            try {
                                message = json.getString("token");
                                findResetData(message);
                            } catch (JSONException e) {
                                Log.d("JSONException", String.valueOf(e));                            }
                        }else{
                            Toast.makeText(getContext(), "Wir können keinen Nutzer mit diesen Daten finden", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", mail);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };
        // add it to the RequestQueue
        queue.add(postRequest);
    }


    public void findResetData(String token)  {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/password/find/" + token;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        String responseToken = response.toString();
                        Log.d("Token", responseToken);

                        JSONObject json = null;

                        try {
                            json = new JSONObject(responseToken);
                        } catch (JSONException e) {
                            Log.d("JSONException", String.valueOf(e));
                        }

                        try {
                            String email = json.getString("email");
                            String token = json.getString("token");

                            ResetPwEmailDirections.ActionResetPwEmailToPasswordReset2 action = ResetPwEmailDirections.actionResetPwEmailToPasswordReset2(token,email);
                            action.setToken(token);
                            action.setEmail(email);
                            Navigation.findNavController(getView()).navigate(action);
                        } catch (JSONException e) {
                            Log.d("JSONException", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
            }

        }){
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
        queue.add(getRequest);
    }
}
