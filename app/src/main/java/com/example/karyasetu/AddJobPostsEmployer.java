package com.example.karyasetu;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class AddJobPostsEmployer extends AppCompatActivity {

    Button backBtn_addjobposts_emp, submitBtn_addjobposts_emp;
    EditText jobTitle_addjobposts_emp, companyName_addjobposts_emp, jobSalary_addjobposts_emp, jobDescription_addjobposts_emp, state_addjobposts_emp, district_addjobposts_emp;
    SessionManagement sessionManagement;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_job_posts_employer);

        sessionManagement = new SessionManagement(this);
        db = FirebaseFirestore.getInstance();

        backBtn_addjobposts_emp = findViewById(R.id.back_button_addjobposts_emp);
        submitBtn_addjobposts_emp = findViewById(R.id.addjobposts_submit_button_emp);
        jobTitle_addjobposts_emp = findViewById(R.id.job_title_addjobposts_emp);
        companyName_addjobposts_emp = findViewById(R.id.companyname_addjobposts_emp);
        jobSalary_addjobposts_emp = findViewById(R.id.job_salary_addjobposts_emp);
        jobDescription_addjobposts_emp = findViewById(R.id.job_description_addjobposts_emp);
        state_addjobposts_emp = findViewById(R.id.addjobposts_state_emp);
        district_addjobposts_emp = findViewById(R.id.addjobposts_district_emp);

        backBtn_addjobposts_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        InputFilter noSpacesFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.toString().contains(" "))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Spaces are not allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                    return source.toString().replace(" ", "");
                }
                return null;
            }
        };

        InputFilter jobDescriptionFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String updatedText = dest.toString() + source.toString();

                if (!updatedText.matches("[a-zA-Z0-9 ,\".&\\-]*"))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Only letters,numbers,few punctuation are allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                    return "";
                }

                return null;
            }
        };

        InputFilter salaryFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!source.toString().matches("[0-9]*")) {
                    showToast("Error", "Only numbers are allowed in salary.", MotionToastStyle.ERROR);
                    return "";
                }
                return null;
            }
        };

        InputFilter stateDistrictFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!source.toString().matches("[a-zA-Z ]*")) {
                    showToast("Error", "Only letters and spaces are allowed.", MotionToastStyle.ERROR);
                    return "";
                }
                return null;
            }
        };

        InputFilter jobTitleCompanyFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!source.toString().matches("[a-zA-Z &\\-]*")) {
                    showToast("Error", "Only letters, spaces, and basic punctuation (-, &) are allowed.", MotionToastStyle.ERROR);
                    return "";
                }
                return null;
            }
        };

        jobTitle_addjobposts_emp.setFilters(new InputFilter[]{jobTitleCompanyFilter, new InputFilter.LengthFilter(25)});
        companyName_addjobposts_emp.setFilters(new InputFilter[]{jobTitleCompanyFilter, new InputFilter.LengthFilter(25)});
        jobDescription_addjobposts_emp.setFilters(new InputFilter[]{jobDescriptionFilter, new InputFilter.LengthFilter(500)});
        jobSalary_addjobposts_emp.setFilters(new InputFilter[]{salaryFilter, noSpacesFilter});
        state_addjobposts_emp.setFilters(new InputFilter[]{stateDistrictFilter, new InputFilter.LengthFilter(25)});
        district_addjobposts_emp.setFilters(new InputFilter[]{stateDistrictFilter, new InputFilter.LengthFilter(25)});

        submitBtn_addjobposts_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobTitle_txt = jobTitle_addjobposts_emp.getText().toString().trim();
                String companyName_txt = companyName_addjobposts_emp.getText().toString().trim();
                String jobSalary_txt = jobSalary_addjobposts_emp.getText().toString().trim();
                String jobDescription_txt = jobDescription_addjobposts_emp.getText().toString().trim();
                String state_txt = state_addjobposts_emp.getText().toString().trim();
                String district_txt = district_addjobposts_emp.getText().toString().trim();

                if (TextUtils.isEmpty(jobTitle_txt) || TextUtils.isEmpty(companyName_txt) || TextUtils.isEmpty(jobSalary_txt) || TextUtils.isEmpty(jobDescription_txt) || TextUtils.isEmpty(state_txt) || TextUtils.isEmpty(district_txt))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Please provide all of your credentials.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                }
                else if(!isValidJobTitle(jobTitle_txt))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Job title must contain only letters and basic punctuation.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                }
                else if (!isValidCompanyName(companyName_txt))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Company name must contain only letters and basic punctuation.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                }
                else if (!isValidJobDescription(jobDescription_txt))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Invalid job description. Follow the format rules.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                }
                else if(!isValidState(state_txt))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Invalid state name. Only letters and spaces are allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                }
                else if (!isValidDistrict(district_txt))
                {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Invalid district name. Only letters and spaces are allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                }
                else
                {
                    addJobPostToFirestore(jobTitle_txt, companyName_txt, jobSalary_txt, jobDescription_txt, state_txt, district_txt);
                }
            }
        });
    }

    private void addJobPostToFirestore(String jobTitle, String companyName, String jobSalary, String jobDescription, String stateName, String districtName) {
        Map<String, Object> jobPost = new HashMap<>();
        jobPost.put("jobTitle", jobTitle);
        jobPost.put("companyName", companyName);
        jobPost.put("jobSalary", jobSalary);
        jobPost.put("jobDescription", jobDescription);
        jobPost.put("stateName", stateName);
        jobPost.put("districtName", districtName);
        jobPost.put("emailId", sessionManagement.getEmailId());

        db.collection("job_posts")
                .add(jobPost)
                .addOnSuccessListener(documentReference -> {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Success", "Job post added successfully!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));

                    clearInputFields();

                    finish();
                })
                .addOnFailureListener(e -> {
                    MotionToast.Companion.createColorToast(AddJobPostsEmployer.this,
                            "Error", "Failed to add job post. Try again.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(AddJobPostsEmployer.this, R.font.montserrat_semibold));
                });
    }

    private void clearInputFields() {
        jobTitle_addjobposts_emp.setText("");
        companyName_addjobposts_emp.setText("");
        jobSalary_addjobposts_emp.setText("");
        jobDescription_addjobposts_emp.setText("");
        state_addjobposts_emp.setText("");
        district_addjobposts_emp.setText("");
    }


    private boolean isValidJobTitle(String jobTitle)
    {
        return jobTitle.matches("[a-zA-Z &\\-]+");
    }

    private boolean isValidCompanyName(String companyName)
    {
        return companyName.matches("[a-zA-Z &\\-]+");
    }

    private boolean isValidJobDescription(String jobDescription)
    {
        return jobDescription.matches("[a-zA-Z0-9 ,\".&\\-]+");
    }

    private boolean isValidState(String state)
    {
        return state.matches("[a-zA-Z ]+") && state.length() >= 3;
    }

    private boolean isValidDistrict(String district)
    {
        return district.matches("[a-zA-Z ]+") && district.length() >= 3;
    }

    private void showToast(String title, String message, MotionToastStyle style)
    {
        MotionToast.Companion.createColorToast(this,
                title, message, style,
                MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.montserrat_semibold));
    }
}