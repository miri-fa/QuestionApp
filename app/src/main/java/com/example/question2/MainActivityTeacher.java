package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.question2.Model.Questionnaire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityTeacher extends AppCompatActivity {
    ListView listView;
    Query query;
    Questionnaire questionnaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_teacher);

        listView = findViewById(R.id.teacher_main_list);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.main_list_item, R.id.label_main,arrayList);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("questionnaires");
        query = questionnaires.orderByChild("author").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Questionnaire q = ds.getValue(Questionnaire.class);
                        arrayList.add(q.getTitle());
                    }

                    listView.setAdapter(adapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get elements from database
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                String text = listView.getItemAtPosition(itemPosition).toString().trim();
                DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("questionnaires");
                query = questionnaires.orderByChild("title").equalTo(text);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                questionnaire = ds.getValue(Questionnaire.class);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(MainActivityTeacher.this, EditQuestionnaireActivity.class);
                intent = intent.putExtra("questionnaire",questionnaire);
                MainActivityTeacher.this.finish();
                startActivity(intent);
            }
        });

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivityTeacher.this, CreateQuestionnaireActivity.class);
                MainActivityTeacher.this.finish();
                startActivity(intent);
            }
        };

        Button buttonLogin = (Button) findViewById(R.id.create_questionnaire_button);
        buttonLogin.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivityTeacher.this, ProfileTeacherActivity.class);
                MainActivityTeacher.this.finish();
                startActivity(intent);
            }
        };

        Button buttonProfile = (Button) findViewById(R.id.profilemainteacher);
        buttonProfile.setOnClickListener(onClickListener2);

    }
}
