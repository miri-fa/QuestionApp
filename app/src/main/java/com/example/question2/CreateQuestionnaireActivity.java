package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.question2.Model.Questionnaire;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateQuestionnaireActivity extends AppCompatActivity{
    private FirebaseDatabase database;
    private EditText title;
    private String openQuestTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_questionnaire_start_teacher);

        database = FirebaseDatabase.getInstance();
        title = findViewById(R.id.questionnaire_name_input);

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MakeQuestionnaireActivityTeacher.class);
                            intent = intent.putExtra("title",title.getText().toString().trim());
                            startActivity(intent);
                        }
        };

        Button buttonLogin = (Button) findViewById(R.id.create_questionnaire);
        buttonLogin.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivityTeacher.class);
                CreateQuestionnaireActivity.this.finish();
                startActivity(intent);
            }
        };

        Button buttonGoBackMain = (Button) findViewById(R.id.goMainFromQuestionnaireTeacher);
        buttonGoBackMain.setOnClickListener(onClickListener2);


    }


}



