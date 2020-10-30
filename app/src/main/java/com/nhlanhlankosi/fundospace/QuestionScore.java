package com.nhlanhlankosi.fundospace;

public class QuestionScore {

    private String userName;
    private String currentUserId;
    private String onceOffScore;
    private String topicScore;
    private String topicId;
    private String topicName;
    private String isTopicDone;
    private String isNewAchievementNotificationShown;
    private int topicProgress;
    private int highScore;
    private String topicLearningArea;

    public QuestionScore() {
    }

    public QuestionScore(String userName, String currentUserId, String onceOffScore,
                         String topicScore, String topicId, String topicName,
                         String isTopicDone, String isNewAchievementNotificationShown,
                         int topicProgress, int highScore, String topicLearningArea) {

        this.userName = userName;
        this.currentUserId = currentUserId;
        this.onceOffScore = onceOffScore;
        this.topicScore = topicScore;
        this.topicId = topicId;
        this.topicName = topicName;
        this.isTopicDone = isTopicDone;
        this.isNewAchievementNotificationShown = isNewAchievementNotificationShown;
        this.topicProgress = topicProgress;
        this.highScore = highScore;
        this.topicLearningArea = topicLearningArea;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getOnceOffScore() {
        return onceOffScore;
    }

    public void setOnceOffScore(String onceOffScore) {
        this.onceOffScore = onceOffScore;
    }

    public String getTopicScore() {
        return topicScore;
    }

    public void setTopicScore(String topicScore) {
        this.topicScore = topicScore;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getIsTopicDone() {
        return isTopicDone;
    }

    public void setIsTopicDone(String isTopicDone) {
        this.isTopicDone = isTopicDone;
    }

    public String getIsNewAchievementNotificationShown() {
        return isNewAchievementNotificationShown;
    }

    public void setIsNewAchievementNotificationShown(String isNewAchievementNotificationShown) {
        this.isNewAchievementNotificationShown = isNewAchievementNotificationShown;
    }

    public int getTopicProgress() {
        return topicProgress;
    }

    public void setTopicProgress(int topicProgress) {
        this.topicProgress = topicProgress;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public String getTopicLearningArea() {
        return topicLearningArea;
    }

    public void setTopicLearningArea(String topicLearningArea) {
        this.topicLearningArea = topicLearningArea;
    }

}

