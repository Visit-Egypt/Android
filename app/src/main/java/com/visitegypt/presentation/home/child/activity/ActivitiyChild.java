package com.visitegypt.presentation.home.child.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.visitegypt.R;
import com.visitegypt.presentation.home.child.activity.ARCharacter.CharacterActivity;
import com.visitegypt.presentation.home.parent.Home;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActivitiyChild extends Fragment {

    ConstraintLayout getCharacterConstraintLayout;
    Dialog chooseCharacter;
    private ActivitiyChildViewModel mViewModel;
    private ShapeableImageView learnHistory, learnAboutEgypt;

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
    }

    private void initClickListeners() {

        getCharacterConstraintLayout.setOnClickListener(v -> {
            showDialog();

        });

    }

    private void showDialog() {
        View dialogLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_character, null);
        chooseCharacter = new Dialog(getContext());
        chooseCharacter.setContentView(dialogLayout);
        chooseCharacter.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chooseCharacter.show();

        MaterialButton cancelChoosingCharacterMaterialButton = chooseCharacter.findViewById(R.id.cancelChoosingCharacterMaterialButton);
        cancelChoosingCharacterMaterialButton.setOnClickListener(v -> {
            chooseCharacter.dismiss();
        });

        CircularImageView anubisCharacterCircularImageView = chooseCharacter.findViewById(R.id.anubisCharacterCircularImageView);
        anubisCharacterCircularImageView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), Home.class));

        });
        CircularImageView personalCharacterCircularImageView = chooseCharacter.findViewById(R.id.personalCharacterCircularImageView);
        personalCharacterCircularImageView.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CharacterActivity.class));
        });


    }


}