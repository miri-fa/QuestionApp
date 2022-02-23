package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.question2.Model.Student;
import com.example.question2.Model.Teacher;
import com.example.question2.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private Spinner spinnerRole;
    private EditText textEmail, textPassword, textConfirmPassword;
    private String registerRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        textEmail = findViewById(R.id.editTextEmailAddress);
        textPassword = findViewById(R.id.newPassword);
        textConfirmPassword = findViewById(R.id.newConfirmTextPassword);

        spinnerRole = findViewById(R.id.optionsRole);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
        spinnerRole.setOnItemSelectedListener(this);



        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        };

        Button buttonLogin = (Button) findViewById(R.id.registerButton2);
        buttonLogin.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.finish();
                startActivity(intent);
            }
        };

        Button button = (Button) findViewById(R.id.registerGoBack);
        button.setOnClickListener(onClickListener2);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        this.registerRole = (String) parent.getItemAtPosition(pos);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void registerUser(){

        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();
        String confirmPassword = textConfirmPassword.getText().toString().trim();

        StringEmptyAndError(password,textPassword);
        StringEmptyAndError(email,textEmail);
        StringEmptyAndError(confirmPassword,textConfirmPassword);

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmail.setError("formato incorrecto");
            textEmail.requestFocus();
        }

        if(password.length() < 6){
            textPassword.setError("La contraseña debe tener al menos 6 caracteres");
            textPassword.requestFocus();
        }

       if(password!=confirmPassword){
            textConfirmPassword.setError("La contraseña no coincide");
            textConfirmPassword.requestFocus();
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user;
                            if (registerRole.equals("Profesor")){
                                user = new Teacher(email,password,registerRole);
                            } else {
                                user = new Student(email,password,registerRole);
                                
                            }

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "El usuario ha sido registrado con éxito",Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        //Not sure if it really makes sense
                                        if(user.isEmailVerified()){
                                            startActivity(new Intent(RegisterActivity.this, MainActivityTeacher.class));
                                        }
                                        else{
                                            user.sendEmailVerification();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                        }

                                    }{

                                    }
                                }
                            });
                        }else{
                        }
                    }
                });

    }

    private void StringEmptyAndError(String text, EditText textView){
        if(text.isEmpty()){
            textView.setError("Campo requerido");
            textView.requestFocus();
        }
    }

}