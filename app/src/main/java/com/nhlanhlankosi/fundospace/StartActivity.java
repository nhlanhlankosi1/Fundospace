package com.nhlanhlankosi.fundospace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.nhlanhlankosi.fundospace.BiologyTopicsFragment.TOPIC_ID;
import static com.nhlanhlankosi.fundospace.MainActivity.SUBJECT_ID;

public class StartActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference questionsRef, questionScoreRef, currentUserQuestionScoreRef;
    private Button btnStartRevision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firebaseDatabase = FirebaseDatabase.getInstance();
        questionsRef = firebaseDatabase.getReference("Questions");
        questionScoreRef = firebaseDatabase.getReference("Question Score");

        currentUserQuestionScoreRef = questionScoreRef.child(String.format("%s_%s",
                Common.currentUserId, Common.topicId));

        createQuestionScoreRefIfUnAvailable();

        btnStartRevision = findViewById(R.id.start_revision_button);

        String topicIdExtra = getIntent().getStringExtra(TOPIC_ID);

        String topicId = "0" + topicIdExtra;

        Log.e("topicId", topicId);

        loadQuestions(topicId);

        btnStartRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, PlayingActivity.class));
                finish();
            }
        });

    }

    private void loadQuestions(@NonNull String topicId) {

        if (Common.questionsList.size() > 0) {
            Common.questionsList.clear();
        }

        questionsRef.orderByChild("topicId").equalTo(topicId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Question question = postSnapshot.getValue(Question.class);
                            Common.questionsList.add(question);

                            Log.e("Inside Query", "!!!!!!");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void createQuestionScoreRefIfUnAvailable() {

        questionScoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(String.format("%s_%s", Common.currentUserId,
                        Common.topicId))) {

                    currentUserQuestionScoreRef.setValue(new QuestionScore(
                            Common.currentUsername,
                            Common.currentUserId,
                            "0",
                            "0",
                            Common.topicId,
                            Common.topicName,
                            "false",
                            "false",
                            0, 0, Common.topicLearningArea));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}