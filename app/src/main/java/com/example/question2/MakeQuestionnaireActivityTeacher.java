package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.question2.Model.ChoicesQuestion;
import com.example.question2.Model.OpenAnswerQuestion;
import com.example.question2.Model.Question;
import com.example.question2.Model.Questionnaire;
import com.example.question2.Model.ScoreQuestion;
import com.example.question2.Model.Teacher;
import com.example.question2.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MakeQuestionnaireActivityTeacher extends AppCompatActivity implements OpenQuestionFragment.FragmentOpenQuestionListener, RatingQuestionFragment.FragmentRatingQuestionListener,
OneChoiceFragment.FragmentOneChoiceListener, MultipleChoiceFragment.FragmentMultipleChoiceListener{
    private static FragmentManager fragmentManager;
    private ArrayList<Question> questions;
    private String openQuestTitle;
    private User userProfile;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private OpenAnswerQuestion openAnswerQuestion;
    private ScoreQuestion ratingQuestion;
    private ChoicesQuestion choicesQuestion;
    private ChooseFragment chooseFragment;
    private Questionnaire questionnaire;
    private TextView questionNumber;
    private int count;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_questionnaire_teacher_make);
        questions = new ArrayList<Question>();
        questionnaire = new Questionnaire();
        count = 1;
        questionNumber = findViewById(R.id.numberMakeCount);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        chooseFragment = new ChooseFragment();
        fragmentTransaction.add(R.id.fragment_container,chooseFragment,null);
        fragmentTransaction.commit();
         

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuestionnaire();
                FirebaseDatabase.getInstance().getReference("questionnaires").push()
                        .setValue(questionnaire).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(MakeQuestionnaireActivityTeacher.this, "El cuestionario ha sido creado con éxito", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                startActivity(new Intent(MakeQuestionnaireActivityTeacher.this,CreateQuestionnaireActivity.class));
            }
        };

        Button buttonLogin = (Button) findViewById(R.id.create_questionnaire);
        buttonLogin.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MakeQuestionnaireActivityTeacher.this, MainActivityTeacher.class));

            }
        };

        Button buttonMainPage = (Button) findViewById(R.id.create_to_see_button);
        buttonMainPage.setOnClickListener(onClickListener2);

        View.OnClickListener onClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
                count ++;
                String num = Integer.toString(count);
                questionNumber.setText(num);
                chooseFragment.restartSpinner();
            }
        };

        Button buttonCreateQuestion = (Button) findViewById(R.id.questionnaire_add_question);
        buttonCreateQuestion.setOnClickListener(onClickListener3);

        View.OnClickListener onClickListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQuestion();
                String num = Integer.toString(count);
                questionNumber.setText(num);
                chooseFragment.restartSpinner();
            }
        };

        Button buttonDeleteQuestion = (Button) findViewById(R.id.questionnaire_delete_question);
        buttonDeleteQuestion.setOnClickListener(onClickListener4);

        View.OnClickListener onClickListener5 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionnaire.isAtEndOfArray(count)){
                    Question question = questionnaire.getQuestionFromPosition(count);
                    count ++;
                    fillQuestion(question);
                }
            }
        };

        Button buttonNextQuestion = (Button) findViewById(R.id.questionnaire_advance_create);
        buttonNextQuestion.setOnClickListener(onClickListener5);

    }


    private Questionnaire createQuestionnaire(){
        questionnaire.setTitle(getIntent().getStringExtra("title"));
        addQuestion();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();
        questionnaire.setAuthor(user.getEmail());
        questionnaire.setPublished(true);

        return questionnaire;
    }

    @Override
    public void onInputOpenQuestionSent(OpenAnswerQuestion openAnswerQuestion) {
        this.openAnswerQuestion = openAnswerQuestion;
    }

    @Override
    public void onInputRatingQuestionSent(ScoreQuestion scoreQuestion) {
        this.ratingQuestion = scoreQuestion;
    }

     private void addQuestion(){
        if (openAnswerQuestion!=null){
            questionnaire.addQuestion(openAnswerQuestion);
            openAnswerQuestion = null;
        }if (ratingQuestion != null){
            questionnaire.addQuestion(ratingQuestion);
            ratingQuestion = null;
        } if (choicesQuestion!=null){
            questionnaire.addQuestion(choicesQuestion);
         }
    }

    private void deleteQuestion(){
        if (openAnswerQuestion!=null){
            questionnaire.deleteQuestion(openAnswerQuestion);
            openAnswerQuestion = null;
        }if (ratingQuestion != null){
            questionnaire.deleteQuestion(ratingQuestion);
            ratingQuestion = null;
        } if (choicesQuestion!=null){
            questionnaire.deleteQuestion(choicesQuestion);
        }
    }

    private void fillQuestion(Question question){
        int pos = 0;
        if (question instanceof ChoicesQuestion){
            ChoicesQuestion q = (ChoicesQuestion) question;
            if (q.isMultipleChoice()){
                pos = 3;
            } else {
                pos = 2;
            }
            //Find a way to set title and questions to be seen and previewed

        } else if (question instanceof OpenAnswerQuestion){
            pos = 1;
            //Set title

        }else if (question instanceof ScoreQuestion){
            pos = 4;
            //Set title and left and right options maybe an interface would work????
        }
        chooseFragment.setSpinner(pos);
    }

    @Override
    public void onInputOneChoiceSent(ChoicesQuestion choicesQuestion) {
        this.choicesQuestion = choicesQuestion;
    }

    @Override
    public void onInputMultipleChoiceSent(ChoicesQuestion choicesQuestion) {
        this.choicesQuestion = choicesQuestion;
    }
}
