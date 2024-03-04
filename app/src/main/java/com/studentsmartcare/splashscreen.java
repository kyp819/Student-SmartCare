package com.studentsmartcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;

import Authentication.LoginPage;

public class splashscreen extends AppCompatActivity {
    private ImageView imageView1, imageView2, imageView3, imageView4;
    private EditText editTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        editTextText = findViewById(R.id.editTextText);

        // Set up animation for each ImageView
        animateImageView(imageView1, 500);
        animateImageView(imageView2, 1000);
        animateImageView(imageView3, 1500);
        animateImageView(imageView4, 2000);

        new Handler().postDelayed(() -> {
            // This code will run after the delay
            // Start your main activity or another activity
            Intent mainIntent = new Intent(splashscreen.this, LoginPage.class);
            startActivity(mainIntent);

            // Close the splash screen activity
            finish();
        }, 2500); // 2500 milliseconds (2.5 seconds) delay
    }
    private void animateImageView(ImageView imageView, int duration) {
        imageView.setScaleX(0f);
        imageView.setScaleY(0f);
        imageView.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(duration)
                .start();
    }
}