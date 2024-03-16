package HomeCardView;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.studentsmartcare.R;
import com.studentsmartcare.profile;

import java.util.ArrayList;
import java.util.Objects;


public class HomeRecyclerActivity extends AppCompatActivity implements homeCardInterface {
    private ImageView backButton;
    private RoundedImageView profileTapped;

    private RecyclerView homeRecyclerViewActivity;
    private HomeRecyclerAdapter homeAdapter;
    private SearchView searchBar;
    private DocumentReference dbReference;
    private TextView profileName;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fbStore;
    private FirebaseUser currentUser;
    private String userID;
    private  StorageReference storageReference;


    ArrayList<HomeCardModel> homeCardModel = new ArrayList<>();
    int[] homeImages = {R.drawable.home, R.drawable.home, R.drawable.home, R.drawable.home, R.drawable.home};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_recycler_activity);

        initializationViews();
        firebaseInstances();
        documentSnapshotRetrieve();
        storageReference = FirebaseStorage.getInstance().getReference();
        retrieveProfileImage();
        setUpHomeModel();
        homeRecyclerViewActivity.setAdapter(homeAdapter);
        homeRecyclerViewActivity.setLayoutManager(new LinearLayoutManager(this));

      clickListener();
        searchBarFunction();
    }




    private void setUpHomeModel() {
        String[] homeOwnerName = getResources().getStringArray(R.array.homeOwner);
        String[] homeLocation = getResources().getStringArray(R.array.location);
        String[] homePhoneNum = getResources().getStringArray(R.array.contactNumber);

        for (int i = 0; i < homeOwnerName.length; i++) {
            homeCardModel.add(new HomeCardModel(
                    homeOwnerName[i],
                    homeLocation[i],
                    homePhoneNum[i],
                    homeImages[i]
            ));

        }
    }

private void initializationViews(){
        backButton = findViewById(R.id.backNow);
        profileTapped = findViewById(R.id.profileView);
        searchBar = findViewById(R.id.searchBar);
        homeRecyclerViewActivity  = findViewById(R.id.homeRecyclerActivityID);
        homeAdapter = new HomeRecyclerAdapter(this, this, homeCardModel);
        profileName = findViewById(R.id.helloUser);



}

    private void clickListener() {
        backButton.setOnClickListener(v -> backButtonPressed());
        profileTapped.setOnClickListener(v -> profileUser());
    }


    //firebase Instances
    private void firebaseInstances(){
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();
    }

    @Override
    public void onItemClick(int pos){
        popUpMessage();

    }
    private void popUpMessage() {
        // Inflating the custom pop layout
        Dialog popUp = new Dialog(this);
        popUp.setContentView(R.layout.activity_home_card_pop_up);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(popUp.getWindow().getAttributes());

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Example: Match parent width
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Example: Wrap content height

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



    //Search functionality
    private void searchBarFunction() {
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
                ArrayList<HomeCardModel> filteredList = new ArrayList<>();
                for(HomeCardModel item : homeCardModel ) {
                    if (item.getOwnerName().toLowerCase().contains(newText.toLowerCase())) {
                       filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(HomeRecyclerActivity.this, "Sorry, No result", Toast.LENGTH_SHORT).show();
                } else {
                    homeAdapter.setFiltered(filteredList);
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
    private void backButtonPressed(){
        finish();

    }
    private void profileUser() {
        startActivity(new Intent(getApplicationContext(), profile.class));
    }


}


