package com.example.Emotions.Adapters;

import android.content.Context;

import com.example.Emotions.LeaderboardTodayFragment;
import com.example.Emotions.leaderboardAllTimeFragment;
import com.example.Emotions.leaderboardWeekFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPageAdapterLeaderboard extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public ViewPageAdapterLeaderboard(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LeaderboardTodayFragment leaderboardTodayFragment = new LeaderboardTodayFragment();
                return leaderboardTodayFragment;
            case 1:
                leaderboardWeekFragment leaderboardWeekFrag = new leaderboardWeekFragment();
                return leaderboardWeekFrag;
            case 2:
                leaderboardAllTimeFragment leaderboardAllTimeFrag = new leaderboardAllTimeFragment();
                return leaderboardAllTimeFrag;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}