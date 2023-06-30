package com.example.question2;

import android.content.Context;
import android.database.CursorJoiner;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.question2.Model.ChoicesQuestion;
import com.example.question2.Model.OpenAnswerQuestion;

import java.util.ArrayList;
import java.util.Arrays;


public class CompleteOneChoiceFragment extends Fragment {
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText newElement;
    private String title;
    private int position;
    private TextView textTitle;
    private FragmentChooseOneChoiceListener listener;
    private ChoicesQuestion q;

    public CompleteOneChoiceFragment(){

    }

    public interface FragmentChooseOneChoiceListener{
        void onInputOneChoiceSent(String answers, int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_complete_one_choice, container, false);
        q = new ChoicesQuestion();
        q.setMultipleChoice(false);
        ListView listView = view.findViewById(R.id.list_complete_one);
        textTitle = view.findViewById(R.id.title_one_question_complete);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_single_choice, android.R.id.text1, arrayList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if (getArguments().getString("title")!=null) {
            position = getArguments().getInt("position");
            title = getArguments().getString("title");
            textTitle.setText(title);
            ArrayList<String> list = getArguments().getStringArrayList("questions");
            for (String s: list){
                arrayList.add(s);
            }
            listView.setAdapter(adapter);
            if ((getArguments().getString("answer")!=null)&&(getArguments().getString("answer")!="")){
                String answer = getArguments().getString("answer");
                String[] parts = answer.split(" ");
                for (int i=0; i < arrayList.size(); i++){
                    for (int j=0; j< parts.length; j++) {
                        if (listView.getItemAtPosition(i).toString().equals(parts[j])) {
                            listView.setItemChecked(i, true);
                        }
                    }
                }
            }
        }

        //if an item is checked add to answer
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                ArrayList<String> answersList = new ArrayList<>();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        answersList.add(Integer.toString(i));
                    }
                }
                listener.onInputOneChoiceSent(answersList.get(0),position);
            }
        });


        textTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                q.setTitle(textTitle.getText().toString());
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
        if (context instanceof FragmentChooseOneChoiceListener){
            listener = (FragmentChooseOneChoiceListener) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener=null;
    }
}