package com.example.question2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivityStudent extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private Spinner spinnerRole;
    private EditText textEmail, textPassword, textConfirmPassword;
    private String registerRole;
    boolean darkMode;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private Spinner fontSizeSpinner;
    private String selectedFontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Start activity
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);

        // font spinner from view
        fontSizeSpinner = findViewById(R.id.fontSizeSpinner);

        // Listener for font size button
        fontSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get size selected
                selectedFontSize = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing selected
            }


        });
        //Listener for the apply button
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call applyFontSize
                applyFontSize(selectedFontSize);
            }
        };

        Button button = findViewById(R.id.textSizeButton);
        button.setOnClickListener(onClickListener);

        //Go back button to main screen
        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SettingsActivityStudent.this, MainActivityStudent.class);
                SettingsActivityStudent.this.finish();
                startActivity(intent);
            }
        };

        Button button2 = findViewById(R.id.settingsGoBack);
        button2.setOnClickListener(onClickListener2);
    }

    private void applyFontSize(String fontSize) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        float sp = 14; // Default size

        // Configuration of size depending on the option chosen
        if (fontSize.equals("Pequeña")) {
            sp = 12;
        } else if (fontSize.equals("Normal")) {
            sp = 14;
        } else if (fontSize.equals("Grande")) {
            sp = 16;
        }

        // Calculate font size in pixels
        int px = (int) (sp * scaledDensity);

        // Apply all over the app
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = sp / 14f; // Adjust the scale
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Restart the activity to apply changes
        restartActivity();
    }

    private void restartActivity() {
        //Start the activity from zero again
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

}







