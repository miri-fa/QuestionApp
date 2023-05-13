package com.example.question2;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.question2.databinding.ActivityTeamsStudentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class TeamsStudentActivity extends AppCompatActivity {

    private Button joinTeamButton;
    private EditText teamTitle, accessCode;
    private ExpandableListAdapter adapter;
    private ExpandableListView expLV;
    private ArrayList<String> listTeams;
    private Map<String, ArrayList<String>> mapChild;
    private FirebaseUser user;
    private Button buttonJoin;
    private DatabaseReference databaseReference;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_teams_student);
        teamTitle = findViewById(R.id.joinTeamTitle);
        accessCode = findViewById(R.id.joinTeamPassword);
        expLV = findViewById(R.id.StudentTeamsList);
        user = FirebaseAuth.getInstance().getCurrentUser();
        listTeams = new ArrayList<>();
        mapChild = new HashMap<>();
        loadData();

        buttonJoin = findViewById(R.id.joinTeamButton);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String access = accessCode.getText().toString().trim();

                DatabaseReference teams = FirebaseDatabase.getInstance().getReference("teams");
                Query query = teams.orderByChild("accessCode").equalTo(access);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot ds : snapshot.getChildren()){
                                key = ds.getKey();
                            }
                            FirebaseDatabase.getInstance().getReference("teams")
                                    .child(key).child("students").push().setValue(user.getEmail())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(TeamsStudentActivity.this, "Te has unido al equipo con éxito", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        View.OnClickListener onClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TeamsStudentActivity.this, ProfileStudentActivity.class);
                TeamsStudentActivity.this.finish();
                startActivity(intent);
            }
        };

        Button buttonBack = (Button) findViewById(R.id.settingsGoBackStudent);
        buttonBack.setOnClickListener(onClickListener3);


    }

    private void loadData(){
        Context context = this;
        DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("teams");
        Query query = questionnaires;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    listTeams = new ArrayList<>();
                    mapChild = new HashMap<>();
                    int index = 0;
                    for (DataSnapshot data : snapshot.getChildren()) {
                        String name = data.child("name").getValue(String.class);
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                        Map<String, String> map = data.child("students").getValue(genericTypeIndicator );
                        if (map != null) {
                        if (map.containsValue(user.getEmail())) {
                            String nameAndCode = "Equipo: " + name;
                            listTeams.add(nameAndCode);
                                ArrayList<String> students = new ArrayList<>();
                                for (Object s : map.values()) {
                                    students.add((String)s);
                                }
                                mapChild.put(listTeams.get(index), students);
                                index ++;
                            }
                        }
                    }
                    adapter = new ExpLVAdapter(context,listTeams,mapChild);
                    expLV.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ExpLVAdapter(context,listTeams,mapChild);
        expLV.setAdapter(adapter);


    }

}
