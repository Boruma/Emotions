package com.example.Emotions.Adapters;

import android.content.Context;

import com.example.Emotions.AnswerQuestionsFragment;
import com.example.Emotions.pointInfoFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPageAdapterQuestions extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    int pointID;

    public ViewPageAdapterQuestions(Context c, FragmentManager fm, int totalTabs, int pointID) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
        this.pointID = pointID;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AnswerQuestionsFragment answerQuestionsFragment = new AnswerQuestionsFragment(pointID);
                return answerQuestionsFragment;
            case 1:
                pointInfoFragment PointInfoFragment = new pointInfoFragment(pointID);
                return  PointInfoFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}