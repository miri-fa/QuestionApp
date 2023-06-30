package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainer;
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

public class MakeQuestionnaireActivityTeacher extends AppCompatActivity implements OpenQuestionFragment.FragmentOpenQuestionListener, RatingQuestionFragment.FragmentRatingQuestionListener,
OneChoiceFragment.FragmentOneChoiceListener, MultipleChoiceFragment.FragmentMultipleChoiceListener{
    private static FragmentManager fragmentManager;
    private ArrayList<Question> questions;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private String code;
    private OpenAnswerQuestion openAnswerQuestion;
    private ScoreQuestion ratingQuestion;
    private ChoicesQuestion choicesQuestion;
    private ChooseFragment chooseFragment;
    private Questionnaire questionnaire;
    private boolean repeated;
    private FragmentTransaction fragmentTransaction;
    private TextView questionNumber;
    private Button buttonFinish, buttonCreateQuestion, buttonDeleteQuestion,
            buttonNextQuestion, buttonBeforeQuestion;
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
        code="";
        questionNumber = findViewById(R.id.numberMakeCount);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction =fragmentManager.beginTransaction();
        chooseFragment = new ChooseFragment();
        fragmentTransaction.add(R.id.fragment_container,chooseFragment,null);
        fragmentTransaction.commit();

        buttonFinish = (Button) findViewById(R.id.create_questionnaire);
        buttonCreateQuestion = (Button) findViewById(R.id.questionnaire_add_question);
        buttonDeleteQuestion = (Button) findViewById(R.id.questionnaire_delete_question);
        buttonNextQuestion = (Button) findViewById(R.id.questionnaire_advance_create);
        buttonBeforeQuestion = (Button) findViewById(R.id.questionnaire_back_create);

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuestionnaire();
                if (questionnaire.getQuestions().size()!=0){FirebaseDatabase.getInstance().getReference("questionnaires").push()
                        .setValue(questionnaire).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            setContentView(R.layout.activity_finish_making_questionnaire);
                            GenerateCodeFragment generateCodeFragment = new GenerateCodeFragment();
                            Bundle args = new Bundle();


                            args.putString("code", code);
                            generateCodeFragment.setArguments(args);
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container,generateCodeFragment);
                            fragmentTransaction.commit();
                        }
                    }
                });
            } else {
                    Toast.makeText(MakeQuestionnaireActivityTeacher.this, "Crea alguna pregunta antes", Toast.LENGTH_LONG).show();
                }
            }
        };
        buttonFinish.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MakeQuestionnaireActivityTeacher.this, MainActivityTeacher.class));

            }
        };

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
                message.setText(help.getContent(6));

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


        View.OnClickListener onClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion(false);
                count++;
                String num = Integer.toString(count);
                questionNumber.setText(num);
                chooseFragment.restartSpinner();
                Question q = new Question();
                if (!(count - 1 == questionnaire.size())){
                    questionnaire.insertQuestion(count - 1, q);
                }
            }
        };

        buttonCreateQuestion.setOnClickListener(onClickListener3);

        View.OnClickListener onClickListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion(false);
                if(count == questionnaire.size()) {
                    String num = Integer.toString(count);
                    questionNumber.setText(num);
                    chooseFragment.restartSpinner();
                    questionnaire.deleteQuestion(count-1);
                }else{
                    Question question = questionnaire.getQuestionFromPosition(count);
                    String num = Integer.toString(count);
                    questionNumber.setText(num);
                    fillQuestion(question);
                    questionnaire.deleteQuestion(count-1);
                }
            }


        };

        buttonDeleteQuestion.setOnClickListener(onClickListener4);

        View.OnClickListener onClickListener5 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion(false);
                if (count < questionnaire.size()){
                    Question question = questionnaire.getQuestionFromPosition(count);
                    count ++;
                    String num = Integer.toString(count);
                    questionNumber.setText(num);
                    fillQuestion(question);
                }
            }
        };

        buttonNextQuestion.setOnClickListener(onClickListener5);

        View.OnClickListener onClickListener6 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1){
                    Question question = questionnaire.getQuestionFromPosition(count-2);
                    addQuestion(false);
                    count --;
                    String num = Integer.toString(count);
                    questionNumber.setText(num);
                    fillQuestion(question);
                }
            }
        };

        buttonBeforeQuestion.setOnClickListener(onClickListener6);

    }

//add all questions to questionnaire and add to database
    private Questionnaire createQuestionnaire(){
        repeated = true;
        while (repeated){
            code = generateCode();
            repeated = checkCode(code);
        }
        questionnaire.setCode(code);
        questionnaire.setTitle(getIntent().getStringExtra("title"));
        addQuestion(false);
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

    //This method adds question into the questionnaire class, for later save into the database
     private void addQuestion(Boolean insertced){
        Question q = new Question();
        q=null;
        if (openAnswerQuestion!=null){
            q = openAnswerQuestion;
            q.setType(1);
            openAnswerQuestion = null;
        }if (ratingQuestion != null){
            q = ratingQuestion;
            q.setType(3);
            ratingQuestion = null;
        } if (choicesQuestion!=null){
            q = choicesQuestion;
            q.setType(2);
            choicesQuestion = null;
         }

        if (q!=null) {
            if ((count - 1 == questionnaire.size())) {
                questionnaire.addQuestion(q);
            } else {
                questionnaire.setQuestion(count - 1, q);
            }
        }
    }
    //Fill the question with what was written before
    private void fillQuestion(Question question){
        if (question instanceof ChoicesQuestion){
            ChoicesQuestion q = (ChoicesQuestion) question;
            if (q.isMultipleChoice()){
                chooseFragment.setMultipleChoiceQuestion(q.getTitle(),q.getChoices());
            } else {
                chooseFragment.setOneChoiceQuestion(q.getTitle(),q.getChoices());
            }

        } else if (question instanceof OpenAnswerQuestion){
            OpenAnswerQuestion q = (OpenAnswerQuestion) question;
            chooseFragment.setOpenAnswerQuestion(q.getTitle());

        }else if (question instanceof ScoreQuestion){
            ScoreQuestion q = (ScoreQuestion) question;
            chooseFragment.setScoreQuestion(q.getTitle(),q.getLowerSide(),q.getHigherSide());
        }
    }

    @Override
    public void onInputOneChoiceSent(ChoicesQuestion choicesQuestion) {
        this.choicesQuestion = choicesQuestion;
    }

    @Override
    public void onInputMultipleChoiceSent(ChoicesQuestion choicesQuestion) {
        this.choicesQuestion = choicesQuestion;
    }
//Generate code
    private String generateCode(){
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
//Check if it exists
    private boolean checkCode(String code){
        repeated = false;
        DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("questionnaires");
        questionnaires.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Questionnaire q = ds.getValue(Questionnaire.class);
                        if ((q.getCode()!=null)&&(q.getCode().equals(code))){
                            repeated = true;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return repeated;
    }
}
