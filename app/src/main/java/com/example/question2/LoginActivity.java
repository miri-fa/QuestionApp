package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{
    private EditText emailView, passView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        emailView = (EditText) findViewById(R.id.loginEmailAddress);
        passView = (EditText) findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
            }
        };

        Button buttonForgottenPassword = (Button) findViewById(R.id.forgottenpasswordbutton);
        buttonForgottenPassword.setOnClickListener(onClickListener);

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        };

        Button buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonLogin.setOnClickListener(onClickListener1);

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
            }
        };

        Button button = (Button) findViewById(R.id.registerButton);
        button.setOnClickListener(onClickListener2);

    }

    private void userLogin() {
        String email = emailView.getText().toString().trim();
        String pass = passView.getText().toString().trim();

        if(email.isEmpty()){
            emailView.setError("Introduce un email válido");
            emailView.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailView.setError("Introduce un email válido");
            emailView.requestFocus();
        }

        if(pass.isEmpty()){
            passView.setError("Se necesita una contraseña");
            passView.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivityTeacher.class));
                }else{
                    Toast.makeText(LoginActivity.this, "Los datos introducidos no son correctos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
