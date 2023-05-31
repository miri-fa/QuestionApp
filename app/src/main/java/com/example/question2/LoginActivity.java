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

import com.example.question2.Model.Questionnaire;
import com.example.question2.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity{
    private EditText emailView, passView;
    private FirebaseAuth mAuth;
    private String email, pass, role;

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
        email = emailView.getText().toString().trim();
        pass = passView.getText().toString().trim();

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
        else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
                        Query query = users.orderByChild("email").equalTo(email);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        User u = childSnapshot.getValue(User.class);
                                        role = u.getRole();
                                    }
                                    if (role.equals("Profesor")) {
                                        startActivity(new Intent(LoginActivity.this, MainActivityTeacher.class));
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, MainActivityStudent.class));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Los datos introducidos no son correctos", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
