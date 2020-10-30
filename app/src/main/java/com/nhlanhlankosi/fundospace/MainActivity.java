package com.nhlanhlankosi.fundospace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static final String SUBJECT_ID = "com.nhlanhlankosi.fundospace.SUBJECT_ID";
    private String currentUserId;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CircleImageView navProfileImage;
    private TextView navUsername;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private Button biologyButton, chemistryButton, physicsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Common.currentUserId = currentUserId;

        drawerLayout = findViewById(R.id.drawable_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close);

        navigationView = findViewById(R.id.navigation_view);

        toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        navProfileImage = navView.findViewById(R.id.nav_profile_image);
        navUsername = navView.findViewById(R.id.nav_user_full_name);

        usersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    if (snapshot.hasChild("fullName")) {
                        String fullName = snapshot.child("fullName").getValue().toString();
                        Common.currentUsername = fullName;
                        navUsername.setText(fullName);
                    }

                    if (snapshot.child("profileImage").getValue() != null) {

                        String profileImageUrl = snapshot.child("profileImage").getValue().toString();
                        Picasso.get()
                                .load(profileImageUrl)
                                .placeholder(R.drawable.profile)
                                .fit()
                                .centerInside()
                                .into(navProfileImage);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                return false;
            }
        });

        biologyButton = findViewById(R.id.biology_button);
        chemistryButton = findViewById(R.id.chemistry_button);
        physicsButton = findViewById(R.id.physics_button);

        biologyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTopicsActivity(1);
            }
        });

        chemistryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTopicsActivity(2);
            }
        });

        physicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTopicsActivity(3);
            }
        });

    }

    private void openTopicsActivity(int subjectId) {

        Intent loginIntent = new Intent(MainActivity.this, TopicsActivity.class);
        loginIntent.putExtra(SUBJECT_ID, subjectId);
        startActivity(loginIntent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            sendUserToLoginActivity();
        } else {
            checkUserExistence();
        }

    }

    private void checkUserExistence() {

        final String currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(currentUserId)) {
                    sendUserToSetUpActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendUserToSetUpActivity() {

        Intent setUpIntent = new Intent(MainActivity.this, SetupActivity.class);
        setUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setUpIntent);
        finish();

    }

    private void sendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

    }

    private void userMenuSelector(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_profile:
                sendUserToProfileActivity();
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                sendUserToLoginActivity();
                break;

        }

    }

    private void sendUserToProfileActivity() {

        Intent loginIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(loginIntent);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}