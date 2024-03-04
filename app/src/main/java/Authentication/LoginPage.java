package Authentication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studentsmartcare.R;
import com.studentsmartcare.DashBoard;
import com.studentsmartcare.SessionManager;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;


public class LoginPage extends AppCompatActivity {

    //creating variables
    private EditText emailId;
    private EditText password;
    private CompoundButton passwordVisibilityBtn;
    private Button loginButton;
    private TextView forgetPassword, registeredAccount;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fbStore;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private ImageButton googleButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            // Redirect to the dashboard or another relevant screen
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish(); // Optionally finish the login activity
        }
        setContentView(R.layout.activity_login_page);

        initializeViews();

        fAuth = FirebaseAuth.getInstance();

        fbStore = FirebaseFirestore.getInstance();


        //checking if the user logged in or not.
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
        }
passwordVisibility();
            googleSignIn();
            setClickListener();

    }


    //method for initializing variables reference to login-xml
    private void initializeViews() {
        emailId = findViewById(R.id.editEmail);
        password = findViewById(R.id.editpassword);
        passwordVisibilityBtn = findViewById(R.id.togglePw);
        loginButton = findViewById(R.id.login);
        forgetPassword = findViewById(R.id.forgetpassword);
        registeredAccount = findViewById(R.id.registerAccount);
        googleButton =  findViewById(R.id.googleButton);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

    }

private void setClickListener(){
    loginButton.setOnClickListener(v -> loginUser());
    registeredAccount.setOnClickListener(v -> redirectToRegisterPage());
    forgetPassword.setOnClickListener(v -> forgetPasswordPage());
    googleButton.setOnClickListener(v -> googleClicked());

}



//SignIn using google
    private void googleSignIn() {
    activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK);
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                        try {
                            task.getResult(ApiException.class);
                            finish();
                            Intent intent = new Intent(LoginPage.this, DashBoard.class);
                            startActivity(intent);


                        } catch (ApiException e) {
                            Toast.makeText(LoginPage.this, ("Something went wrong"), Toast.LENGTH_SHORT).show();
                       }
                    }
                });



    }

    private void googleClicked (){
        Intent signInIntent = gsc.getSignInIntent();
        activityResultLauncher.launch(signInIntent);

    }

    //Login Password visibility
private void passwordVisibility() {
       passwordVisibilityBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          if (isChecked) {
            password.setTransformationMethod(null);
              passwordVisibilityBtn.setBackgroundResource(R.mipmap.showpasswordicon);
              } else {
              //hide password
              password.setTransformationMethod(PasswordTransformationMethod.getInstance());
               passwordVisibilityBtn.setBackgroundResource(R.mipmap.hidepasswordicon);
              }
              }
         });
}

    //creating method responsible error and validation
    private void loginUser() {
        String emailField = emailId.getText().toString().trim();
        String passwordField = password.getText().toString().trim();

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

        //user validation
        fAuth.signInWithEmailAndPassword(emailField, passwordField)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginPage.this, "Logged in Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),DashBoard.class));
                    }

                    else {
                        Toast.makeText(LoginPage.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }







    //redirecting to register UI
    private void redirectToRegisterPage() {
        startActivity(new Intent(getApplicationContext(),RegisterPage.class));
    }
    private void forgetPasswordPage() {
        startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
    }
}
