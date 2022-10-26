package com.example.application;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateNewAccount extends AppCompatActivity {

    private ImageView imageView1, imageView2, imageView3, imageView4;
    private TextView textView1, textView2, textView3, textView4;

    private TextInputLayout textInputName;
    private TextInputEditText name;

    private TextInputLayout textInputPassword;
    private Button confirmButton;
    private TextInputEditText password;
    private TextInputLayout textInputEmail;
    private TextInputEditText email;

    private FirebaseAuth mAuth;

    // creating a variable for our Database
    // Reference for Firebase.
    private DatabaseReference mDataBase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);

        textInputName = findViewById(R.id.textInputLayoutName);
        name = findViewById(R.id.TextInputEditName);

        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email = findViewById(R.id.TextInputEditEmail);

        textInputPassword = findViewById(R.id.textInputLayoutPassword);
        password = findViewById(R.id.TextInputEditPassword);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        mAuth = FirebaseAuth.getInstance();


        // below line is used to get the
        // instance of our Firebase database.
       // firebaseDatabase = FirebaseDatabase.getInstance();
        mDataBase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        // need validate password and email address to fit the requirement
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameInput = name.getText().toString().trim();
                String emailInput = email.getText().toString().trim();
                String passwordInput = password.getText().toString().trim();
                CreateUser createUser = new CreateUser(nameInput, emailInput,passwordInput,CreateNewAccount.this);
                createUser.handleEvent(nameInput,emailInput, passwordInput,textInputEmail,textInputPassword);
            }
        });


    }

}

