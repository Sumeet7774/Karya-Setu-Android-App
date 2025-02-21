package com.example.karyasetu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragmentForJobSeeker extends Fragment {

    TextView fullName_textview;
    Button delete_user_btn, logout_btn;
    ImageButton view_and_update_profiledetails_imgbtn, view_and_update_professionalprofile_imgbtn, view_and_update_workexperience_imgbtn;
    SessionManagement sessionManagement;
    private String sessionemailId;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_for_job_seeker, container, false);

        fullName_textview = view.findViewById(R.id.fullname_textview_profile);
        view_and_update_profiledetails_imgbtn = view.findViewById(R.id.updateprofile_rightbutton);
        view_and_update_professionalprofile_imgbtn = view.findViewById(R.id.updateprofessionaldata_rightbutton);
        view_and_update_workexperience_imgbtn = view.findViewById(R.id.updateworkexperience_rightbutton);
        delete_user_btn = view.findViewById(R.id.delete_user_button);
        logout_btn = view.findViewById(R.id.logout_button_job);

        sessionManagement = new SessionManagement(getContext());
        sessionemailId = sessionManagement.getEmailId();

        db = FirebaseFirestore.getInstance();

        fetchFullName(sessionemailId);

        view_and_update_workexperience_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view_and_update_professionalprofile_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view_and_update_workexperience_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        delete_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }

    private void fetchFullName(String emailId) {
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("email", emailId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty())
                {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");
                    String fullName = firstName + " " + lastName;

                    if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty())
                    {
                        fullName_textview.setText(fullName);
                    }
                    else
                    {
                        fullName_textview.setText("N/A");
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}