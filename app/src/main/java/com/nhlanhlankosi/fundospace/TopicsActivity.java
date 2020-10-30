package com.nhlanhlankosi.fundospace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nhlanhlankosi.fundospace.MainActivity.SUBJECT_ID;

public class TopicsActivity extends AppCompatActivity {

    private String currentUserId;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CircleImageView navProfileImage;
    private TextView navUsername;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        drawerLayout = findViewById(R.id.drawable_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(TopicsActivity.this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close);

        navigationView = findViewById(R.id.navigation_view);

        toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Topics");
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


        int subjectId = getIntent().getIntExtra(SUBJECT_ID, 1);

        assert toolbar != null;

        switch (subjectId) {

            case 1:
                switchPageTo(new BiologyTopicsFragment());
                Common.topicLearningArea = "Biology";
                Common.topicName = "Biology";
                break;

            case 2:
                switchPageTo(new PhysicsTopicsFragment());
                Common.topicLearningArea = "Physics";
                Common.topicName = "Physics";
                break;

            case 3:
                switchPageTo(new ChemistryTopicsFragment());
                Common.topicLearningArea = "Chemistry";
                Common.topicName = "Chemistry";
                break;

        }

    }

    private void sendUserToLoginActivity() {

        Intent loginIntent = new Intent(TopicsActivity.this, LoginActivity.class);
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

        Intent loginIntent = new Intent(TopicsActivity.this, ProfileActivity.class);
        startActivity(loginIntent);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    // the following method handles the Fragment transactions in this activity
    private void switchPageTo(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activity_main_fragmentContainer, fragment);
        transaction.commit();

    }

    /* The following onBackPressed() method is Overridden so that we don't leave the activity when
       we press the back button on our devices but close the navigation drawer, only if it's open. */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}