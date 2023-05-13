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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.question2.Model.User;
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
import java.util.ArrayList;
import java.util.Random;

public class MakeQuestionnaireActivityStudent extends AppCompatActivity implements
        CompleteOpenQuestionFragment.FragmentCompleteOpenQuestionListener,
        CompleteRatingQuestionFragment.FragmentCompleteRatingQuestionListener,
        CompleteMultipleChoiceFragment.FragmentChooseMultipleChoiceListener,
        CompleteOneChoiceFragment.FragmentChooseOneChoiceListener{
    private static FragmentManager fragmentManager;
    private ArrayList<Question> questions;
    private String questionnaireID;
    private OpenAnswerQuestion openAnswerQuestion;
    private ScoreQuestion ratingQuestion;
    private ChoicesQuestion choicesQuestion;
    private ArrayList<String> answersList;
    private String userID;
    private DatabaseReference reference;
    private String answerOpenQuestion;
    private String answerRatingQuestion;
    private String code;
    private String answerOneChoiceQuestion, answerMultipleChoiceQuestion;
    private int pos;
    private Questionnaire questionnaire;
    private boolean repeated;
    private FragmentTransaction fragmentTransaction;
    private FirebaseUser user;
    private TextView questionNumber;
    private Button buttonFinish, buttonMainPage, buttonCreateQuestion, buttonDeleteQuestion,
            buttonNextQuestion, buttonBeforeQuestion;
    private int count;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_questionnaire_student_make);

        //Get questionnaire from database
        answersList = new ArrayList<>();
        ratingQuestion = new ScoreQuestion();
        openAnswerQuestion = new OpenAnswerQuestion();
        questionnaire = new Questionnaire();
        String codeInput = getIntent().getStringExtra("code");
        questions = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.commit();
        DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("questionnaires");
        Query query = questionnaires.orderByChild("code").equalTo(codeInput);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        code = ds.child("code").getValue(String.class);
                        questionnaireID = ds.getKey();
                        for (DataSnapshot data : ds.child("questions").getChildren()){
                            System.out.println("Valor de data: "+data.getValue());
                            Question q = data.getValue(Question.class);
                            if (q.getType()==1){
                                questions.add(data.getValue(OpenAnswerQuestion.class));
                            }else if (q.getType()==2){
                                questions.add(data.getValue(ChoicesQuestion.class));
                            }else if (q.getType()==3){
                                questions.add(data.getValue(ScoreQuestion.class));
                            }
                        }
                        questionnaire.setQuestions(questions);
                        count = 1;
                        questionNumber = findViewById(R.id.numberMakeCount);
                        fillQuestion(questionnaire.getQuestionFromPosition(0),0);
                    }
                }

                answersList = new ArrayList<>();
                for (Question q: questionnaire.getQuestions()){
                    answersList.add("");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        buttonFinish = (Button) findViewById(R.id.create_questionnaire);
        buttonMainPage = (Button) findViewById(R.id.create_to_see_button);
        buttonCreateQuestion = (Button) findViewById(R.id.questionnaire_add_question);
        buttonDeleteQuestion = (Button) findViewById(R.id.questionnaire_delete_question);
        buttonNextQuestion = (Button) findViewById(R.id.questionnaire_advance_create);
        buttonBeforeQuestion = (Button) findViewById(R.id.questionnaire_back_create);

        //Finish making the questionnaire
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuestionnaire();
                for (int i=0; i<questionnaire.getQuestions().size(); i++) {
                    //Add if this instance is this type of question or other to save
                    if(questionnaire.getQuestions().get(i).getType() == 1){
                        OpenAnswerQuestion op = (OpenAnswerQuestion) questionnaire.getQuestions().get(i);
                        op.addAnswers(answersList.get(i));
                        questionnaire.getQuestions().set(i,op);
                        OpenAnswerQuestion quest = (OpenAnswerQuestion) questionnaire.getQuestions().get(i);
                        FirebaseDatabase.getInstance().getReference("questionnaires/" + questionnaireID+
                                "/questions/"+i+"/answers").setValue(quest.getAnswers())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });
                    } else if(questionnaire.getQuestions().get(i).getType() == 2){
                        ChoicesQuestion op = (ChoicesQuestion) questionnaire.getQuestions().get(i);
                        op.addAnswers(answersList.get(i));
                        questionnaire.getQuestions().set(i,op);
                        ChoicesQuestion quest = (ChoicesQuestion) questionnaire.getQuestions().get(i);
                        FirebaseDatabase.getInstance().getReference("questionnaires/" + questionnaireID+
                                "/questions/"+i+"/answers").setValue(quest.getAnswers())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });
                    } else if(questionnaire.getQuestions().get(i).getType() == 3){
                        ScoreQuestion op = (ScoreQuestion) questionnaire.getQuestions().get(i);
                        op.addAnswers(answersList.get(i));
                        questionnaire.getQuestions().set(i,op);
                        ScoreQuestion quest = (ScoreQuestion) questionnaire.getQuestions().get(i);
                        FirebaseDatabase.getInstance().getReference("questionnaires/" + questionnaireID+
                                "/questions/"+i+"/answers").setValue(quest.getAnswers())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });
                    }
                }
                Toast.makeText(getApplicationContext(), "The questionnaire is saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MakeQuestionnaireActivityStudent.this, MainActivityTeacher.class));
            }
        };
        buttonFinish.setOnClickListener(onClickListener1);

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
                message.setText(help.getContent(3));

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

        //Go to main page
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MakeQuestionnaireActivityStudent.this, MainActivityTeacher.class));

            }
        };

        buttonMainPage.setOnClickListener(onClickListener2);

        //Go forward on questionnaire
        View.OnClickListener onClickListener5 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnswer(false);
                if (count < questionnaire.size()){
                    Question question = questionnaire.getQuestionFromPosition(count);
                    count ++;
                    String num = Integer.toString(count);
                    questionNumber.setText(num);
                    fillQuestion(question,count);
                }
            }
        };

        buttonNextQuestion.setOnClickListener(onClickListener5);

        //Go back on questionnaire
        View.OnClickListener onClickListener6 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1){
                    Question question = questionnaire.getQuestionFromPosition(count-2);
                    addAnswer(false);
                    count --;
                    String num = Integer.toString(count);
                    questionNumber.setText(num);
                    fillQuestion(question,count-2);
                }
            }
        };

        buttonBeforeQuestion.setOnClickListener(onClickListener6);

    }

    //Set up everything to save the questionnaire into database
   private Questionnaire createQuestionnaire(){

        questionnaire.setTitle(getIntent().getStringExtra("title"));
        addAnswer(false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();
        questionnaire.setAuthor(user.getEmail());
        questionnaire.setPublished(true);

        return questionnaire;
    }


    //This method adds answer into the question it belongs to, for later save into the database
     private void addAnswer(Boolean inserted){
        if (answerOpenQuestion != null){
            answersList.set(pos,answerOpenQuestion);
            answerOpenQuestion = null;
        }if (answerRatingQuestion != null){
             answersList.set(pos,answerRatingQuestion);
             answerRatingQuestion = null;
        }if (answerOneChoiceQuestion!=null){
             answersList.set(pos,answerOneChoiceQuestion);
             answerOneChoiceQuestion=null;
        }if (answerMultipleChoiceQuestion!=null){
            answersList.set(pos,answerMultipleChoiceQuestion);
            answerMultipleChoiceQuestion = null;
        }
     }

    private void fillQuestion(Question question, int position){
        if (question instanceof ChoicesQuestion){
            ChoicesQuestion q = (ChoicesQuestion) question;
            if (q.isMultipleChoice()){
                CompleteMultipleChoiceFragment multipleChoiceFragment = new CompleteMultipleChoiceFragment();
                Bundle args = new Bundle();
                args.putString("title", q.getTitle());
                args.putInt("position", count-1);
                args.putStringArrayList("questions",q.getChoices());
                if (answersList.size()!=0){
                    args.putString("answer",answersList.get(count-1));
                }
                multipleChoiceFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.fragment_container_student, multipleChoiceFragment).commit();
            } else {
                CompleteOneChoiceFragment oneChoiceFragment = new CompleteOneChoiceFragment();
                Bundle args = new Bundle();
                args.putString("title", q.getTitle());
                args.putInt("position", count-1);
                if (answersList.size()!=0){
                    args.putString("answer",answersList.get(count-1));
                }
                args.putStringArrayList("questions",q.getChoices());
                oneChoiceFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.fragment_container_student, oneChoiceFragment).commit();
            }

        } else if (question instanceof OpenAnswerQuestion){
            OpenAnswerQuestion q = (OpenAnswerQuestion) question;
            CompleteOpenQuestionFragment openQuestionFragment = new CompleteOpenQuestionFragment();
            Bundle args = new Bundle();
            args.putInt("position", count-1);
            args.putString("title", q.getTitle());
            if (answersList.size()!=0){
                args.putString("answer",answersList.get(count-1));
            }
            openQuestionFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragment_container_student, openQuestionFragment).commit();

        }else if (question instanceof ScoreQuestion){
            ScoreQuestion q = (ScoreQuestion) question;
            CompleteRatingQuestionFragment ratingQuestionFragment = new CompleteRatingQuestionFragment();
            Bundle args = new Bundle();
            args.putInt("position", count-1);
            args.putString("title", q.getTitle());
            args.putString("left", q.getLowerSide());
            args.putString("right", q.getHigherSide());
            if (answersList.size()!=0){
                args.putString("answer",answersList.get(count-1));
            }
            ratingQuestionFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragment_container_student, ratingQuestionFragment).commit();
        }
    }

    @Override
    public void onInputOpenQuestionSent(String openQuestionAnswer, int position) {
        this.answerOpenQuestion = openQuestionAnswer;
        this.pos = position;
    }

    @Override
    public void onInputRatingQuestionSent(float scoreQuestionAnswer, int position) {
        this.answerRatingQuestion = Float.toString(scoreQuestionAnswer);
        this.pos = position;
    }

    @Override
    public void onInputMultipleChoiceSent(ArrayList<String> answers, int position) {
        answerMultipleChoiceQuestion = "";

        for (int i=0; i<answers.size(); i++){
            if (i<answers.size()-1) {
                answerMultipleChoiceQuestion += answers.get(i) + " ";
            }else{
                answerMultipleChoiceQuestion += answers.get(i);
            }
        }
        this.pos = position;
    }

    @Override
    public void onInputOneChoiceSent(String answers, int position) {
        this.answerOneChoiceQuestion = answers;
        this.pos = position;
    }
}
