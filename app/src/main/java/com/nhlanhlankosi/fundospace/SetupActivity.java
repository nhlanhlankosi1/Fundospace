package com.nhlanhlankosi.fundospace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    String currentUserId;
    private CircleImageView profileImage;
    private EditText userName, fullName, countryName;
    private Button saveInformationButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference currentUserRef;
    private StorageReference userProfileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        currentUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("profileImages");

        profileImage = findViewById(R.id.setup_profile_image);
        userName = findViewById(R.id.setup_username);
        fullName = findViewById(R.id.setup_full_name);
        countryName = findViewById(R.id.setup_country_name);
        saveInformationButton = findViewById(R.id.setup_information_button);

        loadingBar = new ProgressDialog(this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(SetupActivity.this);

            }
        });

        saveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountSetUpInformation();
            }
        });

        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    if (snapshot.child("profileImage").getValue() != null) {

                        String imageUrl = Objects.requireNonNull(snapshot.child("profileImage").getValue()).toString();

                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.profile)
                                .fit()
                                .centerInside()
                                .into(profileImage);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveAccountSetUpInformation() {

        String username = userName.getText().toString();
        String fullname = fullName.getText().toString();
        String countryname = countryName.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username Cannot Be Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(this, "Full Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(countryname)) {
            Toast.makeText(this, "Country Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Saving information");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("username", username);
            userMap.put("fullName", fullname);
            userMap.put("countryName", countryname);
            userMap.put("status", "Hey there, I am using social media app");
            userMap.put("gender", "none");
            userMap.put("dateOfBirth", "none");

            currentUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    loadingBar.dismiss();

                    if (task.isSuccessful()) {

                        sendUserToMainActivity();

                        Toast.makeText(SetupActivity.this,
                                "Your account is setup successfully!", Toast.LENGTH_LONG).show();

                    } else {

                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(SetupActivity.this,
                                "Error Occurred: " + errorMessage, Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK && result != null) {

                loadingBar.setTitle("Updating Profile Picture");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();

                Uri resultUri = result.getUri();

                final StorageReference imageFilePath = userProfileImageRef.child(currentUserId + ".jpg");

                UploadTask uploadProfilePic = imageFilePath.putFile(resultUri);
                uploadProfilePic.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                loadingBar.dismiss();
                                final String profilePicUrl = uri.toString();

                                currentUserRef.child("profileImage").setValue(profilePicUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SetupActivity.this,
                                                            "Profile Image Link Stored To Firebase Node", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    String errorMessage = task.getException().getMessage();
                                                    Toast.makeText(SetupActivity.this,
                                                            "Error Occurred: " + errorMessage, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetupActivity.this,
                                "Failed To Update Profile Picture, Retry", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                Toast.makeText(SetupActivity.this,
                        "Error Occurred: Image Could Not Be Cropped, Try Again", Toast.LENGTH_SHORT).show();

            }

        }

    }
}