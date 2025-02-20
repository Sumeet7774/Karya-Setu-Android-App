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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SignupPageForEmployers extends AppCompatActivity {

    Button backBtn_emp, signupBtn_emp;
    EditText companyname_edittext_emp, companydescript_edittext_emp, phonenumber_edittext_emp, email_edittext_emp, password_edittext_emp;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_page_for_employers);

        firestore = FirebaseFirestore.getInstance();

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

                if (!updatedText.matches("[a-zA-Z0-9 ,\".&\\-]*"))
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Only letters,numbers,few punctuation are allowed.",
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

        InputFilter phoneNumberFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!source.toString().matches("[0-9]*")) {
                    showToast("Error", "Only numbers are allowed in phone number.", MotionToastStyle.ERROR);
                    return "";
                }
                return null;
            }
        };

        password_edittext_emp.setFilters(new InputFilter[]
                {
                        new InputFilter.LengthFilter(12),
                        noSpacesFilter
                });

        //companyname_edittext_emp.setFilters(new InputFilter[]{noSpacesFilter, new InputFilter.LengthFilter(16)});
        companyname_edittext_emp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        companydescript_edittext_emp.setFilters(new InputFilter[]{companyDescriptionFilter});
        phonenumber_edittext_emp.setFilters(new InputFilter[]{phoneNumberFilter, new InputFilter.LengthFilter(10)});
        email_edittext_emp.setFilters(new InputFilter[]{noSpacesFilter});
        password_edittext_emp.setFilters(new InputFilter[]{noSpacesFilter});

        signupBtn_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyaname_txt = companyname_edittext_emp.getText().toString().trim();
                String companydesc_txt = companydescript_edittext_emp.getText().toString().trim();
                String email_txt = email_edittext_emp.getText().toString().trim();
                String phonenumber_txt = phonenumber_edittext_emp.getText().toString().trim();
                String password_txt = password_edittext_emp.getText().toString().trim();

                if (TextUtils.isEmpty(companyaname_txt) || TextUtils.isEmpty(companydesc_txt) || TextUtils.isEmpty(email_txt) || TextUtils.isEmpty(password_txt) || TextUtils.isEmpty(phonenumber_txt))
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
                            "Error", "Company name must contain only letters.",
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
                else if(!isValidPhoneNumber(phonenumber_txt))
                {
                    MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                            "Error", "Please enter a valid phone number",
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
                    checkIfEmailExists(companyaname_txt, companydesc_txt, phonenumber_txt, email_txt);
                }
            }
        });
    }

    private void checkIfEmailExists(String companyName, String companyDesc, String phoneNumber, String email) {
        firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Check if the email is already registered with an employer role
                        boolean isEmployer = false;
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String role = document.getString("role");
                            if ("employer".equals(role)) {
                                isEmployer = true;
                                break;
                            }
                        }

                        if (isEmployer) {
                            showToast("Error", "Email already registered as an Employer. Please use another email.", MotionToastStyle.ERROR);
                        } else {
                            showToast("Error", "Email already registered with another role.", MotionToastStyle.ERROR);
                        }
                    } else {
                        String phone_number = "+91"+phoneNumber;
                        registerUserInFirestore(companyName, companyDesc, phone_number, email);
                    }
                })
                .addOnFailureListener(e -> showToast("Error", "Failed to check email: " + e.getMessage(), MotionToastStyle.ERROR));
    }


    private void registerUserInFirestore(String companyName, String companyDesc, String phoneNumber ,String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("companyName", companyName);
        user.put("companyDescription", companyDesc);
        user.put("phoneNumber", phoneNumber);
        user.put("email", email);
        user.put("role", "employer");

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

    private boolean isValidCompanyName(String companyName) {
        return companyName.matches("[a-zA-Z ]+");
    }

    private boolean isValidCompanyDescription(String description) {
        if (!description.matches("[a-zA-Z0-9 ,\".&\\-]*")) {
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

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]{10}");
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
        Dialog successful_registration_dialogBox = new Dialog(SignupPageForEmployers.this);
        successful_registration_dialogBox.setContentView(R.layout.custom_success_dialogbox);
        Button dialogBox_ok_button = successful_registration_dialogBox.findViewById(R.id.okbutton_successDialogBox);
        dialogBox_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                successful_registration_dialogBox.dismiss();
                Intent intent = new Intent(SignupPageForEmployers.this, IndexPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        successful_registration_dialogBox.show();

        MotionToast.Companion.createColorToast(SignupPageForEmployers.this,
                "Success", "Press OK to redirect to the Index Page for login.",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(SignupPageForEmployers.this, R.font.montserrat_semibold));
    }
}