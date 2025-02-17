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

public class IndexPageForLogin extends AppCompatActivity {

    Button backBtn_indexpage_for_login, loginForJobSeekersBtn_indexpage_for_login, loginForEmployersBtn_indexpage_for_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_index_page_for_login);

        backBtn_indexpage_for_login = findViewById(R.id.back_button_index_loginpage);
        loginForJobSeekersBtn_indexpage_for_login = findViewById(R.id.indexpage_login_for_jobseekers_button_loginpage);
        loginForEmployersBtn_indexpage_for_login = findViewById(R.id.indexpage_login_for_employers_button_loginpage);

        backBtn_indexpage_for_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndexPageForLogin.this, IndexPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        loginForJobSeekersBtn_indexpage_for_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndexPageForLogin.this, LoginPageForJobSeekers.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        loginForEmployersBtn_indexpage_for_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IndexPageForLogin.this, LoginPageForEmployers.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }
}