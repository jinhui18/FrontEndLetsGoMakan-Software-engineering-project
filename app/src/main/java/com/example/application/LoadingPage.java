package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.backend.control.others.FirebaseForAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoadingPage extends AppCompatActivity {
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        FirebaseForAPI fb = new FirebaseForAPI();
        fb.getAPIData(mAuth, mDatabase, this);


        mDatabase.child(userID).child("Account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //progressBar.setVisibility(View.GONE);
                //System.out.println("HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE1");
                //startActivity(new Intent(SetTimeLocation.this, DisplayRestaurantsList.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                progressBar.setVisibility(View.GONE);
                System.out.println("Callback 1");
                startActivity(new Intent(LoadingPage.this, DisplayRestaurantsList.class));
                mDatabase.child(userID).child("Account").removeEventListener(this);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
