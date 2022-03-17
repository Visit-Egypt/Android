package com.visitegypt.presentation.home.parent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.visitegypt.R;
import com.visitegypt.databinding.ActivityNewHomeBinding;
import com.visitegypt.domain.model.SearchPlace;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.setting.SettingViewModel;
import com.visitegypt.presentation.signin.SignInActivity;
import com.visitegypt.utils.UploadUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Home extends AppCompatActivity {
    private BottomNavigationView navigation;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNewHomeBinding binding;
    private NavigationView navigationView;
    private NavController navController;
    private View header, searchViewLayout, homeViewLayout;
    private TextView txtName, txtEmail;
    HomeViewModel homeViewModel;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private RecyclerView searchRecyclerView;
    private ArrayList<SearchPlace> searchPlaces = new ArrayList<>();
    private static final String TAG = "Home";
    private TextView txtNotFound;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        searchViewModel.webSocketConnet();
        navigationController();
        startChatBot();
        searchResult();
        homeViewModel.getUserInfo();
        homeViewModel.userEmailMutable.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("TAG", "onChanged:  String" + s);
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
                if (!aBoolean) {
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
        txtName = header.findViewById(R.id.nameNavHeaderTextView);
        txtEmail = header.findViewById(R.id.emailTextView);

        /**********************************************/
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        /**************************************************************/
        homeViewLayout = findViewById(R.id.home_layout);
        searchViewLayout = findViewById(R.id.search_layout);
        txtNotFound = searchViewLayout.findViewById(R.id.not_found);
        /*********************************************************/
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchRecyclerView = searchViewLayout.findViewById(R.id.searchRecyclerView);
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(searchPlaces, this);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
        /********************************************************/


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.search);
        MenuItem menuItemNotification = menu.findItem(R.id.notification);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setQueryHint("Search on visit egypt");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.isEmpty()) {
                    searchPlaces.clear();
                    searchRecyclerViewAdapter.updatePlacesList(searchPlaces);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.isEmpty()) {
                    Log.d(TAG, "onQueryTextChange: " + s);
                    searchViewModel.search(s);
                } else {
                    searchPlaces.clear();
                    searchRecyclerViewAdapter.updatePlacesList(searchPlaces);
                }

                return false;
            }
        });
        menuItemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                menuItemNotification.setVisible(false);
                menuItemSearch.setVisible(false);
                homeViewLayout.setVisibility(View.GONE);
                searchViewLayout.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                menuItemNotification.setVisible(true);
                menuItemSearch.setVisible(true);
                homeViewLayout.setVisibility(View.VISIBLE);
                searchViewLayout.setVisibility(View.GONE);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchResult() {
        searchViewModel.mutableLiveDataText.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "Web sockets Test onChanged: " + s);
                if (!s.contains("errors")) {
                    txtNotFound.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<SearchPlace>>() {
                    }.getType();
                    searchPlaces = gson.fromJson(s, listType);
                    searchRecyclerViewAdapter.updatePlacesList(searchPlaces);
                } else {
                    txtNotFound.setVisibility(View.VISIBLE);
                    searchPlaces.clear();
                    searchRecyclerViewAdapter.updatePlacesList(searchPlaces);
                }


            }
        });
    }



    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}