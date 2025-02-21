package com.example.karyasetu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class JobPostDetailsViewPageEmp extends AppCompatActivity {
    SessionManagement sessionManagement;
    FirebaseFirestore db;
    TextView jobTitle, companyName, jobSalary, jobDescription, stateName, districtName;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_post_details_view_page_emp);

        sessionManagement = new SessionManagement(this);
        db = FirebaseFirestore.getInstance();

        jobTitle = findViewById(R.id.job_title_viewjobposts_emp);
        companyName = findViewById(R.id.companyname_viewjobposts_emp);
        jobSalary = findViewById(R.id.job_salary_viewjobposts_emp);
        jobDescription = findViewById(R.id.job_description_viewjobposts_emp);
        stateName = findViewById(R.id.viewjobposts_state_emp);
        districtName = findViewById(R.id.viewjobposts_district_emp);
        backButton = findViewById(R.id.back_button_viewjobposts_emp);

        SharedPreferences sharedPreferences = getSharedPreferences("JobDataDetails", Context.MODE_PRIVATE);

        jobTitle.setText(sharedPreferences.getString("jobTitle", ""));
        companyName.setText(sharedPreferences.getString("companyName", ""));
        jobSalary.setText(sharedPreferences.getString("jobSalary", ""));
        jobDescription.setText(sharedPreferences.getString("jobDescription", ""));
        stateName.setText(sharedPreferences.getString("stateName", ""));
        districtName.setText(sharedPreferences.getString("districtName", ""));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}