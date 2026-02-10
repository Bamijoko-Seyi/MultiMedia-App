package com.example.mediahub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorySelectionActivity extends AppCompatActivity {
    private ListView listView;
    private androidx.appcompat.widget.AppCompatButton continueButton;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        userId = UserSession.getInstance().getUserId();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_new);

        listView = findViewById(R.id.listView);
        continueButton = findViewById(R.id.continueButton);

        // Data
        List<Category> data = new ArrayList<>();
        data.add(new Category("Action", "Explosive movies and shows",false));
        data.add(new Category("Comedy", "Laugh out loud series",false));
        data.add(new Category("Drama", "Emotional stories",false));
        data.add(new Category("Sci-Fi", "Space and futuristic",false));
        data.add(new Category("Horror", "Scary and thrilling content", false));
        data.add(new Category("Romance", "Love stories and heartwarming tales", false));
        data.add(new Category("Thriller", "Suspense and edge-of-your-seat plots", false));
        data.add(new Category("Fantasy", "Magical worlds and adventures", false));
        data.add(new Category("Documentary", "Real stories and factual content", false));
        data.add(new Category("Mystery", "Whodunits and crime-solving plots", false));
        data.add(new Category("Animation", "Cartoons and animated films", false));
        data.add(new Category("Family", "Fun for all ages", false));
        data.add(new Category("Adventure", "Exciting journeys and quests", false));
        data.add(new Category("Sports", "Athletic events and competitions", false));
        data.add(new Category("History", "Stories from the past", false));

        CategoriesAdapter adapter = new CategoriesAdapter(this, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Category clickedItem = data.get(position);

            // Toggle selection (tap once to select, again to deselect)
            clickedItem.setSelected(!clickedItem.isSelected());

            // Refresh list so background updates
            adapter.notifyDataSetChanged();
        });

        continueButton.setOnClickListener(v -> {
            // Check if any category is selected
            boolean anyCategorySelected = data.stream().anyMatch(Category::isSelected);
            if (anyCategorySelected) {
                Map<String, Object> categoryMap = new HashMap<>();
                List<String> myCategories = new ArrayList<>();
                for (Category category : data) {
                    if (category.isSelected()) {
                        myCategories.add(category.getTitle());
                    }
                }
                Map<String, Object> updates = new HashMap<>();
                updates.put("categories", myCategories);
                updates.put("categorySelected", true);
                db.collection("loginProfile")
                        .document(userId)
                        .set(updates, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> {
                            Log.d("CategorySelectionActivity", "Categories saved successfully");

                            Intent intent = new Intent(CategorySelectionActivity.this, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("CategorySelectionActivity", "Failed to save categories", e);
                            Toast.makeText(this, "Failed to save categories", Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(CategorySelectionActivity.this, "No category was selected", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
