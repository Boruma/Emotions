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
import com.example.Emotions.Adapters.LeaderboardApapter;
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


public class leaderboardAllTimeFragment extends Fragment {
    private RecyclerView recyclerView;
    private LeaderboardApapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> userlistReq = new ArrayList<User>();
    private SwipeRefreshLayout swipeContainer;
    TabLayout tabLayout;
    ViewPager viewPager;

    public leaderboardAllTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard_all_time, container, false);
        GetAllUsers(view);

        recyclerView = view.findViewById(R.id.leaderboard_recycler_view);
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
                GetAllUsers(view);
                mAdapter.clear();
                swipeContainer.setRefreshing(false);
            }
        });


        return view;
    }




    public void GetAllUsers(View view){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
       final String urlGet = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/points/leaderboard/All";

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

                        Comparator<User> compareByPoints = (User o1, User o2) -> o2.getPoints() - o1.getPoints();
                        userlistReq.sort(compareByPoints);
                        mAdapter = new LeaderboardApapter(userlistReq, getContext());
                        TextView emptyState = view.findViewById(R.id.empty_view);
                        if(userlistReq.size() == 0){
                            emptyState.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
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
                String Beaerer_Token = sharedPref.getString(getString(R.string.access_token), null);
                params.put("Authorization", "Bearer " + Beaerer_Token );
                return params;
            }
        };
        queue.add(getRequest);

    }
}
