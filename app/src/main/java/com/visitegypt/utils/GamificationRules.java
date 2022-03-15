package com.visitegypt.utils;

public class GamificationRules {
    public static final int CONFIRM_LOCATION_CIRCLE_RADIUS = 500;
    public static final int MAX_LEVEL = 20;

    public static final int BRONZE_BADGE_XP = 30;
    public static final int SILVER_BADGE_XP = 70;
    public static final int GOLD_BADGE_XP = 300;

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

    public static long getLevelXp(int level) {
        return Math.round(0.04 * (level ^ 3) + 0.8 * (level ^ 2) + 1.7 * level);
    }
}
