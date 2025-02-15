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

public class SignupPageForEmployers extends AppCompatActivity {

    Button backBtn_emp, signupBtn_emp;
    EditText companyname_edittext_emp, companydescript_edittext_emp, phonenumber_edittext_emp, email_edittext_emp, password_edittext_emp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_page_for_employers);

        backBtn_emp = findViewById(R.id.back_button_signuppage_emp);
        signupBtn_emp = findViewById(R.id.signup_button_signuppage_emp);
        companyname_edittext_emp = findViewById(R.id.nameofcompany_edittext_signuppage_emp);
        companydescript_edittext_emp = findViewById(R.id.description_edittext_signuppage_emp);
        phonenumber_edittext_emp = findViewById(R.id.phone_edittext_signuppage_emp);
        email_edittext_emp = findViewById(R.id.email_edittext_signuppage_emp);
        password_edittext_emp = findViewById(R.id.password_edittext_signuppage_emp);

        backBtn_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupPageForEmployers.this, IndexPageForSignup.class);
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
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Spaces are not allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                    return source.toString().replace(" ", "");
                }
                return null;
            }
        };

        InputFilter companyDescriptionFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String updatedText = dest.toString() + source.toString();

                if (!updatedText.matches("[a-zA-Z0-9 ]*"))
                 {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Only alphabets and numbers are allowed.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                    return "";
                }

                /*

                // Count words in the description
                String[] words = updatedText.trim().split("\\s+");
                if (words.length < 12)
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Company description must be at least 12 words long.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                }
                else if (words.length > 50)
                {
                    return "";
                }

                */

                return null;
            }
        };




        password_edittext_emp.setFilters(new InputFilter[]
                {
                        new InputFilter.LengthFilter(12),
                        noSpacesFilter
                });

        companyname_edittext_emp.setFilters(new InputFilter[]{noSpacesFilter, new InputFilter.LengthFilter(16)});
        companydescript_edittext_emp.setFilters(new InputFilter[]{companyDescriptionFilter});
        email_edittext_emp.setFilters(new InputFilter[]{noSpacesFilter});
        password_edittext_emp.setFilters(new InputFilter[]{noSpacesFilter});

        signupBtn_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyaname_txt = companyname_edittext_emp.getText().toString().trim();
                String companydesc_txt = companydescript_edittext_emp.getText().toString().trim();
                String email_txt = email_edittext_emp.getText().toString().trim();
                String password_txt = password_edittext_emp.getText().toString().trim();

                if (TextUtils.isEmpty(companyaname_txt) || TextUtils.isEmpty(companydesc_txt) || TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Please provide all of your credentials.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                }
                else if (!isValidCompanyName(companyaname_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Company name must contain only letters and numbers, without spaces.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                }
                else if (!isValidCompanyDescription(companydesc_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Invalid company description. Follow the format rules.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                }
                else if (!isValidEmail(email_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Please enter a valid email address",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                }
                else if (!isValidPassword(password_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Password must be 8 chars with A-Z,0-9 and symbols",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
                }
                else
                {
                    Intent intent = new Intent(SignupPageForEmployers.this, IndexPageForLogin.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        });

    }

    private boolean isValidCompanyName(String companyName) {
        return companyName.matches("[a-zA-Z]+");
    }

    private boolean isValidCompanyDescription(String description) {
        if (!description.matches("[a-zA-Z0-9 ]*")) {
            MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                    "Error", "Only letters, numbers, and spaces are allowed in description.",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
            return false;
        }

        String[] words = description.trim().split("\\s+");
        if (words.length < 12) {
            MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                    "Error", "Company description must be at least 12 words long.",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
            return false;
        }
        else if (words.length > 50) {
            MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                    "Error", "Company description must not exceed 50 words.",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
            return false;
        }

        return true;
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