package com.studentsmartcare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import Authentication.LoginPage;

public class profile extends AppCompatActivity {
    private static final int PICK_IMAGE =1 ;
    Context context;

    private TextView fullNameProfileUser, emailProfileUser, phoneProfileUser;

    private FirebaseAuth fAuth;
    private ImageButton homeButton, logOutUser;
    private RoundedImageView  imageProfile;
    private ImageView backPressed, editImage, editProfileIcon;
    private FirebaseFirestore fbStore;
    private StorageReference storageReference;

    private Uri imageUri;



    private DocumentReference dbReference;
    private String userID;
    private FirebaseUser currentUser;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        initializerView();



        //Instances
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();


        //method
        documentSnapshotRetrieve();

        //Listener
        logOutUser.setOnClickListener(v -> logOutUserProfile());
        homeButton.setOnClickListener(v -> homeDashboard());
        backPressed.setOnClickListener(v -> backSession());
       editImage.setOnClickListener(v -> profileImageUpload());
        retrieveProfileImage();
        editProfileIcon.setOnClickListener(v -> popUptoEditprofile(v));


        }

    private void popUptoEditprofile(View v) {
        Intent intent = new Intent(v.getContext(), editProfile.class);
        intent.putExtra("fullName", fullNameProfileUser.getText().toString());
        intent.putExtra("email", emailProfileUser.getText().toString());
        intent.putExtra("phoneNumber", phoneProfileUser.getText().toString());
        startActivity(intent);


    }

    //Image upload in profile
    private void profileImageUpload() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    //Getting request to upload image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageProfile);
            Uri uriImage = data.getData();

            uploadImageFirebase(uriImage);
        }
    }

    //upload image to firebase
    private void uploadImageFirebase(Uri uriImage) {
        final StorageReference upFileReference = storageReference.child("usersImage/" +fAuth.getCurrentUser().getUid()+"/profileImage");
     upFileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Toast.makeText(profile.this,"Image Uploaded", Toast.LENGTH_SHORT).show();

    }
}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this,"Failed to Upload", Toast.LENGTH_SHORT).show();

            }
        });




    }


    //retrieving image from firestore
    private  void retrieveProfileImage(){
        StorageReference pFileReference = storageReference.child("usersImage/" +fAuth.getCurrentUser().getUid()+"/profileImage");

        pFileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri)
                                .fit()
                                .centerCrop()
                                .into(imageProfile);

                    }
                });

    }

    private void backSession() {
        finish();
    }



    private void initializerView(){
        fullNameProfileUser = findViewById(R.id.fullNameProfile);
        emailProfileUser = findViewById(R.id.emailProfile);
        logOutUser = findViewById(R.id.logoutProfile);
        homeButton = findViewById(R.id.homeButtonProfile);
        backPressed = findViewById(R.id.backNow);
        phoneProfileUser = findViewById(R.id.phoneNumberProfile);
        editImage = findViewById(R.id.editIcon);
        imageProfile = findViewById(R.id.imageProfile);
        editProfileIcon = findViewById(R.id.editProfileIcon);


    }


    private void documentSnapshotRetrieve() {
        if (currentUser != null) {
            userID = currentUser.getUid();
            dbReference = fbStore.collection("users").document(userID);

            dbReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        // Handle the error appropriately, such as logging or displaying a message
                        Log.e(TAG, "Error fetching document", error);
                        return;
                    }

                    if (value != null) {
                        String fullName = value.getString("fullNameUser");
                        String emailId = value.getString("emailUser");
                        String phoneNo = value.getString("phoneNumber");
                        fullNameProfileUser.setText(fullName);
                        emailProfileUser.setText(emailId);
                        phoneProfileUser.setText(phoneNo);
                    } else {
                        fullNameProfileUser.setText("N/A");
                        emailProfileUser.setText("N/A");
                        phoneProfileUser.setText("N/A");
                    }
                }
            });
        } else {

            Log.e(TAG, "No authenticated user found");
        }
    }



    private void logOutUserProfile() {
        fAuth.signOut();
        Toast.makeText(profile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Clear session and redirect to loginPage

        sessionManager.logout();
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //Redirecting to home Dashboard
    private void homeDashboard(){
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();

    }

}