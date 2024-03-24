// feedbackForm.java
package feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.studentsmartcare.R;

import java.util.HashMap;
import java.util.Map;

public class feedbackForm extends AppCompatActivity {

    private EditText fullName, comment;
    private Button submitButton;
    private ImageView backButton;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        fullName = findViewById(R.id.editFullNameId);
        comment = findViewById(R.id.editcomment);
        submitButton = findViewById(R.id.submitClicked);
        backButton = findViewById(R.id.backButton);

        database = FirebaseDatabase.getInstance();

        submitButton.setOnClickListener(v -> submitFeedback());
        backButton.setOnClickListener(v -> setBackButton());
    }

    private void submitFeedback() {
        String fullNameText = fullName.getText().toString();
        String commentText = comment.getText().toString();


        if (!fullNameText.isEmpty() && !commentText.isEmpty()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(userId != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("fullName", fullNameText);
                map.put("comment", commentText);
                map.put("userId", userId);

                database.getReference().child("Feedback").push()
                        .setValue(map)
                        .addOnSuccessListener(unused -> {
                            fullName.setText("");
                            comment.setText("");
                        }).addOnFailureListener(e -> {
                        });


                Toast.makeText(this, "Feedback added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), feedbackRecyclerActivity.class));
            } finish();

        }
    }

    private void setBackButton() {
        startActivity(new Intent(getApplicationContext(), feedbackRecyclerActivity.class));
        finish();
    }
}
