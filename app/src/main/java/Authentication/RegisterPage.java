package Authentication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentsmartcare.R;
import com.studentsmartcare.DashBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //creating variables
    private EditText emailId, password, confirmPassword, fullName;
    private Button signUpButton;
    private TextView alreadyAccount;
    private Spinner spinnerItem;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fbStore;

    private DocumentReference dReference;
    private String userId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        initializeViews();

        //Instance
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        //Created event listener
        signUpButton.setOnClickListener(v -> registerUser());

        alreadyAccount.setOnClickListener(v -> redirectToLoginPage());

        setUpSpinner();



    }



    //Method for the Initialization the variables reference to register-xml
    private void initializeViews() {
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.signUp);
        alreadyAccount = findViewById(R.id.alreadyAccount);
        fullName = findViewById(R.id.fullName);
        spinnerItem = findViewById(R.id.spinner);

    }




    //method for creating registration and validation
    private void registerUser() {
        String emailField = emailId.getText().toString().trim();
        String passwordField = password.getText().toString().trim();
        String fullNameRegistered = fullName.getText().toString().trim();
        String confirmPasswordField = confirmPassword.getText().toString().trim();
        String spinnerField = spinnerItem.getSelectedItem().toString();


        if (TextUtils.isEmpty(emailField)) {
            emailId.setError("Email field is required");
            return;
        }
        if (TextUtils.isEmpty(passwordField)) {
            password.setError("Password field is required");
            return;
        }
        if (passwordField.length() < 8) {
            password.setError("Password should be at least eight characters");
            return;
        }
        if (!confirmPasswordField.equals(passwordField)) {
            confirmPassword.setError("Password does not match");
            return;
        }

        fAuth.createUserWithEmailAndPassword(emailField, passwordField)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterPage.this, "User Created", Toast.LENGTH_SHORT).show();
                        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                        dReference = fbStore.collection("users").document(userId);
                        Map<String, Object> user = new HashMap<>();
                        user.put("emailUser", emailField);
                        user.put("passwordUser", passwordField);
                        user.put("fullNameUser", fullNameRegistered);
                        user.put("confirmPasswordUser", confirmPasswordField);
                        user.put("spinnerItemSelected", spinnerField);

                        dReference.set(user).addOnSuccessListener(aVoid ->
                                        Log.d("RegisterPage", "Data write operation successful"))
                                .addOnFailureListener(e ->
                                        Log.e("RegisterPage", "Data write operation failed: " + e.getMessage()));

                        startActivity(new Intent(getApplicationContext(), DashBoard.class));
                    } else {
                        Toast.makeText(RegisterPage.this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //redirecting to loginPage after clicking on login
    private void redirectToLoginPage() {
        startActivity(new Intent(getApplicationContext(), LoginPage.class));
        finish();
    }



    //spinner method for the item selection
    private void setUpSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedItem, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
