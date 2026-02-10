package com.example.mediahub;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FireBaseLogin {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Context context;
    private FirebaseUser firebaseUser;
    private String errorMessage;

    public interface Callback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void signUp(String email, String password, Callback callback){
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db.collection("loginProfile").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().isEmpty()) {
                        // If the firebase auth account has been successfully created, add the user's details into the database
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(authTask -> {
                                    if (authTask.isSuccessful()) {
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        String userId = firebaseUser.getUid();

                                        Map<String, Object> signupMap = new HashMap<>();
                                        //Modify the user's details based on their role
                                        signupMap.put("userId", userId); // I stored the userId generated from the firebaseAuth
                                        signupMap.put("email", email);
                                        signupMap.put("categorySelected", false);
                                        signupMap.put("darkModeOn", false);

                                        signupMap.put("language", "English");

                                        signupMap.put("notificationsDisabled", false);

                                        db.collection("loginProfile").document(userId).set(signupMap)
                                                .addOnCompleteListener(dbTask -> {
                                                    if (dbTask.isSuccessful()) {
                                                        UserSession.getInstance().setUserId(userId);
                                                        callback.onSuccess();
                                                    } else {
                                                        errorMessage = dbTask.getException() != null
                                                                ? dbTask.getException().getMessage()
                                                                : "Unknown error occurred";
                                                        callback.onFailure("Registration failed: " + errorMessage);
                                                    }
                                                });
                                    } else {
                                       String errorMessage = authTask.getException() != null
                                                ? authTask.getException().getMessage()
                                                : "Unknown error occurred";
                                        callback.onFailure("Registration failed: " + errorMessage);
                                    }
                                });
                    }
                    //If the username is already in use, use a toast to inform the user that the username is taken
                    else {
                        errorMessage = task.getException() != null
                                ? task.getException().getMessage()
                                : "Unknown error occurred";
                        callback.onFailure("Username Is already taken: " + errorMessage);
                    }
                });
    }



    public void passwordLogin(String email, String password, Callback callback){
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Password is incorrect: " + authTask.getException().getMessage());
                    }
                });

    }
}
