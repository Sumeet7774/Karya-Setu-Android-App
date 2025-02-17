package com.example.karyasetu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class LoginPageForEmployers extends AppCompatActivity {

    private Button backBtn_login_emp, googleSignInButton;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private GoogleSignInClient googleSignInClient;
    private ProgressDialog progressDialog;
    private SessionManagement sessionManagement;

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page_for_employers);

        sessionManagement = new SessionManagement(this);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        backBtn_login_emp = findViewById(R.id.back_button_loginpage_emp);
        googleSignInButton = findViewById(R.id.google_login_button_emp);

        backBtn_login_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPageForEmployers.this, IndexPageForLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        firestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // ✅ Enable offline cache
                .build());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id)) // Use Web Client ID from Firebase
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Set Google Sign-In button click listener
        googleSignInButton.setOnClickListener(view -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
            progressDialog.setMessage("Logging in, please wait...");
            progressDialog.show();  // Show loading indicator when sign-in starts
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    authenticateWithFirebase(account);
                } else {
                    progressDialog.dismiss();  // Dismiss loading if authentication fails
                }
            } catch (ApiException e) {
                progressDialog.dismiss();  // Dismiss loading if authentication fails
                showToast("Error", "Google Sign-In Failed", MotionToastStyle.ERROR);
            }
        }
    }

    private void authenticateWithFirebase(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        if (idToken == null) {
            Log.e("AuthError", "ID Token is null, cannot authenticate.");
            showToast("Error", "Google authentication failed. Try again.", MotionToastStyle.ERROR);
            progressDialog.dismiss();
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        checkIfUserExists(user.getEmail());
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e("AuthError", "Authentication Failed: " + e.getMessage());
                    showToast("Error", "Authentication Failed: " + e.getMessage(), MotionToastStyle.ERROR);
                });
    }

    private void checkIfUserExists(String email) {
        progressDialog.setMessage("Checking email in database...");
        progressDialog.show();

        firestore.collection("users")
                .whereEqualTo("email", email)
                .limit(1) // ✅ Fetch only one document for faster query
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressDialog.dismiss(); // Dismiss loading once email check is done

                    if (!queryDocumentSnapshots.isEmpty()) {
                        String role = queryDocumentSnapshots.getDocuments().get(0).getString("role");
                        //String name = queryDocumentSnapshots.getDocuments().get(0).getString("name");
                        //String phone = queryDocumentSnapshots.getDocuments().get(0).getString("phone");
                        String userId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        // ✅ Log user details in Logcat
                        Log.d("LoginSuccess", "----------------------------");
                        Log.d("LoginSuccess", "User ID: " + userId);
                        Log.d("LoginSuccess", "Email: " + email);
                        //Log.d("LoginSuccess", "Name: " + name);
                        //Log.d("LoginSuccess", "Phone: " + phone);
                        Log.d("LoginSuccess", "Role: " + role);
                        Log.d("LoginSuccess", "----------------------------");

                        if ("employer".equals(role)) {
                            sessionManagement.setEmailId(email);
                            sessionManagement.setUserRole(role);

                            Log.d("LoggedInUser", "Role: " + role);
                            Log.d("LoggedInUser", "Email: " + email);

                            showToast("Success", "Login Successful", MotionToastStyle.SUCCESS);
                            Intent intent = new Intent(LoginPageForEmployers.this, HomeScreenForEmployers.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                        else
                        {
                            showToast("Error", "This email is not registered as a Job Seeker.", MotionToastStyle.ERROR);
                            auth.signOut();
                        }
                    }
                    else
                    {
                        showToast("Error", "Email not registered.", MotionToastStyle.ERROR);
                        auth.signOut();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss(); // Dismiss loading if Firebase check fails
                    Log.e("FirestoreError", "Failed to check email: " + e.getMessage());
                    showToast("Error", "Failed to check email", MotionToastStyle.ERROR);
                });
    }

    private void showToast(String title, String message, MotionToastStyle style) {
        MotionToast.Companion.createColorToast(this,
                title, message, style,
                MotionToast.GRAVITY_BOTTOM, MotionToast.LONG_DURATION,
                getResources().getFont(R.font.montserrat_semibold));
    }
}