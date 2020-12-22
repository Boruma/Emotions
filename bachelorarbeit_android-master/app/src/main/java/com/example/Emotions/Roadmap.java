package com.example.Emotions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.models.Quest_user;
import com.example.Emotions.models.Target;
import com.example.Emotions.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Roadmap extends Fragment {
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();
    public Roadmap() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_roadmap, container, false);


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

        NavigationView navigationView = view.findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_settings:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_settings);
                        break;
                    case R.id.nav_profile:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_profileFragment);
                        break;
                    case R.id.nav_map:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_mapFragment);
                        break;
                    case R.id.nav_scanner:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_QR_Scanner);
                        break;
                    case R.id.nav_leaderborad:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_leaderboardFragment);
                        break;
                    case R.id.nav_roadmap:
                        mDrawer.closeDrawers();
                        break;

                }
                return true;
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_leaderboard:
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_leaderboardFragment);
                        break;
                    case R.id.action_map:
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_mapFragment);
                        break;
                    case R.id.action_scanner:
                        Navigation.findNavController(getView()).navigate(R.id.action_roadmap_to_QR_Scanner);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetAllQuestsandTargestsforUser(view);
    }




    public void GetAllQuestsandTargestsforUser(View view){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //Get User Data from Storage
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(getString(R.string.user_data), null);
        User u1  = new Gson().fromJson(user, new TypeToken<User>() {}.getType());
         final String urlGet = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/quest/toUser/" +u1.getId();


        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        Log.d("response_Quests",json);
                        try {
                            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                            Display display = wm.getDefaultDisplay();
                            DisplayMetrics metrics = new DisplayMetrics();
                            wm.getDefaultDisplay().getMetrics(metrics);

                            LinearLayout linearLayout = view.findViewById(R.id.Layout);


                            JSONObject reader = new JSONObject(json);
                            String targets = reader.getString("targets");
                            List<Target> Tragetlist = new Gson().fromJson(targets, new TypeToken<List<Target>>() {}.getType());

                            String Allquests = reader.getString("quests");
                            Gson gsonAllQUEST = new Gson();
                            List<Quest_user> QuestList = gsonAllQUEST.fromJson(Allquests, new TypeToken<List<Quest_user>>() {}.getType());

                            Log.d("Targets", Tragetlist.toString());
                            Log.d("All Quests", QuestList.toString());

                            //MAXIMAL PLATZ FÜR 7 Quests
                            int marginTop = (linearLayout.getHeight()/7)/5;
                            int marginTop1 = (int) ((linearLayout.getHeight()/7)/3.75);

                            int marginLeft = (int) (linearLayout.getWidth()/3);
                            int marginLeft1 = linearLayout.getWidth()/5;

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(marginLeft,marginTop , 20, 20);

                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            params1.setMargins(marginLeft1, marginTop1, 20, 20);

                            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            params3.setMargins(marginLeft,marginTop1, 20, 20);

                            for (int i = 0; i < QuestList.size(); i++) {

                                if(i<=7){
                                ConstraintLayout constraintLayout = new ConstraintLayout(getContext());
                                ImageView imageView = new ImageView(getContext());
                                imageView.setImageDrawable(getResources().getDrawable(R.drawable.task_icon));
                                imageView.setId(Integer.parseInt(1000 + QuestList.get(i).getQuestobj().getId()));

                                ImageView imageViewHalf = new ImageView(getContext());
                                imageViewHalf.setImageDrawable(getResources().getDrawable(R.drawable.point_half));
                                imageViewHalf.setId(Integer.parseInt(1001 + QuestList.get(i).getQuestobj().getId()));
                                imageViewHalf.setVisibility(View.INVISIBLE);

                                ImageView imageViewFull = new ImageView(getContext());
                                imageViewFull.setImageDrawable(getResources().getDrawable(R.drawable.point_full));
                                imageViewFull.setId(Integer.parseInt(1002 + QuestList.get(i).getQuestobj().getId()));
                                imageViewFull.setVisibility(View.INVISIBLE);

                                TextView textViewName = new TextView(getContext());

                                if(i%2==0){
                                    if(i==0){
                                        //ERSTES ELEMENT
                                        textViewName.setId(Integer.parseInt(1003 + QuestList.get(i).getQuestobj().getId()));
                                        textViewName.setText(QuestList.get(i).getQuestobj().getName());
                                        textViewName.setPadding((int) (linearLayout.getWidth()/3.5),(linearLayout.getHeight()/28),0,0);
                                        constraintLayout.setLayoutParams(params);
                                    }else {
                                        //RECHTE SEITE
                                        textViewName.setId(Integer.parseInt(1003 + QuestList.get(i).getQuestobj().getId()));
                                        textViewName.setText(QuestList.get(i).getQuestobj().getName());
                                       textViewName.setPadding((int) (linearLayout.getWidth()/3.5),(linearLayout.getHeight()/28),0,0);
                                        constraintLayout.setLayoutParams(params3);
                                    }
                                }else {
                                        //LINKE SEITE
                                        textViewName.setId(Integer.parseInt(1003 + QuestList.get(i).getQuestobj().getId()));
                                        textViewName.setText(QuestList.get(i).getQuestobj().getName());
                                        textViewName.setPadding((int) (linearLayout.getWidth()/3.5), (linearLayout.getHeight()/28), 0, 0);
                                        constraintLayout.setLayoutParams(params1);
                                }

                                constraintLayout.addView(imageView);
                                constraintLayout.addView(imageViewHalf);
                                constraintLayout.addView(imageViewFull);
                                constraintLayout.addView(textViewName);
                                linearLayout.addView(constraintLayout);


                                switch (QuestList.get(i).getProgress()) {
                                    case "0":
                                        break;
                                    case "1":
                                        imageViewHalf.setVisibility(View.VISIBLE);
                                        break;
                                    case "2":
                                        imageViewFull.setVisibility(View.VISIBLE);
                                        break;
                                }

                            }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
