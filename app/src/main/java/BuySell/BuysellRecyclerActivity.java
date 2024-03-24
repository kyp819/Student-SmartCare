package BuySell;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.studentsmartcare.DashBoard;
import com.studentsmartcare.R;
import com.studentsmartcare.profile;

import java.util.ArrayList;
import java.util.Objects;

import CarCardView.CarCardModel;

public class BuysellRecyclerActivity extends AppCompatActivity implements buySellRecyclerInterface {
    private ImageView backButton;
    ArrayList<BuySellModel> buySellCardModel = new ArrayList<>();
    private SearchView searchBar;
    private RoundedImageView profileTapped;

    private RecyclerView buySellRecyclerView;
private StorageReference storageReference;
    private TextView profileName;

    private BuySellRecyclerViewAdapter buySellAdapter;

    int[] bookImages = {R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book};
    private String userID;
    FirebaseAuth fAuth;
    DocumentReference dbReference;
    private FirebaseFirestore fbStore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell_recycler_activity);

        initializationViews();
        firebaseInstances();
        documentSnapshotRetrieve();
        storageReference = FirebaseStorage.getInstance().getReference();
        retrieveProfileImage();
        setUpBuySellModel();
        buySellRecyclerView.setAdapter(buySellAdapter);
        buySellRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clickListener();
        searchBarFunction();


    }




    private void setUpBuySellModel(){
        String[] bookNames = getResources().getStringArray(R.array.bookName);
        String[] sellerNames = getResources().getStringArray(R.array.sellerName);
        String[] contactNum = getResources().getStringArray(R.array.contactNumSeller);


        for(int i = 0; i< bookNames.length; i++){
            buySellCardModel.add(new BuySellModel(
                    bookImages[i],
                    bookNames[i],
                    sellerNames[i],
                    contactNum[i]


            ));
        }


    }

    private void clickListener(){
        backButton.setOnClickListener(v -> backButtonPressed());
        profileTapped.setOnClickListener(v -> profileUser());


    }



    private void initializationViews(){
        backButton = findViewById(R.id.backNow);
        profileTapped = findViewById(R.id.profileView);
        searchBar = findViewById(R.id.searchBar);
        profileName = findViewById(R.id.helloUser);
        buySellRecyclerView = findViewById(R.id.buySellRecyclerActivityId);
        buySellAdapter = new BuySellRecyclerViewAdapter(this, this, buySellCardModel);
    }

    //Instances from Firebase
    private void firebaseInstances(){
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();
    }


    //PopUp on Clicked
    @Override
    public void onItemClick(int pos){
        popUpMessage();

    }
    private void popUpMessage() {
        // Inflating the custom pop layout
            Dialog popUp = new Dialog(this);
            popUp.setContentView(R.layout.buysellpopupinfo);

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(popUp.getWindow().getAttributes());

            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Example: Match parent width
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Example: Wrap content height

            popUp.getWindow().setAttributes(layoutParams);

            // Set background to transparent
            popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            popUp.show();
        }







    //Retrieving name from FirebaseFirestore

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
                ArrayList<BuySellModel> filteredList = new ArrayList<>();
                for (BuySellModel item : buySellCardModel) {
                    if (item.getBookName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(BuysellRecyclerActivity.this, "Sorry, No result", Toast.LENGTH_SHORT).show();
                } else {
                    buySellAdapter.setFiltered(filteredList);
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
        FirebaseDatabase.getInstance().getReference("BuySell")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        buySellCardModel.clear();
                        for (DataSnapshot dbSnapshot: snapshot.getChildren()){
                            BuySellModel buySellModel = dbSnapshot.getValue(BuySellModel.class);
                            buySellCardModel.add(buySellModel);

                        }
                        // Notify adapter after adding data
                        buySellAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Database error: " + error.getMessage());
                    }
                });
    }

    //Retrieval of user Profile name using UserProfileModel

    private void profileUser() {
        startActivity(new Intent(getApplicationContext(), profile.class));
    }

    private void backButtonPressed(){
        finish();

    }




}