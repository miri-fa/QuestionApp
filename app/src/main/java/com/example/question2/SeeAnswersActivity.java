package com.example.question2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SeeAnswersActivity extends AppCompatActivity implements
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
            buttonNextQuestion, buttonBeforeQuestion,buttonDeleteQuestionnaire;
    private int count;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        answersList = new ArrayList<>();

        String role = getIntent().getStringExtra("role");
        String codeInput = getIntent().getStringExtra("title");

        if(role.equals("1")){
            setContentView(R.layout.activity_questionnaire_student_make);
            TextView title = findViewById(R.id.title1);
            title.setText("Ver Resultados");
            Button backMain = (Button) findViewById(R.id.backmain);
            backMain.setVisibility(View.GONE);
        }else{
            setContentView(R.layout.activity_see_results);
            View.OnClickListener onClickListener1 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(SeeAnswersActivity.this, EditQuestionnaireActivity.class);
                    intent = intent.putExtra("title",codeInput);
                    SeeAnswersActivity.this.finish();
                    startActivity(intent);
                }
            };

            Button editButton = (Button) findViewById(R.id.edit_questionnaire);

            editButton.setOnClickListener(onClickListener1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
            View.OnClickListener onClickListener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HSSFWorkbook wb = new HSSFWorkbook();
                    int cell = 0;
                    HSSFSheet sheet = wb.createSheet();

                    HSSFRow rowExcel;
                    int row = 0;
                    rowExcel = sheet.createRow(row);
                    int index = 0;
                    for (Question q: questionnaire.getQuestions()) {
                        row = 0;
                        sheet.getRow(row).createCell(cell).setCellValue(q.getTitle());

                        if (q.getType()==1){
                            OpenAnswerQuestion questionAnswers = (OpenAnswerQuestion) q;
                            for(String answer : questionAnswers.getAnswers()){
                                row++;
                                if (index==0){
                                    sheet.createRow(row);
                                }
                                sheet.getRow(row).createCell(cell).setCellValue(answer);
                            }

                        }else if (q.getType()==2){
                            ChoicesQuestion questionAnswers = (ChoicesQuestion) q;
                            //Aquí hay que convertir los números a las respuestas como string

                            for(String answer : questionAnswers.getAnswers()){
                                row++;

                                String[] parts = answer.split(" ");
                                String phrase = "";
                                for (int i = 0; i< parts.length; i++) {
                                    if (i != parts.length - 1) {
                                        phrase += questionAnswers.getChoice(Integer.parseInt(parts[i])) + "//";
                                    } else {
                                        phrase += questionAnswers.getChoice(Integer.parseInt(parts[i]));
                                    }
                                }
                                if (index==0){
                                    sheet.createRow(row);
                                }
                                sheet.getRow(row).createCell(cell).setCellValue(phrase);
                            }

                        }else{
                            ScoreQuestion questionAnswers = (ScoreQuestion) q;
                            for(String answer : questionAnswers.getAnswers()){
                                row++;
                                if (index==0){
                                    sheet.createRow(row);
                                }
                                sheet.getRow(row).createCell(cell).setCellValue(answer);
                            }

                        }
                        cell++;
                        index++;
                    }
                    //Get path of the downloads phone directory
                    File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+questionnaire.getTitle()+".xlsx");
                    System.out.println("PATH: "+filePath);
                    try {
                    if (!filePath.exists()){

                            filePath.createNewFile();
                    }

                        FileOutputStream fileOS = new FileOutputStream(filePath);
                    wb.write(fileOS);
                    if(fileOS!=null){
                        fileOS.flush();
                        fileOS.close();
                        Toast.makeText(SeeAnswersActivity.this,
                                "El archivo .xlsx se ha guardado en la carpeta de download con éxito", Toast.LENGTH_SHORT).show();
                    }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            Button excelButton = (Button) findViewById(R.id.excel);
            excelButton.setOnClickListener(onClickListener2);
        }

        //Initialize everything
        answersList = new ArrayList<>();
        ratingQuestion = new ScoreQuestion();
        openAnswerQuestion = new OpenAnswerQuestion();
        questionnaire = new Questionnaire();


        questions = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.commit();

        //Search the correct questionnaire in database
        DatabaseReference questionnaires = FirebaseDatabase.getInstance().getReference("questionnaires");
        Query query = questionnaires.orderByChild("title").equalTo(codeInput);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        code = ds.child("code").getValue(String.class);
                        questionnaireID = ds.getKey();
                        for (DataSnapshot data : ds.child("questions").getChildren()){
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
                        questionnaire.setTitle(ds.child("title").getValue(String.class));
                        count = 1;
                        questionNumber = findViewById(R.id.numberMakeCount);
                        fillQuestion(questionnaire.getQuestionFromPosition(0),0);
                        if(!role.equals("1")) {
                            TextView codeView = findViewById(R.id.code);
                            codeView.setText(code);
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Setting the views
        buttonFinish = (Button) findViewById(R.id.create_questionnaire);
        buttonCreateQuestion = (Button) findViewById(R.id.questionnaire_add_question);
        buttonDeleteQuestion = (Button) findViewById(R.id.questionnaire_delete_question);
        buttonNextQuestion = (Button) findViewById(R.id.questionnaire_advance_create);
        buttonBeforeQuestion = (Button) findViewById(R.id.questionnaire_back_create);
        if (!role.equals("1")){
            buttonDeleteQuestionnaire = (Button) findViewById(R.id.delete_questionnaire);
            Context context = this;
            View.OnClickListener onClickListener9 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Quieres eliminar este cuestionario?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference teams = FirebaseDatabase.getInstance().getReference("questionnaires");
                            Query query = teams.orderByChild("title").equalTo(codeInput);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds:snapshot.getChildren()) {
                                        FirebaseDatabase.getInstance().getReference("questionnaires/"+ds.getKey()).removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            startActivity(new Intent(SeeAnswersActivity.this, MainActivityTeacher.class));
                        }

                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            };

            buttonDeleteQuestionnaire.setOnClickListener(onClickListener9);

        }

        //Get help
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

        //Go to main page
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeeAnswersActivity.this, MainActivityTeacher.class));

            }
        };

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
        View.OnClickListener onClickListener7 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!role.equals("1")) {
                    Intent intent = new Intent(SeeAnswersActivity.this, MainActivityTeacher.class);
                    SeeAnswersActivity.this.finish();
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(SeeAnswersActivity.this, MainActivityStudent.class);
                    SeeAnswersActivity.this.finish();
                    startActivity(intent);
                }

            }
        };

        buttonFinish.setOnClickListener(onClickListener7);

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
            AnswersChoicesFragment answersChoicesFragment = new AnswersChoicesFragment();
            Bundle args = new Bundle();
            args.putString("title", q.getTitle());
            args.putInt("position", count-1);
            args.putStringArrayList("questions",q.getChoices());
            args.putStringArrayList("answers",q.getAnswers());
            answersChoicesFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragment_container_student, answersChoicesFragment).commit();

        } else if (question instanceof OpenAnswerQuestion){
            OpenAnswerQuestion q = (OpenAnswerQuestion) question;
            AnswersOpenChoiceFragment answersOpenChoiceFragment = new AnswersOpenChoiceFragment();
            Bundle args = new Bundle();
            args.putInt("position", count-1);
            args.putString("title", q.getTitle());
            args.putStringArrayList("answers",q.getAnswers());
            answersOpenChoiceFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragment_container_student, answersOpenChoiceFragment).commit();

        }else if (question instanceof ScoreQuestion){
            ScoreQuestion q = (ScoreQuestion) question;
            AnswersRatingFragment answersRatingFragment = new AnswersRatingFragment();
            Bundle args = new Bundle();
            args.putInt("position", count-1);
            args.putString("title", q.getTitle());
            args.putString("left", q.getLowerSide());
            args.putString("right", q.getHigherSide());
            args.putStringArrayList("answers",q.getAnswers());
            answersRatingFragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragment_container_student, answersRatingFragment).commit();
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
