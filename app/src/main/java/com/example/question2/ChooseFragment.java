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

import java.util.ArrayList;


public class ChooseFragment extends Fragment {
  private Spinner spinner;
  private FragmentManager fragmentManager;
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
      spinner.setAdapter(adapter);
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          fragmentManager = getParentFragmentManager();
          FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
          if (position == 1){
            OpenQuestionFragment openQuestionFragment = new OpenQuestionFragment();
            fragmentTransaction.replace(R.id.fragment_container_2,openQuestionFragment);
            fragmentTransaction.commit();
          } else if (position == 2) {
            OneChoiceFragment oneChoiceFragment = new OneChoiceFragment();
            fragmentTransaction.replace(R.id.fragment_container_2, oneChoiceFragment);
            fragmentTransaction.commit();
          }else if (position == 3){
            MultipleChoiceFragment multipleChoiceFragment = new MultipleChoiceFragment();
            fragmentTransaction.replace(R.id.fragment_container_2, multipleChoiceFragment);
            fragmentTransaction.commit();
          } else if (position == 4){
            RatingQuestionFragment ratingQuestionFragment = new RatingQuestionFragment();
            fragmentTransaction.replace(R.id.fragment_container_2,ratingQuestionFragment);
            fragmentTransaction.commit();
          } else if (position==0){
            if (fragmentManager.findFragmentById(R.id.fragment_container_2) != null) {
              fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fragment_container_2)).commit();
            }
          }
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
      fragmentManager = getParentFragmentManager();
    }
}