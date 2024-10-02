package com.example.gigglegossip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // Check if user is logged in
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(splash.this, MainActivity.class); // Change to your main activity
            } else {
                intent = new Intent(splash.this, login.class);
            }

            startActivity(intent);
            finish();
        }, 2000); // Splash screen delay
    }
}
