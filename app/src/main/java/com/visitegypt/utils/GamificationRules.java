package com.visitegypt.utils;

import android.util.Log;

public class GamificationRules {
    public static final int CONFIRM_LOCATION_CIRCLE_RADIUS = 500;
    public static final int MAX_LEVEL = 20;

    // ACTIVITIES XP
    public static final int XP_POST_STORY = 20;
    public static final int XP_POST_POST = 20;
    public static final int XP_VISIT_LOCATION = 100;
    public static final int XP_ASK_CHAT_BOT = 10;
    public static final int XP_POST_REVIEW = 20;
    public static final int XP_GENERAL = 5;

    // TITLES LEVELS
    public static final int EXPLORER_LEVEL = 3;
    public static final int ADVENTURER_LEVEL = 7;
    public static final int ADVANCED_TRAVELLER_LEVEL = 10;
    public static final int PHAROS_LEVEL = 15;
    public static final int ANUBIS_LEVEL = MAX_LEVEL - 1;

    public static final int[] ALL_LEVELS = {EXPLORER_LEVEL,
            ADVENTURER_LEVEL,
            ADVANCED_TRAVELLER_LEVEL,
            PHAROS_LEVEL,
            ANUBIS_LEVEL};


    public static final int BRONZE_BADGE_XP = 30;
    public static final int SILVER_BADGE_XP = 70;
    public static final int GOLD_BADGE_XP = 300;
    public static final String FIRST_SIGN_IN_BADGE_ID = "623c9aa3de3a6b3d7c407f63";

    private static final String TAG = "gamification rules";

    // DIGESTA LEVELING SYSTEM
    /*
        level 0: 20
        level 1: 40
        level 2: 40
        level 3: 70
        level 4: 130
        level 5: 160
        level 6: 150
        level 7: 180
        level 8: 240
        level 9: 270
        level 10: 270
        level 11: 300
        level 12: 360
        level 13: 390
        level 14: 380
        level 15: 410
        level 16: 470
        level 17: 500
        level 18: 490
        level 19: 520

        level 5  xp  : 300
        level 10 xp  : 1300
        level 15 xp  : 3000
        Total 20 xp  : 5390
    */

    public static int getLevelXp(int level) {
        return (int) Math.round(0.04 * (level ^ 3) + 0.8 * (level ^ 2) + 1.7 * level) * 10;
    }

    public static int getLevelFromXp(int xp) {
        for (int i = 0; i < GamificationRules.MAX_LEVEL; i++) {
            int levelXp = GamificationRules.getLevelXp(i);
            xp -= levelXp;
            Log.d(TAG, "getLevelFromXp: " + xp);
            if (xp <= 0) {
                return i;
            }
        }
        return -1;
    }

    public static final String getTitleFromLevel(int level) {
        if (level <= EXPLORER_LEVEL) {
            return "Explorer";
        } else if (level <= ADVENTURER_LEVEL) {
            return "Adventurer";
        } else if (level <= ADVANCED_TRAVELLER_LEVEL) {
            return "Advanced Traveler";
        } else if (level <= PHAROS_LEVEL) {
            return "Pharos";
        } else if (level <= ANUBIS_LEVEL) {
            return "Anusbis";
        } else {
            return "Off the Grid";
        }
    }
}
