package com.visitegypt.presentation.home.child.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.unity3d.player.UnityPlayerActivity;
import com.visitegypt.R;
import com.visitegypt.presentation.home.child.activity.ARCharacter.CharacterActivity;
import com.visitegypt.presentation.home.parent.Home;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActivitiyChild extends Fragment {
    private static final String TAG = "Activitie fragment";
    DownloadManager downloadManager;
    Handler handler = new Handler();
    private ConstraintLayout getCharacterConstraintLayout, moneyARConstraintLayout, learnHistoryConstraintLayout;
    private Dialog chooseCharacterDialog;
    private ActivitiyChildViewModel mViewModel;
    private ShapeableImageView learnHistory, learnAboutEgypt;
    private View loadingLayout;

    public static ActivitiyChild newInstance() {
        return new ActivitiyChild();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View activityChild = inflater.inflate(R.layout.activitiy_child_fragment, container, false);
        initViews(activityChild);
        initClickListeners();

        return activityChild;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActivitiyChildViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        ((Home) getActivity()).setActionBarTitle("Activity");
        super.onResume();
    }


    private void initViews(View v) {
        getCharacterConstraintLayout = v.findViewById(R.id.getCharacterConstraintLayout);
        moneyARConstraintLayout = v.findViewById(R.id.moneyARConstraintLayout);
        learnHistoryConstraintLayout = v.findViewById(R.id.learnHistoryConstraintLayout);
        loadingLayout = v.findViewById(R.id.loadingLayout);

    }

    private void initClickListeners() {
        getCharacterConstraintLayout.setOnClickListener(v -> {
            showDialog();
        });
        moneyARConstraintLayout.setOnClickListener(v -> {
//            showLoading();

            Map<String, String> m1 = new HashMap<String, String>();
            Map<String, String> m2 = new HashMap<String, String>();
            Map<String, String> m3 = new HashMap<String, String>();
            Map<String, String> m4 = new HashMap<String, String>();
            Map<String, String> m5 = new HashMap<String, String>();
            Map<String, String> m6 = new HashMap<String, String>();
            Map<String, String> m7 = new HashMap<String, String>();
            Map<String, String> m8 = new HashMap<String, String>();
            Map<String, String> m9 = new HashMap<String, String>();
            Map<String, String> m10 = new HashMap<String, String>();

            m1.put("abusimple", "https://drive.google.com/uc?export=download&id=1LBwp9ayw0dmY0bj2tpbAHrrCi8S7jMYq");
            m2.put("horus", "https://drive.google.com/uc?export=download&id=15qZbz0e-taYXiT8Q5wa7cfErbEppZy4Y");
            m3.put("khafre", "https://drive.google.com/uc?export=download&id=1mo5IuWtDJ2ejmNyBPmMAjcFABufdykLe");
            m4.put("mohamedali", "https://drive.google.com/uc?export=download&id=1G8EPk-5lsLOczBbudLb5MtkC1EEAVVtF");
            m5.put("sounds", "https://drive.google.com/uc?export=download&id=17Ze3VjceIOmSBs8aVQ4ll7I0NhQkQKsB");
            m6.put("sphinx", "https://drive.google.com/uc?export=download&id=1jK3agHThZ7ezd-YFYDrLHKnY5j5RnlG6");
            m7.put("sultanhassan", "https://drive.google.com/uc?export=download&id=1du00f6ck9ayj8gJ9HqMBYU4kgpMF8BLS");
            m8.put("warwheel", "https://drive.google.com/uc?export=download&id=1ICH4KYi65Z8FOH_f3ZA8ZoY5V5s9R50o");
            m9.put("writer", "https://drive.google.com/uc?export=download&id=1rqym5wTxWZuETovaSQ1_24zDRzJ3BBa2");
            m10.put("hatshepsut", "https://drive.google.com/uc?export=download&id=1G2cmLMSPMKf9DPLJK5izKz9HwdjASiCK");
//        Collections.addAll(list, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10);
//           List<Map<String, String>> list = new ArrayList<Map<String, String>>(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10);
//            final List<Map<String, String>> list = new ArrayList<Map<String, String>>(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10);
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            Collections.addAll(list, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10);

            downloadManager = (DownloadManager) getActivity().getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);
            for (int i = 0; i < 10; i++) {
                Log.d(TAG, "onCreate: " + list.get(i).keySet().iterator().next() + "" + list.get(i).values().iterator().next());
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(list.get(i).values().iterator().next()));
                File file = new File("/storage/emulated/0/Android/data/com.visitegypt/files", list.get(i).keySet().iterator().next());
                if (file.exists()) {
                    Log.d(TAG, "buttonClick: file exist");
                } else {
                    Log.d(TAG, "buttonClick: file isn't exist" + file);
                    try {
                        request.setDestinationInExternalFilesDir(getActivity(), File.separator, list.get(i).keySet().iterator().next());
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        Long reference = downloadManager.enqueue(request);
                        Log.d(TAG, "buttonClick: done");

                    } catch (Exception e) {
                        Log.e(TAG, "buttonClick:xx " + e.getMessage());
                    }
                }

            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                Intent intent = new Intent(getActivity(), UnityPlayerActivity.class);
                intent.putExtra("sceneIndex", "1");
                startActivity(intent);
            }
//            hideLoading();
        });
        learnHistoryConstraintLayout.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), UnityPlayerActivity.class);
                    intent.putExtra("sceneIndex", "2");
                    startActivity(intent);
                }
        );
    }

    private void showDialog() {
        View dialogLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_character, null);
        chooseCharacterDialog = new Dialog(getContext());
        chooseCharacterDialog.setContentView(dialogLayout);
        chooseCharacterDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chooseCharacterDialog.show();

        MaterialButton cancelChoosingCharacterMaterialButton = chooseCharacterDialog.findViewById(R.id.cancelChoosingCharacterMaterialButton);
        cancelChoosingCharacterMaterialButton.setOnClickListener(v -> {
            chooseCharacterDialog.dismiss();
        });

        CircularImageView anubisCharacterCircularImageView = chooseCharacterDialog.findViewById(R.id.anubisCharacterCircularImageView);
        anubisCharacterCircularImageView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Anubis successfully set as your tour guide", Toast.LENGTH_SHORT).show();
//            GeneralUtils.showSnackInfo(getActivity(), getCharacterConstraintLayout, "Anubis successfully set as your tour guide");
            chooseCharacterDialog.dismiss();
        });
        CircularImageView personalCharacterCircularImageView = chooseCharacterDialog.findViewById(R.id.personalCharacterCircularImageView);
        personalCharacterCircularImageView.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CharacterActivity.class));
        });
    }

//    rivate

    void showLoading() {

        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingLayout.setVisibility(View.GONE);

    }
}