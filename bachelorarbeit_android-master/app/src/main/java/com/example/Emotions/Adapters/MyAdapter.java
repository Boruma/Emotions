package com.example.Emotions.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.Emotions.models.Question;
import com.example.Emotions.R;
import com.google.android.material.slider.Slider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Question> questionList;
    private Context mContext;

    // View holder class whose objects represent each list item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView subTitleTextView;
        public EditText editTextFreeText;
        public Button btn;
        public Slider rangeSlider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
              titleTextView = itemView.findViewById(R.id.card_title);
              subTitleTextView = itemView.findViewById(R.id.card_subtitle);
              rangeSlider = itemView.findViewById(R.id.slider_1);
              editTextFreeText = itemView.findViewById(R.id.editTextFreeText);
        }

        public void bindData(final Question question, final Context context) {
              titleTextView.setText(question.getText());
              subTitleTextView.setText("Frage Nr. " + question.getId());

              editTextFreeText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    question.setFreeText(String.valueOf(editable));
                }
            });

            rangeSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(@NonNull Slider slider) {

                }

                @Override
                public void onStopTrackingTouch(@NonNull Slider slider) {
                    question.setTest(slider.getValue());
                }
            });

        }

    }

    public MyAdapter(List<Question> modelList, Context context, ProgressDialog progressBar) {
        questionList = modelList;
        mContext = context;
        progressBar.setProgress(100);
        progressBar.dismiss();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        // Return a new view holder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data for the item at position
        holder.bindData(questionList.get(position), mContext);

    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return questionList.size();
    }



}

