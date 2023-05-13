package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class ProfileTeacherActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        TextView name = findViewById(R.id.profileName);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        name.setText(user.getEmail());
        TextView type = findViewById(R.id.profileType);
        type.setText("Profesor");


        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfileTeacherActivity.this, TeamsTeacherActivity.class);
                ProfileTeacherActivity.this.finish();
                startActivity(intent);
            }
        };

        Button buttonTeams = (Button) findViewById(R.id.check_teams);
        buttonTeams.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfileTeacherActivity.this, LoginActivity.class);
                ProfileTeacherActivity.this.finish();
                startActivity(intent);
            }
        };

        Button buttonExit = (Button) findViewById(R.id.close_session);
        buttonExit.setOnClickListener(onClickListener2);

        View.OnClickListener onClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfileTeacherActivity.this, MainActivityStudent.class);
                ProfileTeacherActivity.this.finish();
                startActivity(intent);
            }
        };

        Button buttonBack = (Button) findViewById(R.id.settingsGoBack);
        buttonBack.setOnClickListener(onClickListener3);

    }
}
