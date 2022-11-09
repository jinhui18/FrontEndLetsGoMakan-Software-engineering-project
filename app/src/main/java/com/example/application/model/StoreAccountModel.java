package com.example.application.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.view.InputPreferencesUI;
import com.example.application.backend.control.others.FormatChecker;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StoreAccountModel extends Model{


    public StoreAccountModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    // store data/ interact with firebase
    @Override
    public void service() {
        // FORMAT: attributeList = [name, email, textInputEmail, password, textInputPassword]

        //Getting necessary variables
        String name = ((TextInputEditText) super.attributeList.get(0)).getText().toString().trim();
        String email = ((TextInputEditText) super.attributeList.get(1)).getText().toString().trim();
        TextInputLayout textInputEmail = (TextInputLayout) super.attributeList.get(2);
        String password = ((TextInputEditText) super.attributeList.get(3)).getText().toString().trim();
        TextInputLayout textInputPassword = (TextInputLayout) super.attributeList.get(4);

        final String TAG = "CreateUser";



        if (FormatChecker.isValidEmail(email, textInputEmail) && FormatChecker.isValidPassword(password, textInputPassword)) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Account account = new Account(name, email, null);
                                //Testing
                                Restaurant r1 = new Restaurant(true, (float) 1, 5, (float) 5, "KFC", "76 Nanyang Dr, #01-04 NTU North Spine Plaza, Singapore 637331", 1.34729, 103.68080,"Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.NONE);
                                Restaurant r2 = new Restaurant(true, (float) 2, 4, (float) 4, "McDonald's", "76 Nanyang Drive, #01-08, NTU Block N2, #1, Singapore 637331", 0.00, 0.00, "Sedapz", "data:image/png;base64,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", 12, TypesOfDietaryRequirements.HALAL);
                                Restaurant r3 = new Restaurant(true, (float) 3, 3, (float) 3, "Fine Foods @ South Spine", "21 Nanyang Link, School of Physical and Mathematical Sciences, Singapore 637371", 0.00, 0.00, "Sedapz", "https://i0.wp.com/u-insight.com/wp-content/uploads/2020/01/food6.png?resize=860%2C644&ssl=1", 12, TypesOfDietaryRequirements.VEGETARIAN);
                                Restaurant r4 = new Restaurant(true, (float) 4, 2, (float) 2, "d", "Woodlands", 0.00, 0.00, "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.BOTH);

                                ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
                                restaurantArrayList.add(r1);
                                restaurantArrayList.add(r2);
                                restaurantArrayList.add(r3);
                                restaurantArrayList.add(r4);

                                account.setFullRestaurantList(restaurantArrayList);
                                account.setRecommendedList(restaurantArrayList);
                                //Testing ends

                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                                mDatabase.child(userID).child("Account").setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if (task1.isSuccessful()){

                                            // send email verification link
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Intent i = new Intent(context, InputPreferencesUI.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        context.startActivity(i);
                                                    }
                                                    else{
                                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            Toast.makeText(context, "Failed to create an account 1", Toast.LENGTH_SHORT).show();
                                            //delete user here if he fails
                                            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d(TAG, "User account deleted");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            } else { //when user account was already created before
                                Toast.makeText(context, "Failed to create an account 2", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
        }


    }
}
