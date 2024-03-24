package feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.studentsmartcare.DashBoard;
import com.studentsmartcare.R;

import java.io.Serializable;

public class feedbackRecyclerActivity extends AppCompatActivity implements ListenerInterface {

    private ImageView backButtonPressed;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private feedbackAdapter feedbackAdapter;
    private feedBackModel model;

    private FirebaseDatabase database;

    private String currentUserId;

    private FirebaseRecyclerOptions<feedBackModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_recycler);

        //Initialization
        initializer();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();


       fetchData();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        feedbackAdapter = new feedbackAdapter(options, this, this, currentUserId);
        recyclerView.setAdapter(feedbackAdapter);

        // Set click listeners for buttons
        backButtonPressed.setOnClickListener(v -> setBackButton());
        addButton.setOnClickListener(v -> feedBackForm());


    }

    private void initializer(){
        backButtonPressed = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.recyclerViewFeed);
        addButton = findViewById(R.id.feedBackAdd);

    }





    // Method to navigate to the feedback form activity
    private void feedBackForm() {
        startActivity(new Intent(getApplicationContext(), feedbackForm.class));
        finish();
    }


    private void  fetchData(){
        options = new FirebaseRecyclerOptions.Builder<feedBackModel>()
                .setQuery(database.getReference().child("Feedback"), feedBackModel.class)
                .build();


    }
    @Override
    protected void onStart() {
        super.onStart();
        feedbackAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        feedbackAdapter.stopListening();
    }


    // Method to navigate back to the dashboard
    private void setBackButton() {
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
    }


    //Deletion feedback

    @Override
    public void onDelete(String deleteModel) {
        DatabaseReference databaseRef = database.getReference().child("Feedback").child(deleteModel);
        databaseRef.removeValue();
    }


}
