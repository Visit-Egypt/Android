package com.visitegypt.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.google.gson.Gson;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.UserTitle;

import java.util.ArrayList;
import java.util.List;

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
    public static final int EXPLORER_LEVEL = 1;
    public static final int ADVENTURER_LEVEL = 4;
    public static final int ADVANCED_TRAVELLER_LEVEL = 9;
    public static final int PHAROS_LEVEL = 15;
    public static final int ANUBIS_LEVEL = MAX_LEVEL - 1;
    public static final int THOTH = MAX_LEVEL + 5;

    public static final int[] ALL_LEVELS = {EXPLORER_LEVEL,
            ADVENTURER_LEVEL,
            ADVANCED_TRAVELLER_LEVEL,
            PHAROS_LEVEL,
            ANUBIS_LEVEL};

    public static final String[] ALL_TITLES = {
            "Explorer",
            "Adventurer",
            "Traveler",
            "Globetrotter",
            "Anubis",
            "Thoth"
    };

    private static final String[] BADGE_TASKS_TITLES = {
            "review the place",
            "post a post",
            "ask Anubis about the artifacts",
            "ask Anubis about the place",
            "visit the place",
            "explore the artifacts"
    };


    public static final int BRONZE_BADGE_XP = 30;
    public static final int SILVER_BADGE_XP = 70;
    public static final int GOLD_BADGE_XP = 300;
    public static final String FIRST_SIGN_IN_BADGE_ID = "623c9aa3de3a6b3d7c407f63";

    public static final int EXPLORE_XP = 25;

    private static final String TAG = "gamification rules";

    public static int getLevelXp(int level) {
        float boom = Math.round(3 * Math.pow(level - 1, 1.5) * 10);
        return 5 * Math.round(boom / 5);
    }

    public static int getLevelFromXp(int xp) {

        for (int i = 1; i < GamificationRules.THOTH; i++) {
            if (getLevelXp(i + 1) > xp) {
                return i;
            }
        }
        return THOTH;
    }

    // 300 XP
    public static int getRemainingXPToNextLevel(int xp) {
        for (int i = 1; i < 30; i++) {
            if (getLevelXp(i + 1) > xp) {
                return getLevelXp(i + 1) - xp;
            }
        }
        return 0;
    }

    public static String getTitleFromLevel(int level) {
        if (level < ADVENTURER_LEVEL) {
            return ALL_TITLES[0];
        } else if (level < ADVANCED_TRAVELLER_LEVEL) {
            return ALL_TITLES[1];
        } else if (level < PHAROS_LEVEL) {
            return ALL_TITLES[2];
        } else if (level < ANUBIS_LEVEL) {
            return ALL_TITLES[3];
        } else if (level < THOTH) {
            return ALL_TITLES[4];
        } else {
            return ALL_TITLES[5];
        }
    }

    public static void mergeTwoBadges(List<Badge> badges, List<Badge> userBadges) {
        for (Badge fullBadge : badges) {
            for (Badge userBadge : userBadges) {
                if (fullBadge.getId().equals(userBadge.getId())) {
                    MergeObjects.MergeTwoObjects.merge(fullBadge, userBadge);
                    for (BadgeTask badgeTask : fullBadge.getBadgeTasks()) {
                        for (BadgeTask userBadgeTask : userBadge.getBadgeTasks()) {
                            if (badgeTask.getTaskTitle().equals(userBadgeTask.getTaskTitle())) {
                                MergeObjects.MergeTwoObjects.merge(badgeTask, userBadgeTask);
                            }
                        }
                    }

                }
            }
        }
    }

    public static void mergeTwoPlaceActivities(List<PlaceActivity> placeActivities,
                                               List<PlaceActivity> userPlaceActivities) {
        for (PlaceActivity placeActivity : placeActivities) {
            for (PlaceActivity userPlaceActivity : userPlaceActivities) {
                if (placeActivity.getId().equals(userPlaceActivity.getId())) {
                    MergeObjects.MergeTwoObjects.merge(placeActivity, userPlaceActivity);
                }
            }
        }
    }

    public static Badge fullBadgeToBadge(FullBadge fullBadge) {
        Badge badge = fullBadge.getBadge();
        badge.setPinned(fullBadge.isPinned());
        badge.setProgress(fullBadge.getProgress());
        badge.setOwned(fullBadge.isOwned());
        Log.d(TAG, "fullBadgeToBadge: " + new Gson().toJson(fullBadge));
        for (BadgeTask badgeTask : badge.getBadgeTasks()) {
            for (BadgeTask fullBadgeTask : fullBadge.getBadgeTasks()) {
                if (badgeTask.getTaskTitle().equals(fullBadgeTask.getTaskTitle())) {
                    badgeTask.setProgress(fullBadgeTask.getProgress());
                }
            }
        }
        return badge;
    }

    public static ArrayList<UserTitle> getAllUserTitles(int userLevel) {
        ArrayList<UserTitle> userTitles = new ArrayList<>();
        for (Integer level : ALL_LEVELS) {
            if (userLevel >= level) {
                userTitles.add(new UserTitle(level, getTitleFromLevel(level), true));
            } else {
                userTitles.add(new UserTitle(level, getTitleFromLevel(level)));
            }
        }
        return userTitles;
    }

    public static Drawable getProfileFrameDrawable(Context context, String title) {
        if (title.equals(GamificationRules.ALL_TITLES[0])) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.rank1, null);
        } else if (title.equals(GamificationRules.ALL_TITLES[1])) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.rank2, null);
        } else if (title.equals(GamificationRules.ALL_TITLES[2])) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.rank3, null);
        } else if (title.equals(GamificationRules.ALL_TITLES[3])) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.rank4, null);
        } else if (title.equals(GamificationRules.ALL_TITLES[4])) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.rank5, null);
        } else if (title.equals(GamificationRules.ALL_TITLES[5])) {
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.rank6, null);
        }
        return null;
    }

    public static Drawable getBadgeTaskDrawable(Context context, String badgeTaskTitle) {
        if (badgeTaskTitle.equals(BADGE_TASKS_TITLES[0])) {
            // REVIEW
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.badge_task_review, null);
        } else if (badgeTaskTitle.equals(BADGE_TASKS_TITLES[1])) {
            // POST
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.badge_task_post, null);
        } else if (badgeTaskTitle.equals(BADGE_TASKS_TITLES[2])) {
            // CHATBOT PLACES
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.badge_ask_insights, null);
        } else if (badgeTaskTitle.equals(BADGE_TASKS_TITLES[3])) {
            // CHATBOT ARTIFACTS
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.badge_task_artifacts, null);
        } else if (badgeTaskTitle.equals(BADGE_TASKS_TITLES[4])) {
            // VISIT
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.badge_task_visit, null);
        } else if (badgeTaskTitle.equals(BADGE_TASKS_TITLES[5])) {
            // EXPLORE
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.badge_task_explore, null);
        }
        return null;
    }
}
