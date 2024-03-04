package SellerContents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.studentsmartcare.R;

import Authentication.LoginPage;

public class SellerDashBoard extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private Button logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dash_board);

fAuth = FirebaseAuth.getInstance();
    initialization();
        clickListener();
    }


    private void initialization(){
        logOut = findViewById(R.id.logout);
    }

    private void clickListener(){
        logOut.setOnClickListener(v -> logOutUser());
    }

    private void logOutUser() {
        fAuth.signOut();
        Toast.makeText(SellerDashBoard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        // Redirect to loginPage
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
}