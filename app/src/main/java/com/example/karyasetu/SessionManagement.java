package com.example.karyasetu;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    private SharedPreferences prefs;

    public SessionManagement(Context context)
    {
        prefs = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
    }

    public String getEmailId()
    {
        return prefs.getString("email_id", "");
    }

    public void setEmailId(String emailId)
    {
        prefs.edit().putString("email_id", emailId).apply();
    }

    public String getUserRole()
    {
        return prefs.getString("role", "");
    }

    public void setUserRole(String userRole)
    {
        prefs.edit().putString("role", userRole).apply();
    }

    public void logout()
    {
        prefs.edit().remove("email_id").apply();
        prefs.edit().remove("role").apply();
    }
}
