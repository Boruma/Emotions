package com.example.Emotions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Emotions.Adapters.LeaderboardApapter;
import com.example.Emotions.Adapters.ViewPageAdapterLeaderboard;
import com.example.Emotions.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class LeaderboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private LeaderboardApapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> userlistReq = new ArrayList<User>();
    TabLayout tabLayout;
    ViewPager viewPager;
    public LeaderboardFragment() {
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
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        tabLayout = view.findViewById(R.id.tabLayout_leaderboard);
        viewPager = view.findViewById(R.id.pager_leaderboard);
        tabLayout.addTab(tabLayout.newTab().setText("Heute"));
        tabLayout.addTab(tabLayout.newTab().setText("Woche"));
        tabLayout.addTab(tabLayout.newTab().setText("Gesamt"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPageAdapterLeaderboard adapter = new ViewPageAdapterLeaderboard(getContext(),getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_leaderboard:
                        break;
                    case R.id.action_map:
                        Navigation.findNavController(getView()).navigate(R.id.action_leaderboardFragment_to_mapFragment);
                        break;
                    case R.id.action_scanner:
                        Navigation.findNavController(getView()).navigate(R.id.action_leaderboardFragment_to_QR_Scanner);
                        break;

                }
                return true;
            }
        });


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
                        Navigation.findNavController(getView()).navigate(R.id.action_leaderboardFragment_to_settings);
                        break;
                    case R.id.nav_profile:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_leaderboardFragment_to_profileFragment);
                        break;
                    case R.id.nav_map:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_leaderboardFragment_to_mapFragment);
                        break;
                    case R.id.nav_scanner:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_leaderboardFragment_to_QR_Scanner);
                        break;
                    case R.id.nav_leaderborad:
                        mDrawer.closeDrawers();
                        break;
                    case R.id.nav_roadmap:
                        mDrawer.closeDrawers();
                        Navigation.findNavController(getView()).navigate(R.id.action_leaderboardFragment_to_roadmap);
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
}
