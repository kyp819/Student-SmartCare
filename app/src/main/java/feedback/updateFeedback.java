package feedback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studentsmartcare.R;

public class updateFeedback extends AppCompatActivity {

    feedbackDBHelper feedbackDBHelper;
    feedBackDao feedBackDao;
    feedBack feedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_feedback);


        feedBack = (feedBack) getIntent().getSerializableExtra("model");
        if (feedBack != null) {
            feedbackDBHelper = feedbackDBHelper.getInstance(this);
            feedBackDao = feedbackDBHelper.getDao();

            // Inflate the dialog layout
            View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_update_feedback, null);

            // Initialize dialog views
            EditText editFullName = dialogView.findViewById(R.id.editTextFullName);
            EditText editComment = dialogView.findViewById(R.id.editTextComment);
            Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
            Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

            // Populate EditTexts with existing data
            editFullName.setText(feedBack.getUserName());
            editComment.setText(feedBack.getComment());

            // Create AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            // Handle update button click
            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newFullName = editFullName.getText().toString().trim();
                    String newComment = editComment.getText().toString().trim();

                    // Update data and notify adapter
                    feedBack.setUserName(newFullName);
                    feedBack.setComment(newComment);
                    feedBackDao.updateFeedback(feedBack);
                    // Dismiss dialog
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),feedbackRecyclerActivity.class));
                    finish();
                    Toast.makeText(updateFeedback.this, "Updated feedback", Toast.LENGTH_SHORT).show();

                }
            });

            // Handle cancel button click
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Dismiss dialog
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),feedbackRecyclerActivity.class));
                    finish();
                }
            });

            dialog.show();
        } else {

            finish();
        }
    }
}