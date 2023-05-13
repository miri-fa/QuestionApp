package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.question2.Model.Questionnaire;
import com.example.question2.Model.Student;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivityStudent extends AppCompatActivity {
    ListView listView;
    ArrayList userTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_student);
        userTeams = new ArrayList();


        //Help button
        View.OnClickListener onClickListenerHelp = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.fragment_help, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(findViewById(R.id.frameLayout2), Gravity.CENTER, 0, 0);
                TextView message = popupWindow.getContentView().findViewById(R.id.helpText);
                HelpManager help = new HelpManager();
                 message.setText(help.getContent(1));

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        };
        Button buttonHelp = (Button) findViewById(R.id.helpmainstudent);
        buttonHelp.setOnClickListener(onClickListenerHelp);

        listView = findViewById(R.id.student_main_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = listView.getItemAtPosition(position).toString().trim();
                Intent intent = new Intent(MainActivityStudent.this, SeeAnswersActivity.class);
                intent = intent.putExtra("title",text);
                intent = intent.putExtra("role","1");
                MainActivityStudent.this.finish();
                startActivity(intent);
            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.main_list_item, R.id.label_main,arrayList);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




            DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("questionnaires");
        questionnaires.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //We get the teams the user is on
                    DatabaseReference teams = FirebaseDatabase.getInstance().getReference("teams");
                    Query query = teams;
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int index = 0;
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    String name = data.child("name").getValue(String.class);
                                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                                    };
                                    Map<String, String> map = data.child("students").getValue(genericTypeIndicator);
                                    if (map != null) {
                                        if (map.containsValue(user.getEmail())) {
                                            userTeams.add(name);
                                            index++;
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //for each questionnaire, we look if the user or team the user is in are assigned
                    //If not, we do not add them to the list to show
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Questionnaire q = ds.getValue(Questionnaire.class);
                                ArrayList<String> mapTeams = new ArrayList<>();
                                ArrayList<String> mapStudents = new ArrayList<>();
                                HashMap<String, ArrayList<String>> valueTeams = (HashMap<String, ArrayList<String>>) ds.child("assignedTeams").getValue();
                                if(valueTeams!=null){
                                    for (Map.Entry<String, ArrayList<String>> entry: valueTeams.entrySet()) {
                                        mapTeams = entry.getValue();
                                    }}

                                HashMap<String, ArrayList<String>> valueStudents = (HashMap<String, ArrayList<String>>) ds.child("assignedStudents").getValue();
                                if (valueStudents!=null){
                                    for (Map.Entry<String, ArrayList<String>> entry: valueStudents.entrySet()) {
                                        mapStudents = entry.getValue();
                                    }}


                        /*GenericTypeIndicator<ArrayList<ArrayList<String>>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<ArrayList<String>>>() {};
                        ArrayList<ArrayList<String>> mapTeams = ds.child("assignedTeams").getValue(genericTypeIndicator);
                        GenericTypeIndicator<Map<String, ArrayList<String>>> genericTypeIndicator2 = new GenericTypeIndicator<Map<String, ArrayList<String>>>() {};
                        Map<String, ArrayList<String>> mapStudents = ds.child("assignedStudents").getValue(genericTypeIndicator2);
*/
                                Boolean found = false;
                                if (mapTeams!=null){
                                    for(int i=0; i<userTeams.size(); i++){
                                        if (mapTeams.contains(userTeams.get(i))){
                                            found = true;
                                        }
                                    }
                                }

                                if (mapStudents!=null && !found && mapStudents.contains(user.getEmail())){
                                    found = true;
                                }
                                if (found){
                                    arrayList.add(q.getTitle());
                                }

                            }

                            listView.setAdapter(adapter);
                        }
                    }, 2000); // 5 seconds to load everything

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivityStudent.this, FillQuestionnaireActivity.class);
                MainActivityStudent.this.finish();
                startActivity(intent);
            }
        };

        Button buttonLogin = (Button) findViewById(R.id.fill_questionnaire_button);
        buttonLogin.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivityStudent.this, ProfileStudentActivity.class);
                MainActivityStudent.this.finish();
                startActivity(intent);
            }
        };

        Button buttonProfile = (Button) findViewById(R.id.profilemainstudent);
        buttonProfile.setOnClickListener(onClickListener2);

    }
}
