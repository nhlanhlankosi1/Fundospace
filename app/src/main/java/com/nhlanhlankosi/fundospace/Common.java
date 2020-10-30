package com.nhlanhlankosi.fundospace;

import java.util.ArrayList;

public class Common {

    public static final int BTN_COLOR_CHANGE_DELAY = 2000;
    public static String currentUserId, topicId, topicName, topicLearningArea, whoForfeitedGame, referrerPhoneNumber, currentUsername;
    public static String assetImageUrl, assetName, assetDescription, assetSizeInMB, assetUrl, assetStorageName;

    public static ArrayList<Question> questionsList = new ArrayList<>(20);
    public static QuestionScore questionScore = new QuestionScore();
    public static int topicScore = 0;
    public static boolean isTopicDone = false;
    public static boolean isNewAchievementNotificationShown = false;
    public static boolean isUserJustLoggedIn = true;
    public static boolean isUserJustStartedPlaying = true;

    //private constructor to prevent instances of this class

    private Common() {
    }


}
