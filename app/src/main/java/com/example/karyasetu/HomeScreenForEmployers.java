package com.example.karyasetu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreenForEmployers extends AppCompatActivity {

    SessionManagement sessionManagement;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen_for_employers);

        sessionManagement = new SessionManagement(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationMenu_emp);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragmentForEmployer(), true, false);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                Fragment currentFragment = getCurrentFragment();
                boolean isNavigatingBack = false;

                if (itemId == R.id.home_menu_emp) {
                    if (!(currentFragment instanceof HomeFragmentForEmployer)) {
                        isNavigatingBack = currentFragment != null &&
                                (currentFragment instanceof MyJobPostsFragmentForEmployer ||
                                        currentFragment instanceof ApplicantsFragmentForEmployer ||
                                        currentFragment instanceof ProfileFragmentForEmployer);
                        loadFragment(new HomeFragmentForEmployer(), false, isNavigatingBack);
                    }
                } else if (itemId == R.id.myjobposts_emp) {
                    if (!(currentFragment instanceof MyJobPostsFragmentForEmployer)) {
                        isNavigatingBack = currentFragment != null &&
                                (currentFragment instanceof ApplicantsFragmentForEmployer ||
                                        currentFragment instanceof ProfileFragmentForEmployer);
                        loadFragment(new MyJobPostsFragmentForEmployer(), false, isNavigatingBack);
                    }
                } else if (itemId == R.id.applicants_menu_emp) {
                    if (!(currentFragment instanceof ApplicantsFragmentForEmployer)) {
                        isNavigatingBack = currentFragment != null &&
                                (currentFragment instanceof ProfileFragmentForEmployer);
                        loadFragment(new ApplicantsFragmentForEmployer(), false, isNavigatingBack);
                    }
                } else if (itemId == R.id.profile_menu_emp) {
                    if (!(currentFragment instanceof ProfileFragmentForEmployer)) {
                        loadFragment(new ProfileFragmentForEmployer(), false, false);
                    }
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment newFragment, boolean isAppInitialized, boolean isNavigatingBack) {
        Fragment currentFragment = getCurrentFragment();
        loadFragment(newFragment, currentFragment, isAppInitialized, isNavigatingBack);
    }

    private void loadFragment(Fragment newFragment, Fragment currentFragment, boolean isAppInitialized, boolean isNavigatingBack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (currentFragment != null) {
            if (isNavigatingBack) {
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in_left, R.anim.slide_out_right,
                        R.anim.slide_in_right, R.anim.slide_out_left
                );
            } else {
                fragmentTransaction.setCustomAnimations(
                        R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right
                );
            }
        }

        if (isAppInitialized && fragmentManager.findFragmentById(R.id.frameLayout) == null) {
            fragmentTransaction.add(R.id.frameLayout, newFragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, newFragment);
        }

        fragmentTransaction.commit();
    }

    private Fragment getCurrentFragment()
    {
        return getSupportFragmentManager().findFragmentById(R.id.frameLayout);
    }
}