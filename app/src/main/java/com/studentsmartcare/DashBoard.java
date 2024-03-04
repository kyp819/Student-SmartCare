package com.studentsmartcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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

import BuySell.BuysellRecyclerActivity;
import CarCardView.CarRecyclerActivity;
import Authentication.LoginPage;
import HomeCardView.HomeRecyclerActivity;

public class DashBoard extends AppCompatActivity {
    private ImageView carTapped, homeTapped, buySellTapped, busTapped;
    private FirebaseAuth fAuth;
    private DocumentReference dbReference;
    private FirebaseFirestore fbStore;
    private TextView profileName;

    private CardView profileTapped;
    private ImageButton logOut;

    private String userID;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        initializeViews();
        firebaseInstances();
        documentSnapshotRetrieve();
        setClickListener();


    }


    //Method for the Initialization the variables reference to register-xml
    private void initializeViews() {
        logOut = findViewById(R.id.logout);
        carTapped = findViewById(R.id.carTappedView);
        homeTapped = findViewById(R.id.homeTappedView);
        profileName = findViewById(R.id.greetingTextView);
        profileTapped = findViewById(R.id.profileImageView);
        buySellTapped = findViewById(R.id.buySellView);
        busTapped = findViewById(R.id.transitLink);


    }

    private void setClickListener() {
        logOut.setOnClickListener(v -> logOutUser());
        profileTapped.setOnClickListener(v -> profileUser());
        carTapped.setOnClickListener(v -> setCarTapped());
        homeTapped.setOnClickListener(v -> setHomeTapped());
        buySellTapped.setOnClickListener(v -> setBuySellTapped());
        busTapped.setOnClickListener(v -> setTransitTapped());


    }


    //Firebase Instance
    private void firebaseInstances(){
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
    }

   // Retrieving name from FirebaseFirestore

    private void documentSnapshotRetrieve() {
        dbReference = fbStore.collection("users").document(userID);
        dbReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    String fullName = value.getString("fullNameUser");
                    profileName.setText("Hello! " + fullName);
                } else {
                    profileName.setText("User Name");
                }

            }
        });

    }


    //logout user
    private void logOutUser() {
        fAuth.signOut();
        Toast.makeText(DashBoard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Clear session and redirect to loginPage
        sessionManager.logout();
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();

    }

    private void setCarTapped() {
        startActivity(new Intent(getApplicationContext(), CarRecyclerActivity.class));

    }

    private void setBuySellTapped() {
        startActivity(new Intent(getApplicationContext(), BuysellRecyclerActivity.class));
    }

    private void setHomeTapped() {
        startActivity(new Intent(getApplicationContext(), HomeRecyclerActivity.class));
    }

    //redirected ton profile
    private void profileUser() {
        startActivity(new Intent(getApplicationContext(), profile.class));

    }

    private void setTransitTapped() {
        Uri uri = Uri.parse("https://www.myridebarrie.ca/");
        if (uri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else{
            Toast.makeText(DashBoard.this, "Error. Invalid website", Toast.LENGTH_SHORT).show();
        }


    }
}
