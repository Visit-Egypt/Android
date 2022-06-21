package com.visitegypt.utils;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.visitegypt.R;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.presentation.callBacks.OnFilterUpdate;
import com.visitegypt.presentation.callBacks.OnSelectedChipsUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

public class Chips {
    private static Context context;
    private static HashMap<Integer, String> chipIdMap = new HashMap<>();
    private static OnFilterUpdate onFilterUpdate;
    private static OnSelectedChipsUpdate OnSelectedChipsUpdate;
    private static LayoutInflater layoutInflater;

    public static void setContext(Context context) {
        Chips.context = context;
    }

    public static void setOnFilterUpdate(OnFilterUpdate onFilterUpdate) {
        Chips.onFilterUpdate = onFilterUpdate;
    }

    public static void setOnSelectedChipsUpdate(com.visitegypt.presentation.callBacks.OnSelectedChipsUpdate onSelectedChipsUpdate) {
        OnSelectedChipsUpdate = onSelectedChipsUpdate;
    }

    public static void setLayoutInflater(LayoutInflater layoutInflater) {
        Chips.layoutInflater = layoutInflater;
    }

    public static Chip createChipsLabel(String name) {
        Chip chip = new Chip(context);
        chip.setText(name);
        chip.setChipBackgroundColorResource(R.color.gold);
        chip.setTextColor(context.getColor(R.color.white));
        chip.setCloseIconVisible(false);
        chip.setChipIconVisible(false);
        chip.setCheckable(false);
        chip.setClickable(false);
        return chip;
    }

    public static void createSelectChips(List<Tag> tags, HashSet<String> myTags, ChipGroup chipGroup) {
        Random rand = new Random();
        int myId = 0 ;
        for (Tag tag : tags) {
            Chip chip = (Chip) layoutInflater.inflate(R.layout.item_hashtag, chipGroup, false);
            myId = rand.nextInt(1000);
            chip.setText(tag.getName());
            chip.setId(myId);
            if (myTags.contains(tag.getId())) {
                chip.setChecked(true);
            }
            chip.setOnCheckedChangeListener((compoundButton, b) -> {
                List<Integer> ids = chipGroup.getCheckedChipIds();
                List<String> chipIds = new ArrayList<>();
                ids.forEach(id -> {
                    chipIds.add(chipIdMap.get(id));
                });
                onFilterUpdate.onFilterUpdate(chipIds);
            });
            chipGroup.addView(chip);
            chipIdMap.put(chip.getId(), tag.getId());
        }


    }


    public static Chip createFilterChips(String name) {
        Chip chip = new Chip(context, null, R.style.Widget_MaterialComponents_Chip_Filter);
        chip.setText(name);
        chip.setCloseIconVisible(false);
        chip.setChipIconVisible(false);
        chip.setCheckable(true);
        chip.setClickable(true);
        return chip;
    }

    public static void createFilterChipsEnhance(List<Tag> tags, ChipGroup chipGroup) {
        for (Tag tag : tags) {
            Chip chip = Chips.createFilterChips(tag.getName());
            chip.setOnCheckedChangeListener((compoundButton, b) -> {
                List<Integer> ids = chipGroup.getCheckedChipIds();
                List<String> chipIds = new ArrayList<>();
                ids.forEach(id -> {
                    chipIds.add(chipIdMap.get(id));
                });
                onFilterUpdate.onFilterUpdate(chipIds);
            });
            chipGroup.addView(chip);
            chipIdMap.put(chip.getId(), tag.getId());
        }
    }


}