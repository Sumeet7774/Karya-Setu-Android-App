package com.example.karyasetu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IndexPage extends AppCompatActivity {

    Button signupButton, loginButton;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index_page);

        sessionManagement = new SessionManagement(this);

        signupButton = findViewById(R.id.signup_button_indexpage);
        loginButton = findViewById(R.id.login_button_indexpage);

        if (!sessionManagement.getEmailId().isEmpty()) {
            redirectToHomeScreen();
            return; // Prevents the rest of the code from executing
        }

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndexPage.this, IndexPageForSignup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndexPage.this, IndexPageForLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void redirectToHomeScreen() {
        String role = sessionManagement.getUserRole();

        if ("job_seeker".equals(role))
        {
            Intent intent = new Intent(IndexPage.this, HomeScreenForJobSeekers.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
        else if ("employer".equals(role))
        {
            Intent intent = new Intent(IndexPage.this, HomeScreenForEmployers.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }
}