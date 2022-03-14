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


public class OpenQuestionFragment extends Fragment {
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private EditText textTitle;
    private FragmentOpenQuestionListener listener;
    private String title;

    public OpenQuestionFragment(){

    }
    public interface FragmentOpenQuestionListener{
        void onInputOpenQuestionSent(OpenAnswerQuestion openAnswerQuestion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_open_question, container, false);
        textTitle = view.findViewById(R.id.title_open_question);

        //navigation filling
        if (getArguments().getString("title")!=null) {
            title = getArguments().getString("title");
            textTitle.setText(title);
        }

        textTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OpenAnswerQuestion q = new OpenAnswerQuestion();
                q.setTitle(textTitle.getText().toString());
                listener.onInputOpenQuestionSent(q);
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
        if (context instanceof FragmentOpenQuestionListener){
            listener = (FragmentOpenQuestionListener) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener=null;
    }
}