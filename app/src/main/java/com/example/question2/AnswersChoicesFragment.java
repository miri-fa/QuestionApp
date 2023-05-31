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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.question2.Model.OpenAnswerQuestion;

import java.util.ArrayList;


public class AnswersChoicesFragment extends Fragment {
    private TextView textTitle;
    private int position;
    private ArrayList<String> answers, questions, finalList;
    private ArrayList<Integer> points;
    private String title;

    public AnswersChoicesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_answers_choices, container, false);
        textTitle = view.findViewById(R.id.answer_choices_title);
        ListView listQuestions = view.findViewById(R.id.answers_choices_list);

        questions = new ArrayList<>();
        answers = new ArrayList<>();
        points = new ArrayList<>();
        finalList = new ArrayList<>();

        ArrayAdapter<String> adapterQuestions = new ArrayAdapter<String>
                (getActivity().getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, finalList);
        listQuestions.setAdapter(adapterQuestions);

        /*ArrayAdapter<Integer> adapterAnswers = new ArrayAdapter<Integer>
                (getActivity().getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, points);
        listPercentages.setAdapter(adapterAnswers);*/

        //navigation filling

        if (getArguments().getString("title")!=null) {
            title = getArguments().getString("title");
            position = getArguments().getInt("position");
            textTitle.setText(title);
            if (getArguments().getStringArrayList("answers") != null){
                ArrayList<String> list = getArguments().getStringArrayList("answers");
                for (String s: list){
                    answers.add(s);
                }
            }

            if (getArguments().getStringArrayList("questions") != null){
                ArrayList<String> list = getArguments().getStringArrayList("questions");
                for (String s: list){
                    questions.add(s);
                    points.add(0);
                }
            }

            for (int i=0; i<answers.size(); i++){
                String[] chosen = answers.get(i).split(" ");
                for (int j=0; j<chosen.length; j++){
                    int aux = points.get(j);
                    points.set(j,aux+1);
                }
                finalList.add(questions.get(i)+"       "+points.get(i).toString());
            }
            listQuestions.setAdapter(adapterQuestions);
            /*int total = 0;
            for (int i=0; i<points.size(); i++){
                total += points.get(i);
            }

            for (int i=0; i<points.size(); i++){
                int aux = 0;
            }*/

        }

        return view;

    }
}