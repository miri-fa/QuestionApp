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


public class ChooseFragment extends Fragment{
  private Spinner spinner;
  private FragmentManager fragmentManager;
  private FragmentTransaction fragmentTransaction;
  private boolean work;
  public ChooseFragment(){

  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =inflater.inflate(R.layout.fragment_choose, container, false);
      spinner = view.findViewById(R.id.optionsQuestion);
      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.questions, android.R.layout.simple_spinner_item);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      work = false;
      spinner.setAdapter(adapter);

      //when a choice of type of question is made, call the method for the question and call the fragment
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          fragmentManager = getParentFragmentManager();
          fragmentTransaction =fragmentManager.beginTransaction();
          if (work){
          if (position == 1){
            setOpenAnswerQuestion(null);
          } else if (position == 2) {
            OneChoiceFragment oneChoiceFragment = new OneChoiceFragment();
            Bundle args = new Bundle();
            args.putString("title", null);
            args.putStringArrayList("questions",null);
            oneChoiceFragment.setArguments(args);
            fragmentTransaction.replace(R.id.fragment_container_2, oneChoiceFragment);
            fragmentTransaction.commit();
          }else if (position == 3){
            MultipleChoiceFragment multipleChoiceFragment = new MultipleChoiceFragment();
            Bundle args = new Bundle();
            args.putString("title", null);
            args.putString("questions", null);
            multipleChoiceFragment.setArguments(args);
            fragmentTransaction.replace(R.id.fragment_container_2, multipleChoiceFragment);
            fragmentTransaction.commit();
          } else if (position == 4){
            RatingQuestionFragment ratingQuestionFragment = new RatingQuestionFragment();
            Bundle args = new Bundle();
            args.putString("title", null);
            args.putString("left", null);
            args.putString("right", null);
            ratingQuestionFragment.setArguments(args);
            fragmentTransaction.replace(R.id.fragment_container_2,ratingQuestionFragment);
            fragmentTransaction.commit();
          } else if (position==0) {
            if (fragmentManager.findFragmentById(R.id.fragment_container_2) != null) {
              fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fragment_container_2)).commit();
            }
          }
          }
          work = true;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
      });

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

  //for a score question, send the fragment the parameters
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

  //for a open answer question, send the fragment the parameters
  public void setOpenAnswerQuestion(String title){
    setSpinner(1);
    fragmentManager = getParentFragmentManager();

    OpenQuestionFragment openQuestionFragment = new OpenQuestionFragment();
    Bundle args = new Bundle();
    args.putString("title", title);
    openQuestionFragment.setArguments(args);
    fragmentManager.beginTransaction().replace(R.id.fragment_container_2, openQuestionFragment).commit();

  }

  //for a one choice question, send the fragment the parameters
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

  //for a multiple choice question, send the fragment the parameters
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