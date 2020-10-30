package com.nhlanhlankosi.fundospace;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView userProfImage;
    private TextView userName, userProfName, userStatus, userCountry, userGender, userDOB;
    private DatabaseReference profileUserRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userProfImage = findViewById(R.id.my_profile_pic);
        userName = findViewById(R.id.my_username);
        userProfName = findViewById(R.id.my_profile_full_name);
        userStatus = findViewById(R.id.my_profile_status);
        userCountry = findViewById(R.id.my_country);
        userGender = findViewById(R.id.my_gender);
        userDOB = findViewById(R.id.my_dob);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String profileImage = snapshot.child("profileImage").getValue().toString();
                    String profileFullName = snapshot.child("fullName").getValue().toString();
                    String profileName = snapshot.child("username").getValue().toString();
                    String profileStatus = snapshot.child("status").getValue().toString();
                    String profileCountry = snapshot.child("countryName").getValue().toString();
                    String profileGender = snapshot.child("gender").getValue().toString();
                    String profileDOB = snapshot.child("dateOfBirth").getValue().toString();

                    Picasso.get()
                            .load(profileImage)
                            .placeholder(R.drawable.profile)
                            .fit()
                            .centerInside()
                            .into(userProfImage);

                    userName.setText("@" + profileName);
                    userProfName.setText(profileFullName);
                    userStatus.setText(profileStatus);
                    userCountry.setText("Country:" + profileCountry);
                    userGender.setText("Gender:" + profileGender);
                    userDOB.setText("DOB:" + profileDOB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}