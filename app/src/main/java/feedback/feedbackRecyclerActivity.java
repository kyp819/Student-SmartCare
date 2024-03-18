package feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.studentsmartcare.DashBoard;
import com.studentsmartcare.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class feedbackRecyclerActivity extends AppCompatActivity implements listenerInterface {

    private TextView userNameFeed, userCommentFeed;

    private FloatingActionButton feedBackAdd;
    private ImageView userImageFeed, backButtonPressed;
    ArrayList<feedBack> models = new ArrayList<>();

    RecyclerView recyclerView;

    feedbackDBHelper feedbackDBHelper;


    feedbackAdapter feedbackAdapter;

    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    String currentUserId;


    feedBackDao feedBackDao;
    feedBackModel feedBackModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_recycler);

        firebaseAuth = FirebaseAuth.getInstance();
        // Get the current user
        currentUser = firebaseAuth.getCurrentUser();
        // Get the user ID
        feedbackDBHelper = feedbackDBHelper.getInstance(this);
        feedBackDao = feedbackDBHelper.getDao();
        recyclerView = findViewById(R.id.recyclerViewFeed);
        feedbackAdapter = new feedbackAdapter(this, models, this, currentUserId);
        recyclerView.setAdapter(feedbackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initializer();
        backButtonPressed.setOnClickListener(v -> setBackButton());
        feedBackAdd.setOnClickListener(v -> feedBackFormUI());

    }


    private void initializer() {
        userNameFeed = findViewById(R.id.userName);
        userCommentFeed = findViewById(R.id.userComment);
        userImageFeed = findViewById(R.id.userImage);
        backButtonPressed = findViewById(R.id.backButton);
        feedBackAdd = findViewById(R.id.feedBackAdd);

    }


    private void fetchData() {
        feedbackAdapter.clearData();

        List<feedBack> feedBackList = feedBackDao.getAllFeedback();

        for (feedBack feedback : feedBackList) {
            feedbackAdapter.addData(feedback);
        }
        recyclerView.scrollToPosition(feedbackAdapter.getItemCount() - 1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchData();

    }


    @Override
    public void onDelete(int id, int pos) {
        feedBack feedback = models.get(pos);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (feedback.getUserId().equals(currentUserId)) {
            // Delete the comment if it belongs to the current user
            feedBackDao.deleteFeedback(id);
            models.remove(pos);
            feedbackAdapter.notifyItemRemoved(pos);
            feedbackAdapter.notifyItemRangeChanged(pos, models.size());
        } else {



            // Display a message indicating that the user is not allowed to delete this comment
            Toast.makeText(this, "You can only delete your own comments", Toast.LENGTH_SHORT).show();
        }


    }

    //backButton
    private void setBackButton (){
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
    }

    private void feedBackFormUI(){
        startActivity(new Intent(getApplicationContext(), feedbackForm.class));


    }

    @Override
    public void onUpdate(feedBack feedBack) {
        Intent intent = new Intent(this, updateFeedback.class);
        intent.putExtra("model", feedBack);
        startActivity(intent);
    }
}