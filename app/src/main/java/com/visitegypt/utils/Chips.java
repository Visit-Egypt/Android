package com.visitegypt.utils;

import android.content.Context;

import com.google.android.material.chip.Chip;
import com.visitegypt.R;

public class Chips {
    private static Context context;

    public static void setContext(Context context) {
        Chips.context = context;
    }
    public static Chip createChipsLabel(String name)

    {
        Chip chip = new Chip(context);
        chip.setText(name);
        chip.setChipBackgroundColorResource(R.color.gold);
        chip.setTextColor(context.getColor(R.color.white));
        chip.setCloseIconVisible(false);
        return chip;
    }
}
