package com.example.question2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.question2.Model.Question;
import com.example.question2.Model.ScoreQuestion;

import java.util.ArrayList;


public class ShowQuestionFragment extends Fragment{
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean work;
    public ShowQuestionFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_choose, container, false);

        return view;

    }

    public void restartSpinner(){
        spinner.setSelection(0);
        fragmentManager = getParentFragmentManager();
    }

    public void setSpinner(int pos){
        spinner.setSelection(pos);
        work = false;
        fragmentManager = getParentFragmentManager();
    }

    public void setScoreQuestion(String title, String left, String right){
        setSpinner(4);
        fragmentManager = getParentFragmentManager();


        RatingQuestionFragment ratingQuestionFragment = new RatingQuestionFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("left", left);
        args.putString("right", right);
        ratingQuestionFragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.fragment_container_2, ratingQuestionFragment).commit();

    }

    public void setOpenAnswerQuestion(String title){
        setSpinner(1);
        fragmentManager = getParentFragmentManager();

        OpenQuestionFragment openQuestionFragment = new OpenQuestionFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        openQuestionFragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.fragment_container_2, openQuestionFragment).commit();

    }

    public void setOneChoiceQuestion(String title, ArrayList<String> questions){
        setSpinner(2);
        fragmentManager = getParentFragmentManager();

        OneChoiceFragment oneChoiceFragment = new OneChoiceFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArrayList("questions",questions);
        oneChoiceFragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.fragment_container_2, oneChoiceFragment).commit();

    }

    public void setMultipleChoiceQuestion(String title, ArrayList<String> questions){
        setSpinner(3);
        fragmentManager = getParentFragmentManager();

        MultipleChoiceFragment multipleChoiceFragment = new MultipleChoiceFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArrayList("questions",questions);
        multipleChoiceFragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.fragment_container_2, multipleChoiceFragment).commit();
    }
}