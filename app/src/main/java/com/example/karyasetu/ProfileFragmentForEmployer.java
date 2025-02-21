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

public class ProfileFragmentForEmployer extends Fragment {

    TextView companyName_textview;
    Button delete_user_btn, logout_btn;
    ImageButton updatecompanydetails_rightbutton, managejoblistings_rightbutton, viewandmanageapplications_rightbutton, viewandmanageshortlisted_rightbutton;
    SessionManagement sessionManagement;
     FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_for_employer, container, false);

        sessionManagement = new SessionManagement(getContext());
        db = FirebaseFirestore.getInstance();

        companyName_textview = view.findViewById(R.id.companyname_textview_profile);
        delete_user_btn = view.findViewById(R.id.delete_companyuser_button);
        logout_btn = view.findViewById(R.id.logout_button_emp);
        updatecompanydetails_rightbutton = view.findViewById(R.id.updatecompanydetails_rightbutton);
        managejoblistings_rightbutton = view.findViewById(R.id.managejoblistings_rightbutton);
        viewandmanageapplications_rightbutton = view.findViewById(R.id.viewandmanageapplications_rightbutton);
        viewandmanageshortlisted_rightbutton = view.findViewById(R.id.appliedjobs_rightbutton);

        String emailId = sessionManagement.getEmailId();
        fetchCompanyName(emailId);

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

        updatecompanydetails_rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        managejoblistings_rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewandmanageapplications_rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewandmanageshortlisted_rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }


    private void fetchCompanyName(String emailId) {
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("email", emailId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty())
                {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    String companyName = document.getString("companyName");

                    if (companyName != null && !companyName.isEmpty())
                    {
                        companyName_textview.setText(companyName);
                    }
                    else
                    {
                        companyName_textview.setText("N/A");
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Company Name not found", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}