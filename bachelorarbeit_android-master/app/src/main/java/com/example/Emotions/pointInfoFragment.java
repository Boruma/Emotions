package com.example.Emotions;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.Point;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class pointInfoFragment extends Fragment {

    private int poindid;

    public pointInfoFragment(int pointID) {
        this.poindid = pointID;
    }
    public pointInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_point_info, container, false);
        gerPointData(poindid, view);

        return view;
    }

    public void gerPointData(int id, View view)  {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/points/" +id;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        Point p1  = new Gson().fromJson(json, new TypeToken<Point>() {}.getType());
                        TextView poitnname =  view.findViewById(R.id.pointname);
                        TextView poitntext =  view.findViewById(R.id.pointtext);
                        poitnname.setText(p1.getName());
                        poitntext.setText(p1.getText());
                        gerPointImage(id,view);
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


    public void gerPointImage(int id, View view)  {
        ImageView imageView = view.findViewById(R.id.imageView3);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/store_image/fetch_image/PointImage/toPoint/" +id;
        ImageRequest request = new ImageRequest(urlGet,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.logo);
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
        queue.add(request);
    }
}


