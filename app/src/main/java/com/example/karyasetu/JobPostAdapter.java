package com.example.karyasetu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobPostViewHolder> {
    private Context context;
    private List<JobPost> jobPostList;
    private OnJobPostClickListener listener;

    public JobPostAdapter(Context context, List<JobPost> jobPostList, OnJobPostClickListener listener) {
        this.context = context;
        this.jobPostList = jobPostList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_my_job_posts_emp_cardview, parent, false);
        return new JobPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobPostViewHolder holder, int position) {
        JobPost jobPost = jobPostList.get(position);
        holder.jobTitle.setText(jobPost.getJobTitle());
        holder.companyName.setText(jobPost.getCompanyName());
        holder.salary.setText(jobPost.getJobSalary());

        holder.editButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("JobData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("jobTitle", jobPost.getJobTitle());
            editor.putString("companyName", jobPost.getCompanyName());
            editor.putString("jobSalary", jobPost.getJobSalary());
            editor.putString("jobDescription", jobPost.getJobDescription());
            editor.putString("stateName", jobPost.getStateName());
            editor.putString("districtName", jobPost.getDistrictName());
            editor.apply();
            context.startActivity(new Intent(context, JobPostsEditPage.class));
        });

        holder.viewButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("JobDataDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("jobTitle", jobPost.getJobTitle());
            editor.putString("companyName", jobPost.getCompanyName());
            editor.putString("jobSalary", jobPost.getJobSalary());
            editor.putString("jobDescription", jobPost.getJobDescription());
            editor.putString("stateName", jobPost.getStateName());
            editor.putString("districtName", jobPost.getDistrictName());
            editor.apply();
            context.startActivity(new Intent(context, JobPostDetailsViewPageEmp.class));
        });

        holder.deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Job Post");
            builder.setMessage("Are you sure you want to delete this job post?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                listener.onDeleteClicked(jobPost); // Proceed with deletion
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss(); // Close dialog without deleting
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return jobPostList.size();
    }

    public static class JobPostViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName, salary, jobDescription, jobStateName, jobDistrictName;
        Button editButton, viewButton,deleteButton;

        public JobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            companyName = itemView.findViewById(R.id.company_name);
            salary = itemView.findViewById(R.id.job_salary);
            editButton = itemView.findViewById(R.id.edit_button);
            viewButton = itemView.findViewById(R.id.view_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnJobPostClickListener {
        void onEditClicked(JobPost jobPost);
        void onDeleteClicked(JobPost jobPost);
    }
}
