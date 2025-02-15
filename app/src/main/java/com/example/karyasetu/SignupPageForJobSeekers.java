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

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SignupPageForJobSeekers extends AppCompatActivity {

    Button backBtn_job, signupBtn_job;
    EditText firstNameEdittext,lastNameEdittext,emailAddressEdittext,passwordEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_page_for_job_seekers);

        backBtn_job = findViewById(R.id.back_button_signuppage_job);
        firstNameEdittext = findViewById(R.id.firstname_edittext_signuppage_job);
        lastNameEdittext = findViewById(R.id.lastname_edittext_signuppage_job);
        emailAddressEdittext = findViewById(R.id.email_edittext_signuppage_job);
        passwordEdittext = findViewById(R.id.password_edittext_signuppage_job);
        signupBtn_job = findViewById(R.id.signup_button_signuppage_job);

        backBtn_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupPageForJobSeekers.this, IndexPageForSignup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        InputFilter noSpacesFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (source.toString().contains(" "))
                {
                    MotionToast.Companion.createColorToast(SignupPageForJobSeekers.this,
                            "Error", "Spaces are not allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForJobSeekers.this, R.font.montserrat_semibold));
                    return source.toString().replace(" ", "");
                }
                return null;
            }
        };

        passwordEdittext.setFilters(new InputFilter[]
                {
                        new InputFilter.LengthFilter(12),
                        noSpacesFilter
                });

        firstNameEdittext.setFilters(new InputFilter[]{noSpacesFilter, new InputFilter.LengthFilter(12)});
        lastNameEdittext.setFilters(new InputFilter[]{noSpacesFilter, new InputFilter.LengthFilter(12)});
        emailAddressEdittext.setFilters(new InputFilter[]{noSpacesFilter});
        passwordEdittext.setFilters(new InputFilter[]{noSpacesFilter});

        signupBtn_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname_txt = firstNameEdittext.getText().toString().trim();
                String lastname_txt = lastNameEdittext.getText().toString().trim();
                String email_txt = emailAddressEdittext.getText().toString().trim();
                String password_txt = passwordEdittext.getText().toString().trim();

                if (TextUtils.isEmpty(firstname_txt) || TextUtils.isEmpty(lastname_txt) || TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForJobSeekers.this,
                            "Error", "Please provide all of your credentials.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForJobSeekers.this, R.font.montserrat_semibold));
                }
                else if (!isValidFirstname(firstname_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForJobSeekers.this,
                            "Error", "Firstname must contain only letters.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForJobSeekers.this, R.font.montserrat_semibold));
                }
                else if (!isValidLastname(lastname_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForJobSeekers.this,
                            "Error", "Lastname must contain only letters.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForJobSeekers.this, R.font.montserrat_semibold));
                }
                else if (!isValidEmail(email_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForJobSeekers.this,
                            "Error", "Please enter a valid email address",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForJobSeekers.this, R.font.montserrat_semibold));
                }
                else if (!isValidPassword(password_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForJobSeekers.this,
                            "Error", "Password must be 8 chars with A-Z,0-9 and symbols",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForJobSeekers.this, R.font.montserrat_semibold));
                }
                else
                {
                    Intent intent = new Intent(SignupPageForJobSeekers.this, IndexPageForLogin.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        });
    }

    private boolean isValidFirstname(String firstname) {
        return firstname.matches("[a-zA-Z]+");
    }

    private boolean isValidLastname(String lastname) {
        return lastname.matches("[a-zA-Z]+");
    }

    private boolean isValidEmail(CharSequence target) {
        String emailPattern = "^[a-z][a-z0-9]*@gmail\\.com$";
        return (!TextUtils.isEmpty(target) && target.toString().matches(emailPattern));
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }
}