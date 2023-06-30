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


public class AnswersOpenChoiceFragment extends Fragment {
    private TextView textTitle;
    private int position;
    private ArrayList<String> answers;
    private String title;

    public AnswersOpenChoiceFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_answers_open_choice, container, false);
        textTitle = view.findViewById(R.id.see_title_open);
        ListView listView = view.findViewById(R.id.answers_open_list);
        answers = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity().getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, answers);
        listView.setAdapter(adapter);

        //create view for answers

        if (getArguments().getString("title")!=null) {
            title = getArguments().getString("title");
            position = getArguments().getInt("position");
            textTitle.setText(title);
            if (getArguments().getStringArrayList("answers") != null){
                ArrayList<String> list = getArguments().getStringArrayList("answers");
                for (String s: list){
                    answers.add(s);
                }
                listView.setAdapter(adapter);
            }
        }

        return view;

    }
}