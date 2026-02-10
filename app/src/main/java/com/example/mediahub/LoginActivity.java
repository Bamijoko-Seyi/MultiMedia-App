package com.example.mediahub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    // Initialize all the ui components
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private FireBaseLogin firebaseLogin;
    private androidx.appcompat.widget.AppCompatButton signInButton;
    private TextView registerText;
    private TextView emailErrorText;
    private TextView passwordErrorText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connect each ui component to their respective id in the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.continueButton);
        emailErrorText = findViewById(R.id.invalidEmailTextView);
        passwordErrorText = findViewById(R.id.invalidPasswordTextView);
        registerText = findViewById(R.id.registerTextView);

        // Hide the error text views by default
        hideEmailError();
        hidePasswordError();

        // Initialize the firebase login object
        firebaseLogin = new FireBaseLogin();
signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Hide the error text views before checking for errors
                hideEmailError();
                hidePasswordError();

                // Convert the text input fields to strings
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Handle edge cases before we try to register
                if (email.isEmpty() || password.isEmpty()) {
                    if (email.isEmpty() && password.isEmpty()) {
                        showEmailError("E-mail is required");
                        showPasswordError("Password is required");
                        Toast.makeText(LoginActivity.this, "E-mail and password are required", Toast.LENGTH_SHORT).show();
                    } else if (email.isEmpty()) {
                        showEmailError("E-mail is required");
                        Toast.makeText(LoginActivity.this, "E-mail is required", Toast.LENGTH_SHORT).show();
                    } else {
                        showPasswordError("Password is required");
                        Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    firebaseLogin.passwordLogin(email, password, new FireBaseLogin.Callback() {

                                // Handle cases where we fail to login
                                @Override
                                public void onFailure(String errorMessage) {
                                    hideEmailError();
                                    hidePasswordError();

                                    if (errorMessage.toLowerCase().contains("email")) {
                                        showEmailError("E-mail is formatted incorrectly");
                                    } else if (errorMessage.toLowerCase().contains("password is incorrect")) {
                                        showPasswordError("Password is either invalid or the user doesn't exist");
                                    } else {
                                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onSuccess() {
                                    // if the login to firebaseAuth was successful, we use the user id to query the firestore database
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String userId = firebaseUser.getUid();
                                    Log.d("LoginActivity", "User ID: " + userId);
                                    UserSession.getInstance().setUserId(userId);

                                    db.collection("loginProfile")
                                            .whereEqualTo("userId", userId)
                                            .get()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                    String language = document.getString("language");
                                                    Boolean categorySelected = document.getBoolean("categorySelected");
                                                    String role  = document.getString("role");
                                                    Boolean darkModeOn = document.getBoolean("darkModeOn");

                                                    if (categorySelected != null && categorySelected) {

                                                        List<String> categories =
                                                                (List<String>) document.get("categories");

                                                        if (categories == null) {
                                                            categories = new ArrayList<>();
                                                            Log.w("LoginActivity", "Categories field missing or empty");
                                                        }

                                                        UserSession.getInstance().setCategories(categories);

                                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    } else {
                                                        Intent intent = new Intent(LoginActivity.this, CategorySelectionActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }


                                                } else {
                                                    Log.e("LoginActivity", "Firestore query failed with exception: " + task.getException());
                                                    Toast.makeText(LoginActivity.this, "Failed to retrieve user profile data.", Toast.LENGTH_SHORT).show();
                                                }

                                            });

                                }

                            }
                    );
                }

            }
        });


        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //finish(); // Optional
            }
        });
    }

    private void hideEmailError() {
        if (emailErrorText != null) {
            emailErrorText.setVisibility(View.GONE);
        }
    }

    private void hidePasswordError() {
        if (passwordErrorText != null) {
            passwordErrorText.setVisibility(View.GONE);
        }
    }

    private void showEmailError(String message) {
        if (emailErrorText != null) {
            emailErrorText.setText(message);
            emailErrorText.setVisibility(View.VISIBLE);
        }
    }

    private void showPasswordError(String message) {
        if (passwordErrorText != null) {
            passwordErrorText.setText(message);
            passwordErrorText.setVisibility(View.VISIBLE);
        }
    }


}
