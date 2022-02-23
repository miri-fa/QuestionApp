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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailView;
    private Button resetPasswordButton, goToLogin;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password);

        emailView = findViewById(R.id.sendEmailText);
        resetPasswordButton = findViewById(R.id.changePasswordButton);
        goToLogin = findViewById(R.id.forgotGoBack);
        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(v -> resetPassword());

        goToLogin.setOnClickListener(v -> startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class)));

    }

    private void resetPassword(){
        String email = emailView.getText().toString().trim();

        if(email.isEmpty()){
            emailView.setError("Introduce un email válido");
            emailView.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailView.setError("Introduce un email válido");
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Toast.makeText(ForgotPasswordActivity.this, "Revisa tu correo para resetear tu contraseña", Toast.LENGTH_LONG).show();
                resetPasswordButton.setVisibility(View.INVISIBLE);
                goToLogin.setVisibility(View.VISIBLE);

            }else{

            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){

            case R.id.forgotGoBack:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                break;
        }

    }



}