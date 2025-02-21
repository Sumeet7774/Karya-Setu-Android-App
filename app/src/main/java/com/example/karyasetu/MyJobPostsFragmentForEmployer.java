package com.example.karyasetu;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class MyJobPostsFragmentForEmployer extends Fragment {

    FirebaseFirestore db;
    SessionManagement sessionManagement;
    TextView no_jobsinserted_found;
    ProgressBar progressBar_loading_nojobsposts;
    FloatingActionButton add_jobs_btn;
    RecyclerView viewMyJobPosts;
    JobPostAdapter adapter;
    List<JobPost> jobPostList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_job_posts_for_employer, container, false);

        sessionManagement = new SessionManagement(getContext());
        db = FirebaseFirestore.getInstance();

        no_jobsinserted_found = view.findViewById(R.id.no_jobs_posts_textview);
        add_jobs_btn = view.findViewById(R.id.add_new_job_fab);
        viewMyJobPosts = view.findViewById(R.id.recycler_view_my_job_posts);
        progressBar_loading_nojobsposts = view.findViewById(R.id.progress_bar_loading_myjobposts_emp);
        viewMyJobPosts = view.findViewById(R.id.recycler_view_my_job_posts);

        add_jobs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddJobPostsEmployer.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        viewMyJobPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        jobPostList = new ArrayList<>();
        adapter = new JobPostAdapter(getContext(), jobPostList, new JobPostAdapter.OnJobPostClickListener() {
            @Override
            public void onEditClicked(JobPost jobPost) {
            }

            @Override
            public void onDeleteClicked(JobPost jobPost) {
                deleteJobPost(jobPost);
            }
        });
        viewMyJobPosts.setAdapter(adapter);

        fetchJobPosts();

        return view;
    }

    private void fetchJobPosts() {
        progressBar_loading_nojobsposts.setVisibility(View.VISIBLE);

        String employerEmail = sessionManagement.getEmailId();

        db.collection("job_posts")
                .whereEqualTo("emailId", employerEmail)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar_loading_nojobsposts.setVisibility(View.GONE);
                    if (task.isSuccessful() && task.getResult() != null) {
                        jobPostList.clear();
                        QuerySnapshot snapshot = task.getResult();

                        if (snapshot.isEmpty()) {
                            no_jobsinserted_found.setVisibility(View.VISIBLE);
                            viewMyJobPosts.setVisibility(View.GONE);
                        } else {
                            for (DocumentSnapshot document : snapshot.getDocuments()) {
                                JobPost jobPost = document.toObject(JobPost.class);
                                if (jobPost != null) {
                                    jobPost.setDocumentId(document.getId()); // Store Firestore document ID
                                    jobPostList.add(jobPost);
                                }
                            }
                            no_jobsinserted_found.setVisibility(View.GONE);
                            viewMyJobPosts.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteJobPost(JobPost jobPost) {
        if (jobPost.getDocumentId() == null || jobPost.getDocumentId().isEmpty()) {
            return; // Prevent deletion if document ID is null
        }

        db.collection("job_posts").document(jobPost.getDocumentId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    fetchJobPosts(); // Refresh list after deletion

                    // Show Motion Toast for successful deletion
                    MotionToast.Companion.createColorToast(getActivity(),
                            "Success", "Job post deleted successfully!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getActivity(), R.font.montserrat_semibold));
                })
                .addOnFailureListener(e -> {
                    MotionToast.Companion.createColorToast(getActivity(),
                            "Error", "Failed to delete job post.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getActivity(), R.font.montserrat_semibold));
                });
    }
}