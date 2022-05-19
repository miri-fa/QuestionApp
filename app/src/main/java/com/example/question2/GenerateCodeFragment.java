package com.example.question2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.question2.Model.ChoicesQuestion;
import com.example.question2.Model.OpenAnswerQuestion;
import com.example.question2.Model.Question;
import com.example.question2.Model.Questionnaire;
import com.example.question2.Model.ScoreQuestion;
import com.example.question2.Model.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;


public class GenerateCodeFragment extends Fragment{
    private Button finishButton;
    private boolean repeated;
    private TextView codeText;
    private ArrayList<String> arrayTeams, arrayStudents, arrayTeamsChosen;
    private ArrayAdapter<String> adapter, adapterTeams;
    public GenerateCodeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_generate_code, container, false);
        arrayStudents = new ArrayList();
        arrayTeams = new ArrayList<>();
        arrayTeamsChosen = new ArrayList<>();
        String code = getArguments().getString("code");
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("questionnaires");
                Query queryQuestionnaire = questionnaires.orderByChild("code").equalTo(code);
                queryQuestionnaire.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot ds : snapshot.getChildren()){
                                String questionnaireID = ds.getKey();
                                FirebaseDatabase.getInstance()
                                        .getReference("questionnaires/"+questionnaireID)
                                        .child("assignedStudents").push()
                                        .setValue(arrayStudents);
                                FirebaseDatabase.getInstance()
                                        .getReference("questionnaires/"+questionnaireID)
                                        .child("assignedTeams")
                                        .push().setValue(arrayTeamsChosen);
                            }
                        }
                        Intent intent = new Intent(getActivity(), CreateQuestionnaireActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                }
        };
        finishButton = view.findViewById(R.id.finish_making_questionnaire);
        finishButton.setOnClickListener(onClickListener);

        codeText = view.findViewById(R.id.questionnaire_code_name);
        codeText.setText(code);

        EditText newElement = view.findViewById(R.id.input_student_assign);

        ListView listView = view.findViewById(R.id.list_assign_students);
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1,arrayStudents);
        listView.setAdapter(adapter);


        Button btn = view.findViewById(R.id.add_student_assign);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String newItem = newElement.getText().toString();
                arrayStudents.add(newItem);
                adapter.notifyDataSetChanged();
            }
        });

        ListView listViewTeams = view.findViewById(R.id.list_assign_teams);
        adapterTeams = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, arrayTeams);
        listViewTeams.setAdapter(adapterTeams);
        listViewTeams.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference teams = FirebaseDatabase.getInstance().getReference("teams");
        Query queryTeams = teams.orderByChild("creator").equalTo(user.getEmail());
        queryTeams.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Team t = ds.getValue(Team.class);
                        arrayTeams.add(t.getName());
                    }

                    listViewTeams.setAdapter(adapterTeams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listViewTeams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                SparseBooleanArray checked = listViewTeams.getCheckedItemPositions();
                for (int i = 0; i < listViewTeams.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        arrayTeamsChosen.add(listViewTeams.getItemAtPosition(i).toString());
                    }
                }
            }
        });

        return view;

    }

}