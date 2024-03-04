package com.studentsmartcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import Authentication.LoginPage;

public class profile extends AppCompatActivity {

    private TextView fullNameProfileUser, emailProfileUser, phoneProfileUser;

    private FirebaseAuth fAuth;
    private ImageButton homeButton, logOutUser;
    private ImageView backPressed;
    private FirebaseFirestore fbStore;

    private DocumentReference dbReference;
    private String userID;

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

        //method
        dbSnapshot();

        //Listener
        logOutUser.setOnClickListener(v -> logOutUserProfile());
        homeButton.setOnClickListener(v -> homeDashboard());
        backPressed.setOnClickListener(v -> backSession());

        }

    private void backSession() {
        finish();
    }



    private void initializerView(){
        fullNameProfileUser = findViewById(R.id.fullNameProfile);
        emailProfileUser = findViewById(R.id.emailProfile);
        phoneProfileUser = findViewById(R.id.phoneNumberProfile);
        logOutUser = findViewById(R.id.logoutProfile);
        homeButton = findViewById(R.id.homeButtonProfile);
        backPressed = findViewById(R.id.backNow);

    }


    private void dbSnapshot(){
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        dbReference = fbStore.collection("users").document(userID);
        dbReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    fullNameProfileUser.setText(value.getString("fullNameUser"));
                    emailProfileUser.setText(value.getString("emailUser"));

                }
            }
        });
    }

    private void logOutUserProfile() {
        fAuth.signOut();
        Toast.makeText(profile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Clear session and redirect to loginPage
        sessionManager.logout();
        startActivity(new Intent(getApplicationContext(), LoginPage.class));
        finish();
    }

    //Redirecting to home Dashboard
    private void homeDashboard(){
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();

    }

}