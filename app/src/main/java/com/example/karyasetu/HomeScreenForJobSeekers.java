package com.example.karyasetu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreenForJobSeekers extends AppCompatActivity {

    SessionManagement sessionManagement;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_for_job_seekers);

        sessionManagement = new SessionManagement(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationMenu_job);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragmentForJobSeeker(), true, false);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                Fragment currentFragment = getCurrentFragment();
                boolean isNavigatingBack = false;

                if (itemId == R.id.home_menu_job) {
                    if (!(currentFragment instanceof HomeFragmentForJobSeeker)) {
                        isNavigatingBack = currentFragment != null &&
                                (currentFragment instanceof JobListingsFragmentForJobSeeker ||
                                        currentFragment instanceof ApplicationsFragmentForJobSeeker ||
                                        currentFragment instanceof ProfileFragmentForJobSeeker);
                        loadFragment(new HomeFragmentForJobSeeker(), false, isNavigatingBack);
                    }
                } else if (itemId == R.id.job_menu_job) {
                    if (!(currentFragment instanceof JobListingsFragmentForJobSeeker)) {
                        isNavigatingBack = currentFragment != null &&
                                (currentFragment instanceof ApplicationsFragmentForJobSeeker ||
                                        currentFragment instanceof ProfileFragmentForJobSeeker);
                        loadFragment(new JobListingsFragmentForJobSeeker(), false, isNavigatingBack);
                    }
                } else if (itemId == R.id.application_menu_job) {
                    if (!(currentFragment instanceof ApplicationsFragmentForJobSeeker)) {
                        isNavigatingBack = currentFragment != null &&
                                (currentFragment instanceof ProfileFragmentForJobSeeker);
                        loadFragment(new ApplicationsFragmentForJobSeeker(), false, isNavigatingBack);
                    }
                } else if (itemId == R.id.profile_menu_job) {
                    if (!(currentFragment instanceof ProfileFragmentForJobSeeker)) {
                        loadFragment(new ProfileFragmentForJobSeeker(), false, false);
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

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frameLayout);
    }
}
