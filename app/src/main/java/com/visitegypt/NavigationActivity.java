package com.visitegypt;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.visitegypt.ui.account.AccountFragment;
import com.visitegypt.ui.home.HomeFragment;
import com.visitegypt.ui.setting.SettingFragment;

public class NavigationActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.navFragment, new HomeFragment()).commit();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.setting:
                    selectedFragment = new SettingFragment();
                    break;
                case R.id.account:
                    selectedFragment = new AccountFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.navFragment, selectedFragment).commit();
            return true;
        }
    };
}
