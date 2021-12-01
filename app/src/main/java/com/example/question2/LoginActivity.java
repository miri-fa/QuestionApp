package com.example.question2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.login);

        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
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

}
