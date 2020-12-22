package com.example.Emotions;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Emotions.Adapters.MyAdapter;
import com.example.Emotions.models.Question;
import com.example.Emotions.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.RangeSlider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnswerQuestionsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RangeSlider rangeSlider;
    private List<Question> questionlistReq = new ArrayList<Question>();
    private int pointID;
    private SwipeRefreshLayout swipeContainer;
    public AnswerQuestionsFragment(int pointID) {
        this.pointID = pointID;
    }
    public AnswerQuestionsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_answer_questions, container, false);

        Log.d("ID", String.valueOf(pointID));
        recyclerView = view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        GetAllQuestions(view);
        Button button = view.findViewById(R.id.button_questionstest);
        Button buttonCancel = view.findViewById(R.id.btn_cancelQuestions);

        //cancel answering the questions
        buttonCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Sicher")
                            .setMessage("Wollen Sie sicher abbrechen?")
                            .setIcon(R.drawable.ic_logo)
                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Navigation.findNavController(getView()).navigate(R.id.action_questionFragment_to_QR_Scanner);
                                }
                            })
                            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            });


        //finish answering the questions => when dialog is clicked positiv
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mAdapter.notifyDataSetChanged();
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Danke")
                            .setMessage("Befragung beenden?")
                            .setIcon(R.drawable.ic_logo)
                            .setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Pass the answers of all questions to the database
                                    for(Question question : questionlistReq){
                                        passAllAnswertoDB(String.valueOf(question.getTest()), String.valueOf(question.getId()),String.valueOf(question.getFreeText()));
                                    }
                                    updateLeaderBoardAndPoints();
                                    Navigation.findNavController(getView()).navigate(R.id.action_questionFragment_to_QR_Scanner);
                                }
                            })
                            .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            });

        return view;
    }


    //pass the answer to the Backend
    public void passAllAnswertoDB(final String Text, final String questionID,final String TextFielText)  {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlPost = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/answer/create";

        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("text", Text);
                params.put("question_ID", questionID);
                params.put("TextFielText", TextFielText);
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String user = sharedPref.getString(getString(R.string.user_data), null);
                User u1  = new Gson().fromJson(user, new TypeToken<User>() {}.getType());
                params.put("user_ID", String.valueOf(u1.getId()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Requested-With", "XMLHttpRequest");
                //Get Token from Storage
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String token = sharedPref.getString(getString(R.string.access_token), null);
                params.put("Authorization", "Bearer " + token );
                return params;
            }

        };
        queue.add(postRequest);
    }




    //Get All Questions from the Backend
    public void GetAllQuestions(View view){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final String urlGet = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/questions/toPoint/" + pointID;

        //ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ProgressDialog progressBar;
        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Fragen herunterladen...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  questionlistReq.clear();
                        String json = response.toString();
                        Log.d("json-response", response.toString());
                        List<Question> tempQuestions = new Gson().fromJson(json, new TypeToken<List<Question>>() {}.getType());
                        for(Question question : tempQuestions){
                            questionlistReq.add(question);
                        }
                        if(questionlistReq.size() == 0){
                            TextView emptyState = view.findViewById(R.id.empty_view);
                            Button button = view.findViewById(R.id.button_questionstest);
                            Button buttonCancel = view.findViewById(R.id.btn_cancelQuestions);

                            emptyState.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            button.setVisibility(View.INVISIBLE);
                            buttonCancel.setVisibility(View.INVISIBLE);

                          //  progressBar.setVisibility(View.INVISIBLE);
                        }
                        mAdapter = new MyAdapter(questionlistReq, getContext(),progressBar);
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



    //Add the point to the User when the quetions are answerd
    public void updateLeaderBoardAndPoints()  {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String user = sharedPref.getString(getString(R.string.user_data), null);
        User u1  = new Gson().fromJson(user, new TypeToken<User>() {}.getType());
        final String urlPost = getString(R.string.ServerIP)+ "/Bachelorarbeit/public/api/auth/leaderboard/"+ u1.getId() + "/" + pointID;
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlPost,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {@Override
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
        queue.add(postRequest);
    }
}
