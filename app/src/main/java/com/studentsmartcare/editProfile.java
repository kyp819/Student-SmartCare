package com.studentsmartcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Authentication.LoginPage;

public class editProfile extends AppCompatActivity {

    private EditText fullNameEdit, emailEdit, phoneEdit;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fbStore;
   FirebaseUser fbUser;

    private Button updateButton, cancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        fbUser = fAuth.getCurrentUser();


        fullNameEdit = findViewById(R.id.userNameText);
        emailEdit = findViewById(R.id.emailText);
        phoneEdit = findViewById(R.id.phoneText);
        updateButton = findViewById(R.id.updateButton);
        cancelButton = findViewById(R.id.cancelButton);

        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phoneNumber = data.getStringExtra("phoneNumber");

fullNameEdit.setText(fullName);
emailEdit.setText(email);
phoneEdit.setText(phoneNumber);

updateButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (fullNameEdit.getText().toString().isEmpty()||
            emailEdit.getText().toString().isEmpty()||
             phoneEdit.getText().toString().isEmpty()){
            return;
        }

        String email = emailEdit.getText().toString();
        fbUser.verifyBeforeUpdateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference dbReference = fbStore.collection("users").document(fbUser.getUid());
                Map<String, Object> editedProfile = new HashMap<>();
                editedProfile.put("emailUser", email);
                editedProfile.put("fullNameUser", fullNameEdit.getText().toString());
                editedProfile.put("phoneNumber", phoneEdit.getText().toString());
                dbReference.update(editedProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(editProfile.this,"Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });



                Toast.makeText(editProfile.this,"Profile Updated", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
Toast.makeText(editProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



});


cancelButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), profile.class));
        finish();

    }
});




    }

}