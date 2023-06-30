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
import android.widget.TextView;

import com.example.question2.Model.OpenAnswerQuestion;


public class CompleteOpenQuestionFragment extends Fragment {
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private TextView textTitle;
    private EditText answerText;
    private FragmentCompleteOpenQuestionListener listener;
    private int position;
    private String title;

    public CompleteOpenQuestionFragment(){

    }
    public interface FragmentCompleteOpenQuestionListener{
        void onInputOpenQuestionSent(String openQuestionAnswer, int pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_complete_open_question, container, false);
        textTitle = view.findViewById(R.id.title_open_question_complete);

        //if there's arguments passed from another activity
        if (getArguments().getString("title")!=null) {
            title = getArguments().getString("title");
            position = getArguments().getInt("position");
            textTitle.setText(title);
            if ((getArguments().getString("answer")!=null)&&(getArguments().getString("answer")!="")){
                String answer =getArguments().getString("answer");
                answerText = view.findViewById(R.id.openquestionanswercomplete);
                answerText.setText(answer);
            }
        }

        answerText = view.findViewById(R.id.openquestionanswercomplete);
        answerText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onInputOpenQuestionSent(answerText.getText().toString(),position);
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
        if (context instanceof FragmentCompleteOpenQuestionListener){
            listener = (FragmentCompleteOpenQuestionListener) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener=null;
    }
}