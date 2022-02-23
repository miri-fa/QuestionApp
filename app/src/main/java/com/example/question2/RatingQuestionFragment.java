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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.question2.Model.OpenAnswerQuestion;
import com.example.question2.Model.ScoreQuestion;


public class RatingQuestionFragment extends Fragment {
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private EditText textTitle, lowerExtreme, higherExtreme;
    private FragmentRatingQuestionListener listener;
    private String title, lower, higher;

    public RatingQuestionFragment(){

    }
    public interface FragmentRatingQuestionListener{
        void onInputRatingQuestionSent(ScoreQuestion scoreQuestion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_rating_question, container, false);
        ScoreQuestion question = new ScoreQuestion();
        textTitle = view.findViewById(R.id.title_rating_question);
        textTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = textTitle.getText().toString();
                question.setTitle(title);
                listener.onInputRatingQuestionSent(question);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lowerExtreme = view.findViewById(R.id.lower_extreme);
        lowerExtreme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lower = lowerExtreme.getText().toString();
                question.setLowerSide(lower);
                listener.onInputRatingQuestionSent(question);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        higherExtreme = view.findViewById(R.id.higher_extreme);
        higherExtreme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                higher = higherExtreme.getText().toString();
                question.setHigherSide(higher);
                listener.onInputRatingQuestionSent(question);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof FragmentRatingQuestionListener){
            listener = (FragmentRatingQuestionListener) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener=null;
    }
}