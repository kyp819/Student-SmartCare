package feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentsmartcare.R;

public class feedbackForm extends AppCompatActivity {


    private EditText fullName, comment;
    private Button submitButton;

    private feedbackDBHelper feedbackDBHelper;
    private feedBackDao feedBackDao;

    private feedBack feedBack;
    private ImageView backButton;
    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        feedbackDBHelper = feedbackDBHelper.getInstance(this);
        feedBackDao = feedbackDBHelper.getDao();
        initialization();

        submitButton.setOnClickListener(v -> submitFeedback());
        backButton.setOnClickListener(v -> setBackButton());



    }

    private void initialization() {
        fullName = findViewById(R.id.editFullNameId);
        comment = findViewById(R.id.editcomment);
        submitButton = findViewById(R.id.submitClicked);
        backButton = findViewById(R.id.backButton);
        rootView = findViewById(R.id.feedbackFormID);
    }

    private void submitFeedback() {
        String userName = fullName.getText().toString();
        String comments = comment.getText().toString();


        feedBack = new feedBack(0, userName, comments);

        feedBackDao.addFeedback(feedBack);

        Toast.makeText(feedbackForm.this, "Feedback Added", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getApplicationContext(), feedbackRecyclerActivity.class));
        finish();

    }

//dismissing keyboard

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if (inputMethodManager != null && currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    private void dismissKeyboard(){
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(feedbackForm.this);
                return false;
            }
        });

    }

    private void setBackButton(){
        startActivity(new Intent(getApplicationContext(),feedbackRecyclerActivity.class));
        finish();

    }
    }

