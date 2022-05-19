package com.example.question2;

import android.content.Context;
import android.media.Rating;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.question2.Model.OpenAnswerQuestion;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class AnswersRatingFragment extends Fragment {
    private TextView textTitle;
    private int position;
    private ArrayList<String> answers;
    private float finalPoints;
    private String title,getLower,getHigher;

    public AnswersRatingFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_answers_rating, container, false);
        textTitle = view.findViewById(R.id.title_rating_question_answer);
        RatingBar ratingBar = view.findViewById(R.id.ratingBarAnswer);
        ratingBar.setFocusable(false);
        TextView textPoints = view.findViewById(R.id.percentage_rating_total);
        TextView lowerExtreme = view.findViewById(R.id.lower_extreme_answer);
        TextView higherExtreme = view.findViewById(R.id.higher_extreme_answer);

        answers = new ArrayList<>();

        //navigation filling

        if (getArguments().getString("title")!=null) {
            title = getArguments().getString("title");
            position = getArguments().getInt("position");
            getLower = getArguments().getString("left");
            getHigher = getArguments().getString("right");
            lowerExtreme.setText(getLower);
            higherExtreme.setText(getHigher);
            textTitle.setText(title);
            if (getArguments().getStringArrayList("answers") != null){
                ArrayList<String> list = getArguments().getStringArrayList("answers");
                for (String s: list){
                    answers.add(s);
                }
                finalPoints = 0;
                for (int i=0; i<answers.size(); i++){
                    float auxPoints = 0;
                    auxPoints = Float.parseFloat(answers.get(i));
                    finalPoints += auxPoints;
                }
                finalPoints = finalPoints/answers.size();
                textPoints.setText(Float.toString(finalPoints));
                ratingBar.setRating(finalPoints);
            }

        }

        return view;

    }
}