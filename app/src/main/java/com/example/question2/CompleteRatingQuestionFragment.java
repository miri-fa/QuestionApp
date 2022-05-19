package com.example.question2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.question2.Model.OpenAnswerQuestion;
import com.example.question2.Model.ScoreQuestion;

import java.util.ArrayList;


public class CompleteRatingQuestionFragment extends Fragment {
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private TextView textTitle, lowerExtreme, higherExtreme;
    private FragmentCompleteRatingQuestionListener listener;
    private RatingBar ratingBar;
    private int position;
    private String title, lower, higher, getTitle, getLower, getHigher;

    public CompleteRatingQuestionFragment(){

    }
    public interface FragmentCompleteRatingQuestionListener{
        void onInputRatingQuestionSent(float scoreQuestionAnswer, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_complete_rating_question, container, false);
        ScoreQuestion question = new ScoreQuestion();
        textTitle = view.findViewById(R.id.title_rating_question_complete);
        lowerExtreme = view.findViewById(R.id.lower_extreme_complete);
        higherExtreme = view.findViewById(R.id.higher_extreme_complete);
        ratingBar = view.findViewById(R.id.ratingBarComplete);
        ratingBar.setFocusable(false);

        //Fill question text to show what the teacher wrote
        if (getArguments().getString("title")!=null) {
            getTitle = getArguments().getString("title");
            getLower = getArguments().getString("left");
            getHigher = getArguments().getString("right");
            position = getArguments().getInt("position");
            textTitle.setText(getTitle);
            lowerExtreme.setText(getLower);
            higherExtreme.setText(getHigher);
            if ((getArguments().getString("answer")!=null)&&(getArguments().getString("answer")!="")){
                ratingBar.setRating(Float.parseFloat(getArguments().getString("answer")));
            }
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float r = ratingBar.getRating();
                listener.onInputRatingQuestionSent(r, position);
            }
        });




        return view;

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentCompleteRatingQuestionListener){
            listener = (FragmentCompleteRatingQuestionListener) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener=null;
    }
}