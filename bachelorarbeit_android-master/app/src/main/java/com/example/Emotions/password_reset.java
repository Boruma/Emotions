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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class password_reset extends Fragment {

    public password_reset() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_reset, container, false);
        password_resetArgs args1 = password_resetArgs.fromBundle(getArguments());

        String token = args1.getToken();
        String mail = args1.getEmail();

        final EditText pwEdit = view.findViewById(R.id.et_password);
        final EditText cpwEdit = view.findViewById(R.id.et_repassword);


        Button btn_resetPW = view.findViewById(R.id.btn_resetPW);
        btn_resetPW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String pw = String.valueOf(pwEdit.getText());
                String cpw = String.valueOf(cpwEdit.getText());

                if(pw.length()==0){
                    pwEdit.requestFocus();
                    pwEdit.setError(getString(R.string.errorEmpty));
                }

                if(cpw.length()==0){
                    cpwEdit.requestFocus();
                    cpwEdit.setError(getString(R.string.errorEmpty));
                }
                if(cpw.length()!=0 && pw.length() != 0){
                    if(cpw.equals(pw)){
                        resetPW(mail,pw,cpw,token);
                    }else {
                        cpwEdit.requestFocus();
                        cpwEdit.setError("Passwörter müssen gleich sein");
                        pwEdit.requestFocus();
                        pwEdit.setError("Passwörter müssen gleich sein");
                    }

                }
            }
        });

        return view;
    }

    public void resetPW(final String mail, final String PW,final String CPW, final String token)  {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlPost = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/password/reset";

        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        Navigation.findNavController(getView()).navigate(R.id.action_password_reset_to_loginFragment);
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
                params.put("email", mail);
                params.put("password", PW);
                params.put("password_confirmation", CPW);
                params.put("token", token);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Requested-With", "XMLHttpRequest");
                //Get Token from Storage
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String token = sharedPref.getString(getString(R.string.access_token), null);
                params.put("Authorization", "Bearer " + token );
                return params;
            }

        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);



    }
}
