package CarCardView;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.studentsmartcare.BooknowActivity;
import com.studentsmartcare.R;
import com.studentsmartcare.profile;

import java.util.ArrayList;
import java.util.Objects;

import feedback.feedBackModel;


public class CarRecyclerActivity extends AppCompatActivity implements carCardInterface{
private ImageView backButton;
private RoundedImageView profileTapped;
private SearchView searchBar;

    private CarRecyclerViewAdapter carAdapter;
    private RecyclerView carRecyclerViewActivity;
    ArrayList<CarCardModel> carCardModel = new ArrayList<>();
    private DocumentReference dbReference;
    private TextView profileName;
    private FirebaseAuth  fAuth;
    private FirebaseFirestore fbStore;
    private String userID;
    private FirebaseUser currentUser;
    private StorageReference storageReference;

    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_recycle_actvity);

        initializationViews();
         firebaseInstances();
         documentSnapshotRetrieve();
         storageReference = FirebaseStorage.getInstance().getReference();
         retrieveProfileImage();

        carRecyclerViewActivity.setAdapter(carAdapter);
        carRecyclerViewActivity.setLayoutManager(new LinearLayoutManager(this));
        fetchData();
        clickListener();

        searchBarFunction();

    }






    private void initializationViews(){
        backButton = findViewById(R.id.backNow);
        profileTapped = findViewById(R.id.profileView);
        searchBar = findViewById(R.id.searchBar);
        carAdapter = new CarRecyclerViewAdapter(this, this,carCardModel);
        carRecyclerViewActivity = findViewById(R.id.carRecycleActivityID);
        profileName = findViewById(R.id.helloUser);



    }

    private void clickListener() {
        profileTapped.setOnClickListener(v -> profileUser());
        backButton.setOnClickListener(v -> backButtonPressed());
    }

    private void firebaseInstances(){
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();

    }

    @Override
    public void onItemClick(int pos){
        popUpMessage();

    }

    @Override
    public void onBookNowClick(int position) {
        startActivity(new Intent(getApplicationContext(), BooknowActivity.class));

    }

    private void popUpMessage() {
        // Inflating the custom pop layout
        Dialog popUp = new Dialog(this);
        popUp.setContentView(R.layout.activity_car_card_pop_up);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(popUp.getWindow().getAttributes());

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        popUp.getWindow().setAttributes(layoutParams);

        // Set background to transparent
        popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popUp.show();
    }


    //Retrieval profile name from firestore
    private void documentSnapshotRetrieve() {
        if (currentUser != null) {
            userID = currentUser.getUid();
            dbReference = fbStore.collection("users").document(userID);

            dbReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e(TAG, "Error fetching document", error);
                        return;
                    }

                    if (value != null) {
                        String fullName = value.getString("fullNameUser");
                        profileName.setText("Hello! " + fullName);
                    } else {
                        profileName.setText("User Name");
                    }
                }
            });
        } else {

            Log.e(TAG, "No authenticated user found");
        }
    }


    private void searchBarFunction(){
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            private void filterList(String newText) {
                ArrayList<CarCardModel> filteredList = new ArrayList<>();
                for (CarCardModel item: carCardModel){
                    if (item.getOwnerName().toLowerCase().contains(newText.toLowerCase()) ||
                            item.getCarModel().toLowerCase().contains(newText.toLowerCase()) ||
                            item.getRoute().toLowerCase().contains(newText.toLowerCase())){
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(CarRecyclerActivity.this, "Sorry, No result", Toast.LENGTH_SHORT).show();
                } else {

                    carAdapter.setFiltered(filteredList);
                    }

                }


        });
    }

    private  void retrieveProfileImage(){
        StorageReference pFileReference = storageReference.child("usersImage/" +fAuth.getCurrentUser().getUid()+"/profileImage");

        pFileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profileTapped);

            }
        });

    }



        private void fetchData() {
            FirebaseDatabase.getInstance().getReference("carDetails")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            carCardModel.clear();
                            for (DataSnapshot dbSnapshot: snapshot.getChildren()){
                                CarCardModel cardModel = dbSnapshot.getValue(CarCardModel.class);
                                carCardModel.add(cardModel);

                            }
                            // Notify adapter after adding data
                            carAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Database error: " + error.getMessage());
                        }
                    });
        }


        private void profileUser() {
        startActivity(new Intent(getApplicationContext(), profile.class));
    }

    private void backButtonPressed(){
        finish();

    }


}
