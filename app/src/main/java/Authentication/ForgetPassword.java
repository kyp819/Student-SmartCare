package Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.studentsmartcare.R;

public class ForgetPassword extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private EditText emailForReset;
    private Button resetButton;

    ImageView backButtonTapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
          initialization();
          //Instances
        fAuth = FirebaseAuth.getInstance();
             listener();

    }

    
    private void initialization(){
        backButtonTapped = findViewById(R.id.backButton);
        emailForReset = findViewById(R.id.emailReset);
        resetButton = findViewById(R.id.reset);
        
    }
    
    private void listener(){
        backButtonTapped.setOnClickListener(v -> backButtonPressed());
        resetButton.setOnClickListener(v -> resetButtonPressed());

    }
    private void resetButtonPressed() {
        String emailFilled = emailForReset.getText().toString();
        if (!emailFilled.isEmpty()){
            resetPassword(emailFilled);

        }else {
            emailForReset.setError("Please enter your email");
        }

    }



    //resetting method
    private void resetPassword(String email){
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgetPassword.this, "Reset email sent." ,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),LoginPage.class));
                    finish();
                }else{
                    Toast.makeText(ForgetPassword.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void backButtonPressed() {
        finish();
    }


}
