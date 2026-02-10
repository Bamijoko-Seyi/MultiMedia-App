package com.example.mediahub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private FireBaseLogin firebaseLogin;
    private androidx.appcompat.widget.AppCompatButton registerButton;
    private TextView emailErrorText;
    private TextView passwordErrorText;
    private TextView confirmPasswordErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        confirmPasswordInput = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.continueButton);
        emailErrorText = findViewById(R.id.invalidEmailTextView);
        passwordErrorText = findViewById(R.id.invalidPasswordTextView);
        confirmPasswordErrorText = findViewById(R.id.invalidConfirmPasswordTextView);
        firebaseLogin = new FireBaseLogin();

        hideEmailError();
        hidePasswordError();
        hideConfirmPasswordError();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmailError();
                hidePasswordError();
                hideConfirmPasswordError();

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    if (email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                        showEmailError("E-mail is required");
                        showPasswordError("Password is required");
                        showConfirmPasswordError("Confirm Password is required");
                    }
                    else if (email.isEmpty()) {
                        showEmailError("E-mail is required");
                    }
                    else if (password.isEmpty()) {
                        showPasswordError("Password is required");
                        }
                    else {
                        showConfirmPasswordError("Please renter your password");
                    }
                }
                else{
                    if (!password.equals(confirmPassword)) {
                        showConfirmPasswordError("Passwords do not match");
                    }
                    else {
                        firebaseLogin.signUp(email, password, new FireBaseLogin.Callback() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(RegisterActivity.this, CategorySelectionActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                // Deal with cases where the email is already in use
                                hideEmailError();
                                hidePasswordError();
                                hideConfirmPasswordError();
                                if (errorMessage.toLowerCase().contains("Password should be at least 6 characters")) {
                                    showPasswordError("Password should be at least 6 characters");
                                }
                                else if (errorMessage.toLowerCase().contains("email")) {
                                    showEmailError("E-mail is formatted incorrectly");
                                }
                            }
                        });
                    }
                }
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

    private void hideConfirmPasswordError() {
        if (confirmPasswordErrorText != null) {
            confirmPasswordErrorText.setVisibility(View.GONE);
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

    private void showConfirmPasswordError(String message) {
        if (confirmPasswordErrorText != null) {
            confirmPasswordErrorText.setText(message);
            confirmPasswordErrorText.setVisibility(View.VISIBLE);
        }
    }



}
