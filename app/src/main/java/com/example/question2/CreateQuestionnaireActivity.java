package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
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
                popupWindow.showAtLocation(findViewById(R.id.frameLayout2), Gravity.CENTER, 0, 0);
                TextView message = popupWindow.getContentView().findViewById(R.id.helpText);
                HelpManager help = new HelpManager();
                message.setText(help.getContent(5));

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
        Button buttonHelp = (Button) findViewById(R.id.helpmain);
        buttonHelp.setOnClickListener(onClickListenerHelp);
        //If create questionnaire button is clicked, call make questionnaire with the title chosen
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

        View.OnClickListener onClickListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivityTeacher.class);
                CreateQuestionnaireActivity.this.finish();
                startActivity(intent);
            }
        };

        Button buttonBack = (Button) findViewById(R.id.backmain);
        buttonBack.setOnClickListener(onClickListener4);


    }


}



