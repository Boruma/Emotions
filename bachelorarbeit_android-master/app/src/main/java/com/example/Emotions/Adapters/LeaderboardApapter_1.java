package com.example.Emotions.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Emotions.R;
import com.example.Emotions.models.User;

import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardApapter_1 extends RecyclerView.Adapter<LeaderboardApapter_1.MyViewHolderLeaderboard>{
    private List<User> userList;
    private Context mContext;

    // View holder class whose objects represent each list item
    public static class MyViewHolderLeaderboard extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView subTitleTextView;
        public TextView pointsTextView;

        public MyViewHolderLeaderboard(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.leaderboard_card_title);
            subTitleTextView = itemView.findViewById(R.id.leaderboard_card_subtitle);
            pointsTextView = itemView.findViewById(R.id.leaderboard_points);
        }

        public void bindData(final User user, final Context context) {
            titleTextView.setText(user.getName());
            subTitleTextView.setText(user.geteMail());
            pointsTextView.setText(String.valueOf(user.getPoints_1()));
        }


    }

    public LeaderboardApapter_1(List<User> modelList, Context context) {
        userList = modelList;
        mContext = context;
    }

    // Clean all elements of the recycler
    public void clear() {
        userList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        userList.addAll(list);
        notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SortItemsAscendeing(){
        Comparator<User> compareByPoints = (User o1, User o2) -> o1.getPoints() - o2.getPoints();
        userList.sort(compareByPoints);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SortItemsDecending(){
        Comparator<User> compareByPoints = (User o1, User o2) -> o2.getPoints() - o1.getPoints();
        userList.sort(compareByPoints);
    }

    @NonNull
    @Override
    public LeaderboardApapter_1.MyViewHolderLeaderboard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard_1, parent, false);
        // Return a new view holder
        return new LeaderboardApapter_1.MyViewHolderLeaderboard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardApapter_1.MyViewHolderLeaderboard holder, int position) {
        // Bind data for the item at position
        holder.bindData(userList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return userList.size();
    }
}
