package com.example.application.model;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.ShowMap;
import com.example.application.backend.control.others.FormatChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginUserModel extends Model{

    public LoginUserModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {
        // FORMAT: attributeList = [email, textInputEmail, password, textInputPassword]

        //Getting necessary variables
        String email = ((TextInputEditText) super.attributeList.get(0)).getText().toString();
        TextInputLayout textInputEmail = (TextInputLayout) super.attributeList.get(1);
        String password = ((TextInputEditText) super.attributeList.get(2)).getText().toString();
        TextInputLayout textInputPassword = (TextInputLayout) super.attributeList.get(3);

        //Login in process
        if (FormatChecker.isValidEmail(email, textInputEmail) && FormatChecker.isValidPassword(password, textInputPassword) )
        {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser().isEmailVerified() ) {
                            Toast.makeText(context, "User logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, ShowMap.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(context, "Please verify your email address", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e.getMessage().equals("The password is invalid or the user does not have a password.")){
                        Toast.makeText(context, "That's not the right password! Please try again or click on \"Forgot password\". ", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Invalid account. Please try a new email address or create a new account.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } //end of if
    }
}
