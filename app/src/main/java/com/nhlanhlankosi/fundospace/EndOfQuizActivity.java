package com.nhlanhlankosi.fundospace;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import static com.nhlanhlankosi.fundospace.Common.topicScore;

public class EndOfQuizActivity extends AppCompatActivity {

    private DatabaseReference questionScoreRef;
    private DatabaseReference highScoreRef;
    private TextView txtHighScore;
    private ValueEventListener highScoreRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_quiz);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        questionScoreRef = firebaseDatabase.getReference("Question Score");

        DatabaseReference topicProgressRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("topicProgress");
        DatabaseReference isNewAchievementNotificationShownRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId))
                .child("isNewAchievementNotificationShown");
        DatabaseReference topicScoreRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("topicScore");
        highScoreRef = questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId)).child("highScore");

        txtHighScore = findViewById(R.id.high_score_text);
        TextView numberOfCorrectAnswers = findViewById(R.id.number_of_correct_answers);
        ProgressBar progressBar = findViewById(R.id.done_progress_bar);
        Button doneBtn = findViewById(R.id.done_button);

        showTopicHighScore();

        int correctAnswers;
        int totalQuestions = 40;

        if (getIntent().getExtras() != null) {

            correctAnswers = getIntent().getIntExtra("topicScore", topicScore);

        } else {

            correctAnswers = topicScore;

        }

        float correct = (float) correctAnswers;
        float total = (float) totalQuestions;

        numberOfCorrectAnswers.setText(String.format(Locale.ENGLISH, "PASSED: %d / %d",
                correctAnswers, totalQuestions));

        progressBar.setMax(totalQuestions);

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress",
                0, correctAnswers);
        animation.setDuration(2000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        if (!Common.isTopicDone) {

            if (topicScore != 0) {
                sendScoreToFirebase(topicScore);
            } else {
                sendScoreToFirebase(correctAnswers);
            }

        } else {

            topicProgressRef.setValue((40 + 1));
            topicScoreRef.setValue("0");
            isNewAchievementNotificationShownRef.setValue("false");

        }

        doneBtn.setOnClickListener(v -> {

            startActivity(new Intent(EndOfQuizActivity.this, MainActivity.class));
            finish();

        });

    }

    private void showTopicHighScore() {

        highScoreRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    txtHighScore.setText(String.format(Locale.ENGLISH, "HIGH SCORE: %s",
                            dataSnapshot.getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        highScoreRef.addListenerForSingleValueEvent(highScoreRefListener);

    }

    private void sendScoreToFirebase(int score) {

        questionScoreRef.child(String.format("%s_%s", Common.currentUserId, Common.topicId))
                .setValue(new QuestionScore(
                        Common.currentUsername,
                        Common.currentUserId,
                        String.valueOf(score),
                        "0",
                        Common.topicId,
                        Common.topicName,
                        "true",
                        "false",
                        (Common.questionsList.size() + 1),
                        topicScore,
                        Common.topicLearningArea));

    }

}
