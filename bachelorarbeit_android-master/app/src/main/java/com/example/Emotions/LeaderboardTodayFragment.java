package com.example.Emotions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.Adapters.LeaderboardApapter_1;
import com.example.Emotions.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LeaderboardTodayFragment extends Fragment {
    private RecyclerView recyclerView;
    private LeaderboardApapter_1 mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> userlistReq = new ArrayList<User>();
    TabLayout tabLayout;
    ViewPager viewPager;
    private SwipeRefreshLayout swipeContainer;

    public LeaderboardTodayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_leaderboard_today, container, false);
        GetAllUsers(view);

        recyclerView = view.findViewById(R.id.leaderboard_recycler_view_1);
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                GetAllUsers(view);
                mAdapter.clear();
                swipeContainer.setRefreshing(false);
            }
        });


        return view;
    }


    public void GetAllUsers(View view){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/points/leaderboard/1";

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONArray response) {
                        userlistReq.clear();
                        String json = response.toString();
                        Log.d("json-response", response.toString());
                        List<User> tempQuestions = new Gson().fromJson(json, new TypeToken<List<User>>() {}.getType());
                        for(User user : tempQuestions){
                            userlistReq.add(user);
                        }

                        Comparator<User> compareByPoints = (User o1, User o2) -> o2.getPoints_1() - o1.getPoints_1();
                        userlistReq.sort(compareByPoints);
                        TextView emptyState = view.findViewById(R.id.empty_view);
                        if(userlistReq.size() == 0){
                            emptyState.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                        mAdapter = new LeaderboardApapter_1(userlistReq, getContext());
                        recyclerView.setAdapter(mAdapter);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Requested-With", "XMLHttpRequest");
                //Get Token from Storage
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String token = sharedPref.getString(getString(R.string.access_token), null);
                String Beaerer_Token = sharedPref.getString(getString(R.string.access_token), null);
                //  Log.d("Token_Storage", Beaerer_Token);
                params.put("Authorization", "Bearer " + Beaerer_Token );
                //Just for Testing
        //        params.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMzI0YjViZjk4ZmZiOTliYjY4ZmU2ZDZhMjBjMzE3NjYzYzM4NWYxY2Q1ZjA0ODYxZjRlZWE2ZTRiNzQwODg2NjExOGE0OWUwZTg0ZGE5NzYiLCJpYXQiOjE1OTU5NDM5ODcsIm5iZiI6MTU5NTk0Mzk4NywiZXhwIjoxNjI3NDc5OTg3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.Br6JYroor67tCQImDrJdQOj3y8Fq2qoZOBsEgFyF_jbPOnBolmxSrmKKYtJ87VqEOqSSLdmhwAZxJTg4mfFKsuGShSubp31IWfpu9qpxH6fQyOOrQRl_i466ioIahmce0R_ljb4FmaWyUFG9GbYIuucix2Z6GDejz0LYy3KUJCid8BNsWJUHweki8rDsH-KsTLwX8ogSD3VGJhav4h5CIvQtxl7O2kUT7iV7X7GT0zitNLyQSj9AXAf71cdm-_zkaWt5t_qC-GkfXK7v_bayO7FyKSuo1KXctNs-cbjU4s1VwW_lEDQXGsHlww3cxQnRys4vEaP-LJgkJJHX7P9rRK84iw79zmkmGbCBgOyu3nLUrgwS2QPA9y5GY3x-FPrHuLgKkZJbbfPC_R3puXU7umPcNgU66nULxTsI6ATMlPbCof14atILpsmlorMzZOKUt45YElOx3AtfrHASkFEpeznY10chBhuELAq3R4pYlJQeizwwnY1jiG-0RcwSMbk1yy_roNCCuhTMw2Pi9j5SqudBmnLvK4HqIE7B9I80CyJC5ngnm8vVfAkyifD_YtlRHQPbdNya1ivcJVfafq914XHdQWM4AzzOkmyA6GcRgEq-D4mDMmNkbLXTXLou6GDh2z8UXxi1chZdFcmOckaqBaFZWCiyPpugCqKk7-FV_0k" );
                return params;
            }
        };
        queue.add(getRequest);

    }
}
