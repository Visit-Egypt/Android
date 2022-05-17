package com.visitegypt.presentation.home.parent;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.visitegypt.R;
import com.visitegypt.databinding.ActivityNewHomeBinding;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.SearchPlace;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.setting.SettingFragment;
import com.visitegypt.presentation.signin.SignInActivity;
import com.visitegypt.utils.GamificationRules;

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
    private HomeViewModel homeViewModel;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private RecyclerView searchRecyclerView;
    private ArrayList<SearchPlace> searchPlaces = new ArrayList<>();
    private static final String TAG = "Home";
    private TextView txtNotFound;
    private SearchViewModel searchViewModel;
    private MaterialButton editButton;
    private ImageView userImageView;
    private List<TripMateRequest> tripMateRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        searchViewModel.webSocketConnet();
        navigationController();
        startChatBot();
        searchResult();
        homeViewModel.getUserInfo();
        homeViewModel.getUserData();
        homeViewModel.getAllTages();
        ViewModelObserve();
        logOut();

        checkFirstLogIn();

        editButton.setOnClickListener(v -> changeFragment(new SettingFragment()));

    }

    private void checkFirstLogIn() {
        homeViewModel.getAllBadges();
        homeViewModel.allBadgesMutableLiveData.observe(this, badges -> {
            Log.d(TAG, "checkFirstLogIn: retrieved badges");

            homeViewModel.mutableLiveDataUser.observe(this, user -> {
                Log.d(TAG, "checkFirstLogIn: found user");
                boolean firstSignIn = true;
                if (user.getBadges() != null) {
                    for (Badge badge : user.getBadges()) {
                        Log.d(TAG, "checkFirstLogIn: checking badge with id: " + badge.getId());
                        if (badge.getId().equals(GamificationRules.FIRST_SIGN_IN_BADGE_ID)) {
                            Log.d(TAG, "checkFirstLogIn: found that first sign in badge");
                            firstSignIn = false;
                            break;
                        }
                    }
                } else {
                    Log.d(TAG, "checkFirstLogIn: user badges null");
                }

                if (firstSignIn) {
                    Log.d(TAG, "checkFirstLogIn: first sign in... giving badge...");
//                    String badgeId = GamificationRules.FIRST_SIGN_IN_BADGE_ID;
//                    String imgUrl = "https://www.vanstyle.co.uk/van/images/productsbig/va_7080_va7081_va7082_va7083_va7460_vw_logo_t6_gp_front_badge_backing_coloured_1035_jb_b.jpg";
//                    Badge badge = new Badge(badgeId, imgUrl, true, Badge.Type.GENERAL, 30, "");
//                    badge.setTitle("First sign in");
//                    badge.setDescription("New buddy joined in");

//                    badgeTask.setImageUrl(imgUrl);
//                    badgeTasks.add(badgeTask);
//                    badge.setBadgeTasks(badgeTasks);
//                    badge.setProgress(1);
//                    badge.setMaxProgress(1);
//                    badge.setPinned(true);
//                    badge.setImageUrl(imgUrl);
                    Badge badge = null;
                    for (Badge originalBadge : badges) {
                        Log.d(TAG, "checkFirstLogIn: checking badge: " + originalBadge.getId());
                        if (originalBadge.getId().equals(GamificationRules.FIRST_SIGN_IN_BADGE_ID)) {
                            Log.d(TAG, "checkFirstLogIn: found the sign in badge: " + originalBadge.getId());
                            badge = originalBadge;
                            originalBadge.getBadgeTasks().get(0).setProgress(1);
                            originalBadge.getBadgeTasks().get(0).setBadgeId(originalBadge.getId());
                            homeViewModel.setBadgeTask(originalBadge.getBadgeTasks().get(0));
                            homeViewModel.earnFirstBadge();
                        }
                    }
                    showFirstBadgeDialog(badge);

                } else {
                    Log.d(TAG, "checkFirstLogIn: old user... ignoring badge");
                }
            });
        });
    }

    private void showFirstBadgeDialog(Badge badge) {
        Dialog dialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.badge_earned_dialog, null, false);
        dialog.setContentView(v);

        TextView badgeTitle = v.findViewById(R.id.badgeEarnedDialogTitleTextView);
        badgeTitle.setText(badge.getTitle());

        TextView badgeDescription = v.findViewById(R.id.badgeEarnedDialogDescriptionTextView);
        badgeDescription.setText(badge.getDescription());

        CircleProgressbar circleProgressbar = v.findViewById(R.id.badgeEarnedDialogCircleProgressBar);
        circleProgressbar.setProgress(circleProgressbar.getMaxProgress());
        circleProgressbar.setForegroundProgressColor(Color.GREEN);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                circleProgressbar.setBackground(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(badge.getImageUrl()).into(target);

        dialog.show();
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

    private void initViews() {
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
                R.id.tripMate,
                R.id.userProfile,
                R.id.tripMateRequest
        )
                .setOpenableLayout(drawer)
                .build();
        /**************************************************/
        setSupportActionBar(binding.appBarNewHome.toolbar);
        header = navigationView.getHeaderView(0);
        txtName = header.findViewById(R.id.nameNavHeaderTextView);
        txtEmail = header.findViewById(R.id.emailTextView);
        userImageView = header.findViewById(R.id.userImageImageView);
        editButton = header.findViewById(R.id.editButton);


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

    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.nav_host_fragment_content_new_home, fragment).commit();
    }
    public void changeFragmentWithBundle(Fragment fragment,Bundle bundle) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.nav_host_fragment_content_new_home, fragment).commit();
    }
    private void ViewModelObserve() {


        homeViewModel.isLogged.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    redirect();
                }
            }
        });
        homeViewModel.mutableLiveDataUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                txtName.setText(user.getFirstName() + " " + user.getLastName());
                txtEmail.setText(user.getEmail());
                if (user.getPhotoUrl() != null )
                {
                    Log.d(TAG, "onChanged: "+user.getPhotoUrl());
                    homeViewModel.saveUserImage(user.getPhotoUrl());
                    Picasso.get().load(user.getPhotoUrl()).into(userImageView);
                    tripMateRequests = user.getTripMateRequests();

                }

            }
        });
    }
    public void logOut() {
        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(menuItem -> {
            homeViewModel.logOut();
            return false;
        });
    }

    public List<TripMateRequest> getTripMateRequests() {
        return tripMateRequests;
    }
}
