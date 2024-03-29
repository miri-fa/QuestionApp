package com.example.question2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.question2.Model.ChoicesQuestion;
import com.example.question2.Model.OpenAnswerQuestion;

import java.util.ArrayList;
import java.util.Arrays;


public class OneChoiceFragment extends Fragment {
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText newElement;
    private String title;
    private EditText textTitle;
    private FragmentOneChoiceListener listener;
    private ChoicesQuestion q;

    public OneChoiceFragment(){

    }

    public interface FragmentOneChoiceListener{
        void onInputOneChoiceSent(ChoicesQuestion choicesQuestion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        q = new ChoicesQuestion();
        q.setMultipleChoice(false);
        ListView listView = view.findViewById(R.id.list_create_multiple_options);
        newElement = view.findViewById(R.id.input_multiple_choice);
        textTitle = view.findViewById(R.id.title_open_question);
        arrayList = new ArrayList<>();
        if (getArguments().getString("title")!=null) {
            title = getArguments().getString("title");
            textTitle.setText(title);
            ArrayList<String> list = getArguments().getStringArrayList("questions");
            for (String s: list){
                arrayList.add(s);
            }
        }
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1,arrayList);
        listView.setAdapter(adapter);
        //delete an option
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Quieres eliminar esta pregunta?");
                builder.setCancelable(true);
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(pos);
                        q.deleteChoice(pos);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //add choice
        Button btn = view.findViewById(R.id.add_option_open);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String newItem = newElement.getText().toString();
                arrayList.add(newItem);
                q.addChoice(newItem);
                adapter.notifyDataSetChanged();
                listener.onInputOneChoiceSent(q);
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
        if (context instanceof RatingQuestionFragment.FragmentRatingQuestionListener){
            listener = (OneChoiceFragment.FragmentOneChoiceListener) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener=null;
    }
}