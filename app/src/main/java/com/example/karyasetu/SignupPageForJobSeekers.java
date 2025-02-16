package com.example.karyasetu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SignupPageForJobSeekers extends AppCompatActivity {

    Button backBtn_job, signupBtn_job;
    EditText firstNameEdittext,lastNameEdittext,emailAddressEdittext,passwordEdittext;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_page_for_job_seekers);

        firestore = FirebaseFirestore.getInstance();

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
                    checkIfEmailExists(email_txt, firstname_txt, lastname_txt);
                }
            }
        });
    }

    private void checkIfEmailExists(String email, String firstname, String lastname) {
        firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots ->
                {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        showToast("Error", "Email already registered. Please use another email.", MotionToastStyle.ERROR);
                    }
                    else
                    {
                        registerUserInFirestore(firstname, lastname, email);
                    }
                })
                .addOnFailureListener(e -> showToast("Error", "Failed to check email: " + e.getMessage(), MotionToastStyle.ERROR));
    }

    private void registerUserInFirestore(String firstname, String lastname, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", firstname);
        user.put("lastName", lastname);
        user.put("email", email);
        user.put("role", "job_seeker");

        firestore.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showRegistrationSuccessDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Error", "Signup Failed", MotionToastStyle.ERROR);
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

    private void showToast(String title, String message, MotionToastStyle style) {
        MotionToast.Companion.createColorToast(this,
                title, message, style,
                MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.montserrat_semibold));
    }

    private void showRegistrationSuccessDialog()
    {
        Dialog successful_registration_dialogBox = new Dialog(SignupPageForJobSeekers.this);
        successful_registration_dialogBox.setContentView(R.layout.custom_success_dialogbox);
        Button dialogBox_ok_button = successful_registration_dialogBox.findViewById(R.id.okbutton_successDialogBox);
        dialogBox_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                successful_registration_dialogBox.dismiss();
                Intent intent = new Intent(SignupPageForJobSeekers.this, IndexPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        successful_registration_dialogBox.show();

        MotionToast.Companion.createColorToast(SignupPageForJobSeekers.this,
                "Success", "Press OK to redirect to the Index Page for login.",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(SignupPageForJobSeekers.this, R.font.montserrat_semibold));
    }
}