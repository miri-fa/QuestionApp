package com.example.question2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class FullscreenActivity extends AppCompatActivity implements View.OnTouchListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);
        Button start = findViewById(R.id.start);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(FullscreenActivity.this, LoginActivity.class);
                FullscreenActivity.this.finish();
                startActivity(intent);
            }
        };

        start.setOnClickListener(onClickListener1);

    }
    public boolean onTouch(View view, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            Intent intent =new Intent(FullscreenActivity.this, LoginActivity.class);
            FullscreenActivity.this.finish();
            startActivity(intent);
        }
        return true;
    }

}