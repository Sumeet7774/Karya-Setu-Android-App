package com.example.karyasetu;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.firebase.firestore.FirebaseFirestore;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class JobPostsEditPage extends AppCompatActivity {

    EditText jobTitle, companyName, salary, jobDesc, stateName, districtName;
    Button backButton,updateButton;
    FirebaseFirestore db;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_posts_edit_page);

        db = FirebaseFirestore.getInstance();
        sessionManagement = new SessionManagement(this);

        jobTitle = findViewById(R.id.job_title_editjobposts_emp);
        companyName = findViewById(R.id.companyname_editjobposts_emp);
        salary = findViewById(R.id.job_salary_editjobposts_emp);
        jobDesc = findViewById(R.id.job_description_editjobposts_emp);
        stateName = findViewById(R.id.editjobposts_state_emp);
        districtName = findViewById(R.id.editjobposts_district_emp);
        backButton = findViewById(R.id.back_button_editjobposts_emp);
        updateButton = findViewById(R.id.editjobposts_update_button_emp);

        SharedPreferences sharedPreferences = getSharedPreferences("JobData", Context.MODE_PRIVATE);

        jobTitle.setText(sharedPreferences.getString("jobTitle", ""));
        companyName.setText(sharedPreferences.getString("companyName", ""));
        salary.setText(sharedPreferences.getString("jobSalary", ""));
        jobDesc.setText(sharedPreferences.getString("jobDescription", ""));
        stateName.setText(sharedPreferences.getString("stateName", ""));
        districtName.setText(sharedPreferences.getString("districtName", ""));

        InputFilter noSpacesFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.toString().contains(" "))
                {
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Spaces are not allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
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
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Only letters,numbers,few punctuation are allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
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

        jobTitle.setFilters(new InputFilter[]{jobTitleCompanyFilter, new InputFilter.LengthFilter(25)});
        companyName.setFilters(new InputFilter[]{jobTitleCompanyFilter, new InputFilter.LengthFilter(25)});
        jobDesc.setFilters(new InputFilter[]{jobDescriptionFilter, new InputFilter.LengthFilter(500)});
        salary.setFilters(new InputFilter[]{salaryFilter, noSpacesFilter});
        stateName.setFilters(new InputFilter[]{stateDistrictFilter, new InputFilter.LengthFilter(25)});
        districtName.setFilters(new InputFilter[]{stateDistrictFilter, new InputFilter.LengthFilter(25)});

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobTitle_txt = jobTitle.getText().toString().trim();
                String companyName_txt = companyName.getText().toString().trim();
                String jobSalary_txt = salary.getText().toString().trim();
                String jobDescription_txt = jobDesc.getText().toString().trim();
                String state_txt = stateName.getText().toString().trim();
                String district_txt = districtName.getText().toString().trim();

                if (TextUtils.isEmpty(jobTitle_txt) || TextUtils.isEmpty(companyName_txt) || TextUtils.isEmpty(jobSalary_txt) || TextUtils.isEmpty(jobDescription_txt) || TextUtils.isEmpty(state_txt) || TextUtils.isEmpty(district_txt))
                {
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Please provide all of your credentials.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
                }
                else if(!isValidJobTitle(jobTitle_txt))
                {
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Job title must contain only letters and basic punctuation.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
                }
                else if (!isValidCompanyName(companyName_txt))
                {
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Company name must contain only letters and basic punctuation.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
                }
                else if (!isValidJobDescription(jobDescription_txt))
                {
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Invalid job description. Follow the format rules.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
                }
                else if(!isValidState(state_txt))
                {
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Invalid state name. Only letters and spaces are allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
                }
                else if (!isValidDistrict(district_txt))
                {
                    MotionToast.Companion.createColorToast(JobPostsEditPage.this,
                            "Error", "Invalid district name. Only letters and spaces are allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(JobPostsEditPage.this, R.font.montserrat_semibold));
                }
                else
                {
                    updateJobPostToFirestore(jobTitle_txt, companyName_txt, jobSalary_txt, jobDescription_txt, state_txt, district_txt);
                }
            }
        });
    }

    private void updateJobPostToFirestore(String jobTitle, String companyName, String jobSalary, String jobDescription, String state, String district) {
        db.collection("job_posts")
                .whereEqualTo("jobTitle", jobTitle)
                .whereEqualTo("companyName", companyName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        db.collection("job_posts").document(documentId)
                                .update(
                                        "jobTitle", jobTitle,
                                        "companyName", companyName,
                                        "jobSalary", jobSalary,
                                        "jobDescription", jobDescription,
                                        "stateName", state,
                                        "districtName", district
                                )
                                .addOnSuccessListener(aVoid -> {
                                    showToast("Success", "Job post updated successfully!", MotionToastStyle.SUCCESS);
                                    clearInputFields();
                                    finish();
                                })
                                .addOnFailureListener(e -> showToast("Error", "Failed to update job post: " + e.getMessage(), MotionToastStyle.ERROR));
                    } else {
                        showToast("Error", "Job post not found!", MotionToastStyle.ERROR);
                    }
                })
                .addOnFailureListener(e -> showToast("Error", "Failed to fetch job post: " + e.getMessage(), MotionToastStyle.ERROR));
    }

    private void clearInputFields() {
        jobDesc.setText("");
        companyName.setText("");
        salary.setText("");
        jobDesc.setText("");
        stateName.setText("");
        districtName.setText("");
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