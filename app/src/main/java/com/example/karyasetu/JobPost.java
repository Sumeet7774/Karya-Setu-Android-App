package com.example.karyasetu;

public class JobPost {
    private String documentId;
    private String jobTitle;
    private String companyName;
    private String jobSalary;
    private String jobDescription;
    private String stateName;
    private String districtName;
    private String employerEmail;

    public JobPost()
    {
        // Default constructor required for calls to DataSnapshot.getValue(JobPost.class)
    }

    public JobPost(String jobTitle, String companyName, String jobSalary, String jobDescription, String stateName, String districtName, String employerEmail) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobSalary = jobSalary;
        this.jobDescription = jobDescription;
        this.stateName = stateName;
        this.districtName = districtName;
        this.employerEmail = employerEmail;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getStateName() {
        return stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }
}

