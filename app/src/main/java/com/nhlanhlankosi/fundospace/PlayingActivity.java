package com.nhlanhlankosi.fundospace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class PlayingActivity extends AppCompatActivity {

    public static final int BTN_COLOR_AND_NEXT_QUESTION_DELAY = 800;
    private LinearLayout playingActivityButtonsContainer;
    private int inGameProgressValue = 0;
    private int index = 0, // shows which questions we on
            topicScore = 0, //tracks Score for comparison with highScore
            numberOfQuestionsAnswered = 0,
            totalNumberOfQuestions = 40;
    private DatabaseReference currentUserRef;
    private DatabaseReference topicProgressRef;
    private DatabaseReference userTimeStampRef;
    private DatabaseReference isNewAchievementNotificationShownRef;
    private DatabaseReference onceOffScoreRef;
    private DatabaseReference topicScoreRef;
    private DatabaseReference highScoreRef;
    private DatabaseReference dailyScoreTimeStampRef;
    private DatabaseReference isCurrentUserLoggedOutRef;
    private DatabaseReference currentUserReferralCoinsRef;
    private ProgressBar answeredQuestionsProgressBar, endOfQuizProgressBar;
    private Button btnOption1;
    private Button btnOption2;
    private Button btnOption3;
    private Button btnOption4;
    private TextView scoreText, highScoreText;
    private TextView numberOfQuestionsAnsweredText;
    private TextView questionText;
    private TextView checkInternetText;
    private ImageView questionImage;
    private Handler handlerDialog = new Handler();
    private Runnable runnableDialog;
    private Handler handlerActivityDelay = new Handler();
    private Runnable runnableActivityDelay;
    private LinearLayout scoresContainer, dividerLine;
    private boolean onBackButtonPressed = false;
    private ValueEventListener topicProgressRefListener;
    private ValueEventListener highScoreRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        scoreText = findViewById(R.id.score_text);
        highScoreText = findViewById(R.id.high_score_text);
        numberOfQuestionsAnsweredText = findViewById(R.id.txt_number_of_questions_answered);
        questionText = findViewById(R.id.question_text);
        scoresContainer = findViewById(R.id.scores_container);
        dividerLine = findViewById(R.id.divider_line);

        questionImage = findViewById(R.id.question_image);

        answeredQuestionsProgressBar = findViewById(R.id.answered_questions_progress_bar);
        endOfQuizProgressBar = findViewById(R.id.end_of_quiz_progress_bar);

        playingActivityButtonsContainer = findViewById(R.id.playing_activity_buttons_container);

        btnOption1 = findViewById(R.id.btn_option_1);
        btnOption2 = findViewById(R.id.btn_option_2);
        btnOption3 = findViewById(R.id.btn_option_3);
        btnOption4 = findViewById(R.id.btn_option_4);

        answeredQuestionsProgressBar.setMax(totalNumberOfQuestions);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference questionScoreRef = firebaseDatabase.getReference("Question Score");

        onceOffScoreRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("onceOffScore");

        topicScoreRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("topicScore");

        topicProgressRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("topicProgress");

        isNewAchievementNotificationShownRef = questionScoreRef
                .child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("isNewAchievementNotificationShown");

        highScoreRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("highScore");

        Log.e("questionsList.size()", String.valueOf(Common.questionsList.size()));

       /* if (btnOption1.getText().toString().equals("")) {

            hideScreenContents();

        }*/

        try {

            if (Common.questionsList.size() != 0 && index < 20) {

                playGame();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void playGame() {

        btnOption1.setOnClickListener(v -> {

            //mark questions as attempted
            numberOfQuestionsAnswered++;
            inGameProgressValue++;

            sendProgressToFirebase(inGameProgressValue);

            makeButtonsUnclickable();

            if (index < 40) {

                //if option 1 is the correct answer
                if (btnOption1.getText().equals(Common.questionsList.get(index).getAnswer())) {

                    btnOption1.setBackground(getResources().getDrawable(R.drawable.green_btn_background));

                    topicScore++;

                    updateTopicScore(topicScore);

                    updateHighScore(topicScore);

                    updateScoreOnFirebase(topicScore);

                    scoreText.setText(String.format(Locale.ENGLISH, "Current Score: %d", topicScore));

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        btnOption1.setBackground(getResources().getDrawable(R.drawable.btn_background));

                        makeButtonsClickable();

                        showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                } else {

                    //if option1 is wrong
                    btnOption1.setBackground(getResources().getDrawable(R.drawable.red_btn_background));

                    //find the correct answer and make it green
                    if (btnOption2.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption2.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption3.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption3.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption4.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption4.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    }

                    //Replace the colors and update the questions
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        restoreButtonBackgrounds();

                        makeButtonsClickable();

                        final Question currentQuestion = Common.questionsList.get(index);

                        showExplanationDialogue(currentQuestion.getExplanation(), currentQuestion.getExplanationImgUrl());

                        showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                }

            } else {

                hideScreenContents();

            }

        });

        btnOption2.setOnClickListener(v -> {

            numberOfQuestionsAnswered++;
            inGameProgressValue++;

            sendProgressToFirebase(inGameProgressValue);

            makeButtonsUnclickable();

            if (index < 40) {

                if (btnOption2.getText().equals(Common.questionsList.get(index).getAnswer())) {

                    btnOption2.setBackground(getResources().getDrawable(R.drawable.green_btn_background));

                    topicScore++;

                    updateTopicScore(topicScore);

                    updateHighScore(topicScore);

                    updateScoreOnFirebase(topicScore);

                    scoreText.setText(String.format(Locale.ENGLISH, "Current Score: %d", topicScore));

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        btnOption2.setBackground(getResources().getDrawable(R.drawable.btn_background));

                        makeButtonsClickable();

                        showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                } else {

                    btnOption2.setBackground(getResources().getDrawable(R.drawable.red_btn_background));

                    if (btnOption1.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption1.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption3.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption3.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption4.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption4.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        restoreButtonBackgrounds();

                        makeButtonsClickable();

                        final Question currentQuestion = Common.questionsList.get(index);

                        showExplanationDialogue(currentQuestion.getExplanation(), currentQuestion.getExplanationImgUrl());

                       showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                }

            } else {

                hideScreenContents();

            }

        });

        btnOption3.setOnClickListener(v -> {

            numberOfQuestionsAnswered++;
            inGameProgressValue++;

            sendProgressToFirebase(inGameProgressValue);

            makeButtonsUnclickable();

            if (index < 40) {

                if (btnOption3.getText().equals(Common.questionsList.get(index).getAnswer())) {

                    btnOption3.setBackground(getResources().getDrawable(R.drawable.green_btn_background));

                    topicScore++;

                    updateTopicScore(topicScore);

                    updateHighScore(topicScore);

                    updateScoreOnFirebase(topicScore);

                    scoreText.setText(String.format(Locale.ENGLISH, "Current Score: %d", topicScore));

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        btnOption3.setBackground(getResources().getDrawable(R.drawable.btn_background));

                        makeButtonsClickable();

                       showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                } else {

                    btnOption3.setBackground(getResources().getDrawable(R.drawable.red_btn_background));

                    if (btnOption1.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption1.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption2.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption2.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption4.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption4.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        restoreButtonBackgrounds();

                        makeButtonsClickable();

                        final Question currentQuestion = Common.questionsList.get(index);

                        showExplanationDialogue(currentQuestion.getExplanation(), currentQuestion.getExplanationImgUrl());

                        showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                }

            } else {

                hideScreenContents();

            }

        });

        btnOption4.setOnClickListener(v -> {

            numberOfQuestionsAnswered++;
            inGameProgressValue++;

            sendProgressToFirebase(inGameProgressValue);

            makeButtonsUnclickable();

            if (index < 40) {

                if (btnOption4.getText().equals(Common.questionsList.get(index).getAnswer())) {

                    btnOption4.setBackground(getResources().getDrawable(R.drawable.green_btn_background));

                    topicScore++;

                    updateTopicScore(topicScore);

                    updateHighScore(topicScore);

                    updateScoreOnFirebase(topicScore);

                    scoreText.setText(String.format(Locale.ENGLISH, "Current Score: %d", topicScore));

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        btnOption4.setBackground(getResources().getDrawable(R.drawable.btn_background));

                        makeButtonsClickable();

                        showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                } else {

                    btnOption4.setBackground(getResources().getDrawable(R.drawable.red_btn_background));

                    if (btnOption1.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption1.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption2.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption2.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    } else if (btnOption3.getText().equals(Common.questionsList.get(index).getAnswer())) {
                        btnOption3.setBackground(getResources().getDrawable(R.drawable.green_btn_background));
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        restoreButtonBackgrounds();

                        makeButtonsClickable();

                        final Question currentQuestion = Common.questionsList.get(index);

                        showExplanationDialogue(currentQuestion.getExplanation(), currentQuestion.getExplanationImgUrl());

                        showQuestion(++index);

                    }, BTN_COLOR_AND_NEXT_QUESTION_DELAY);

                }

            } else {

                hideScreenContents();

            }

        });

    }

    private void restoreButtonBackgrounds() {

        btnOption1.setBackground(getResources().getDrawable(R.drawable.btn_background));
        btnOption2.setBackground(getResources().getDrawable(R.drawable.btn_background));
        btnOption3.setBackground(getResources().getDrawable(R.drawable.btn_background));
        btnOption4.setBackground(getResources().getDrawable(R.drawable.btn_background));

    }

    private void makeButtonsClickable() {

        btnOption1.setClickable(true);
        btnOption2.setClickable(true);
        btnOption3.setClickable(true);
        btnOption4.setClickable(true);

    }

    private void makeButtonsUnclickable() {

        btnOption1.setClickable(false);
        btnOption2.setClickable(false);
        btnOption3.setClickable(false);
        btnOption4.setClickable(false);

    }

    private void updateTopicScore(int topicScore) {
        topicScoreRef.setValue(String.valueOf(topicScore));
    }

    private void updateHighScore(int currentScore) {

        highScoreRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    int highScore = Integer.parseInt(dataSnapshot.getValue().toString());

                    highScoreText.setText(String.format(Locale.ENGLISH, "High Score: %d", highScore));

                    if (currentScore > highScore) {

                        setNewHighScore(currentScore);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        highScoreRef.addValueEventListener(highScoreRefListener);

    }

    private void setNewHighScore(int currentScore) {
        highScoreRef.setValue(currentScore);
    }

    private void updateScoreOnFirebase(int score) {

        onceOffScoreRef.setValue(String.valueOf(score));

    }

    private void showQuestion(int index) {

                showScreenContents();

                if (index < totalNumberOfQuestions) {

                    answeredQuestionsProgressBar.setProgress(numberOfQuestionsAnswered);

                    numberOfQuestionsAnsweredText.setText(String.format(Locale.ENGLISH, "%d / %d%s ANSWERED",
                            numberOfQuestionsAnswered, totalNumberOfQuestions, (numberOfQuestionsAnswered == 1) ? " QUESTION" : " QUESTIONS"));

                    //if the question has an image
                    if (!(Common.questionsList.get(index).getQuestionImgUrl().equals(""))) {

                        try {

                            questionImage.setVisibility(View.VISIBLE);

                            Picasso.get()
                                    .load(Common.questionsList.get(index).getQuestionImgUrl())
                                    .fit()
                                    .centerInside()
                                    .into(questionImage);

                            questionText.setText(Common.questionsList.get(index).getQuestionText());

                        } catch (Exception e) {

                            questionImage.setVisibility(View.GONE);
                            questionText.setText(Common.questionsList.get(index).getQuestionText());

                        }

                    } else {

                        questionImage.setVisibility(View.GONE);
                        questionText.setText(Common.questionsList.get(index).getQuestionText());

                    }

                    btnOption1.setText(Common.questionsList.get(index).getOption1());
                    btnOption2.setText(Common.questionsList.get(index).getOption2());
                    btnOption3.setText(Common.questionsList.get(index).getOption3());
                    btnOption4.setText(Common.questionsList.get(index).getOption4());

                } else {

                    //If it is the final question
                    numberOfQuestionsAnsweredText.setText(String.format(Locale.ENGLISH, "%d / %d%s ANSWERED",
                            totalNumberOfQuestions, totalNumberOfQuestions, (numberOfQuestionsAnswered == 1) ? " QUESTION" : " QUESTIONS"));

                    loadTopicScore();
                    loadTopicProgress();

                    makeButtonsUnclickable();
                    openEndOfQuizActivity();

                }

    }

    private void openEndOfQuizActivity() {

        Intent endOfQuizActivity = new Intent(PlayingActivity.this, EndOfQuizActivity.class);
        endOfQuizActivity.putExtra("topicScore", topicScore);
        startActivity(endOfQuizActivity);
        finish();

    }

    private void showScreenContents() {

        answeredQuestionsProgressBar.setVisibility(View.VISIBLE);
        numberOfQuestionsAnsweredText.setVisibility(View.VISIBLE);
        scoresContainer.setVisibility(View.VISIBLE);
        dividerLine.setVisibility(View.VISIBLE);
        questionText.setVisibility(View.VISIBLE);
        playingActivityButtonsContainer.setVisibility(View.VISIBLE);

    }

    private void loadTopicProgress() {

        if (topicProgressRef != null) {

            topicProgressRefListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        int topicProgress = Integer.parseInt(dataSnapshot.getValue().toString());

                        Common.questionScore.setTopicProgress(topicProgress);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            topicProgressRef.addValueEventListener(topicProgressRefListener);

        }

    }

    private void sendProgressToFirebase(int inGameProgressValue) {
        topicProgressRef.setValue(inGameProgressValue);
    }

    private void showExplanationDialogue(String explanation, String explanationImgUrl) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayingActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Explanation");

        LayoutInflater inflater = this.getLayoutInflater();
        View explanationDialogue = inflater.inflate(R.layout.explanation_dialogue_layout, null);

        ImageView explanationImage = explanationDialogue.findViewById(R.id.explanation_dialogue_image);
        TextView explanationText = explanationDialogue.findViewById(R.id.explanation_dialogue_text);

        alertDialog.setView(explanationDialogue);
        alertDialog.setIcon(R.drawable.ic_explanation);

        if (explanation != null) {
            explanationText.setText(explanation);
        }

        if (!explanationImgUrl.equals("")) {

            try {

                Picasso.get()
                        .load(explanationImgUrl)
                        .fit()
                        .centerInside()
                        .into(explanationImage);

            } catch (Exception e) {
                explanationImage.setVisibility(View.GONE);
            }

        } else {

            explanationImage.setVisibility(View.GONE);

        }

        alertDialog.setPositiveButton("OK, GOT IT", (dialog, which) -> {

            //if all questions have been answered
            if (index == Common.questionsList.size()) {

                openEndOfQuizActivity();

            }

        });

        try {

            alertDialog.show();

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    private void restoreActivityState() {

        if (topicProgressRef != null) {

            topicProgressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        String topicProgress = dataSnapshot.getValue().toString();

                        if (Integer.parseInt(topicProgress) == (Common.questionsList.size() + 1)) {

                            inGameProgressValue = 0;

                        } else {

                            inGameProgressValue = Integer.parseInt(topicProgress);

                        }

                        index = inGameProgressValue;

                        numberOfQuestionsAnswered = index;

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {

            Common.questionScore.setTopicProgress(0);

        }

    }

    private void loadTopicScore() {

        if (topicScoreRef != null) {

            topicScoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                       Common.topicScore = Integer.parseInt(dataSnapshot.getValue().toString());

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void hideScreenContents() {

        answeredQuestionsProgressBar.setVisibility(View.GONE);
        numberOfQuestionsAnsweredText.setVisibility(View.GONE);
        scoresContainer.setVisibility(View.GONE);
        dividerLine.setVisibility(View.GONE);
        questionImage.setVisibility(View.GONE);
        questionText.setVisibility(View.GONE);
        playingActivityButtonsContainer.setVisibility(View.GONE);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (handlerDialog != null) {
            handlerDialog.removeCallbacks(runnableDialog);
        }

        if (handlerActivityDelay != null) {
            handlerActivityDelay.removeCallbacks(runnableActivityDelay);
        }

        sendProgressToFirebase(inGameProgressValue);

    }

    @Override
    protected void onResume() {
        super.onResume();

        totalNumberOfQuestions = Common.questionsList.size();

        try {

            topicScore = Integer.parseInt(Common.questionScore.getTopicScore());
            scoreText.setText(String.format(Locale.ENGLISH, "Current Score: %d", topicScore));
            updateHighScore(topicScore);

        } catch (NumberFormatException e) {

            e.printStackTrace();

        }

        restoreActivityState();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onBackButtonPressed = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (topicProgressRef != null && topicProgressRefListener != null) {
            topicProgressRef.removeEventListener(topicProgressRefListener);
        }

        if (highScoreRef != null && highScoreRefListener != null) {
            highScoreRef.removeEventListener(highScoreRefListener);
        }

    }

}