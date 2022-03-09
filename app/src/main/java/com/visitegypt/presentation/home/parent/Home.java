package com.visitegypt.presentation.home.parent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.visitegypt.R;
import com.visitegypt.databinding.ActivityNewHomeBinding;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.signin.SignInActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Home extends AppCompatActivity {
    private BottomNavigationView navigation;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNewHomeBinding binding;
    private NavigationView navigationView;
    private NavController navController;
    private View header;
    private TextView txtName, txtEmail;
    HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        navigationController();
        startChatBot();
        homeViewModel.getUserInfo();
        homeViewModel.userEmailMutable.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("TAG", "onChanged:  String"+s);
                txtEmail.setText(s);
            }
        });
        homeViewModel.userNameMutable.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                txtName.setText(s);
            }
        });
        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(menuItem -> {
            homeViewModel.logOut();
            return false;
        });
        homeViewModel.isLoged.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean)
                {
                    redirect();
                }
            }
        });
    }

    private void startChatBot() {
        binding.appBarNewHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ChatbotActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();

            }
        });
    }

    private void updateUserInfo() {

    }

    private void navigationController() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_new_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(navigation, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_new_home);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initView() {
        navigation = binding.appBarNewHome.navigation;
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_discover,
                R.id.account,
                R.id.nav_near_me,
                R.id.nav_activities,
                R.id.nav_map,
                R.id.nav_booking,
                R.id.nav_subscription,
                R.id.setting
        )
                .setOpenableLayout(drawer)
                .build();
        /**************************************************/
        setSupportActionBar(binding.appBarNewHome.toolbar);
        header = navigationView.getHeaderView(0);
        txtName = header.findViewById(R.id.txtName);
        txtEmail = header.findViewById(R.id.txtEmail);
        /**********************************************/
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

    }

    public void setActionBarTitle(String title) {
        binding.appBarNewHome.label.setText(title);
    }

    public void hideChatBot() {
        binding.appBarNewHome.fab.hide();
    }

    public void showChatBot() {
        binding.appBarNewHome.fab.show();
    }

    private void redirect() {

        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        homeViewModel.getUserInfo();
    }
}