package feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentsmartcare.DashBoard;
import com.studentsmartcare.R;

import java.util.ArrayList;
import java.util.List;


public class feedbackRecyclerActivity extends AppCompatActivity implements listenerInterface {

    private TextView userNameFeed, userCommentFeed;

    private FloatingActionButton feedBackAdd;
    private ImageView userImageFeed, backButtonPressed;
    ArrayList<feedBack> models = new ArrayList<>();

    RecyclerView recyclerView;

    feedbackDBHelper feedbackDBHelper;


    feedbackAdapter feedbackAdapter;


    feedBackDao feedBackDao;
    feedBackModel feedBackModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_recycler);

        feedbackDBHelper = feedbackDBHelper.getInstance(this);
        feedBackDao = feedbackDBHelper.getDao();
        recyclerView = findViewById(R.id.recyclerViewFeed);
        feedbackAdapter = new feedbackAdapter(this, models,this);
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
            for(int i = 0; i < feedBackList.size(); i++){
                feedBack feedBack= feedBackList.get(i);
                feedbackAdapter.addData(feedBack);

                recyclerView.scrollToPosition(feedbackAdapter.getItemCount()-1);
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();

    }



    @Override
    public void onDelete(int id, int pos) {
        feedBackDao.deleteFeedback(id);
        models.remove(pos);
        feedbackAdapter.notifyItemRemoved(pos);
        feedbackAdapter.notifyItemRangeChanged(pos, models.size());

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

    }
}