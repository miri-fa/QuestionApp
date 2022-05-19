package com.example.question2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.question2.Model.Questionnaire;
import com.example.question2.Model.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeamsTeacherActivity extends AppCompatActivity {
    Button createTeamButton;
    EditText teamTitle, accessCode;
    private ExpandableListAdapter adapter;
    private ExpandableListView expLV;
    private ArrayList<String> listTeams;
    private Map<String, ArrayList<String>> mapChild;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_teams_teacher);
        teamTitle = findViewById(R.id.createTeamTitle);
        accessCode = findViewById(R.id.createTeamPassword);
        expLV = findViewById(R.id.TeacherTeamsList);
        user = FirebaseAuth.getInstance().getCurrentUser();
        listTeams = new ArrayList<>();
        mapChild = new HashMap<>();
        loadData();



        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Team team = new Team();
                String title = teamTitle.getText().toString().trim();
                String access = accessCode.getText().toString().trim();
                team.setAccessCode(access);
                team.setName(title);
                ArrayList<String> students = new ArrayList<>();
                team.setStudents(students);
                team.setCreator(user.getEmail());
                FirebaseDatabase.getInstance().getReference("teams").push()
                        .setValue(team).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(TeamsTeacherActivity.this,
                                    "El equipo ha sido creado con éxito", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        createTeamButton = findViewById(R.id.createTeamButton);
        createTeamButton.setOnClickListener(onClickListener1);

    }

    private void loadData(){
        Context context = this;
        DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("teams");
        Query query = questionnaires.orderByChild("creator").equalTo(user.getEmail());
        ArrayList<String> e = new ArrayList();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    listTeams = new ArrayList<>();
                    mapChild = new HashMap<>();
                    int index = 0;
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String name = data.child("name").getValue(String.class);
                        String code = data.child("accessCode").getValue(String.class);
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator =
                                new GenericTypeIndicator<Map<String, String>>() {};
                        Map<String, String> map = data.child("students").getValue(genericTypeIndicator );
                        ArrayList<String> students = new ArrayList<>();
                        String nameAndCode = "Equipo: " + name + "\nCódigo de acceso: "
                                + code;

                        listTeams.add(nameAndCode);
                        if (map != null) {

                                for (Object s : map.values()) {
                                    students.add((String) s);
                                }
                        }
                        mapChild.put(listTeams.get(index), students);
                        index ++;
                    }
                    adapter = new ExpLVAdapterTeacher(context,listTeams,mapChild);
                    expLV.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
