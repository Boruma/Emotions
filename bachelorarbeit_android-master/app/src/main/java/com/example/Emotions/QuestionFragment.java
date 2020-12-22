package com.example.Emotions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Emotions.Adapters.LeaderboardApapter;
import com.example.Emotions.Adapters.ViewPageAdapterQuestions;
import com.example.Emotions.models.User;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class QuestionFragment extends Fragment {
    private RecyclerView recyclerView;
    private LeaderboardApapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<User> userlistReq = new ArrayList<User>();
    TabLayout tabLayout;
    ViewPager viewPager;

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        QuestionFragmentArgs args = QuestionFragmentArgs.fromBundle(getArguments());
        int message = args.getPointIDS();
        int  pointidFromQR = message;
        Log.d("MESSSAGE" , String.valueOf(message));

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.pager);
        tabLayout.addTab(tabLayout.newTab().setText("Fragen"));
        tabLayout.addTab(tabLayout.newTab().setText("Informationen"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPageAdapterQuestions adapter = new ViewPageAdapterQuestions(getContext(),getChildFragmentManager(), tabLayout.getTabCount(),pointidFromQR);
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

        return view;
    }

}
