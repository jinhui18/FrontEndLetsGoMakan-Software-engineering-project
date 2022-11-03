package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.controller.LoginUserController;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView textView_hp1, textView_hp2, textView_hp3, textView_hp4,textView_hp5 ,textView_hp6, textView_hp7;
    private ImageView imageView_hp1, imageView_hp2, imageView_hp3, imageView_hp4;


    private Button buttonLogin;

    private TextInputLayout textInputEmail;
    private TextInputEditText email;

    private TextInputLayout textInputPassword;
    private TextInputEditText password;

    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private int counter = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating widgets
        textView_hp1 = findViewById(R.id.textView1);
        textView_hp2 = findViewById(R.id.textView2);
        textView_hp3 = findViewById(R.id.textView3);
        textView_hp4 = findViewById(R.id.textView4);
        textView_hp5 = findViewById(R.id.textView5);
        textView_hp6 = findViewById(R.id.textView6);
        textView_hp7 = findViewById(R.id.textView7);
        imageView_hp1 = findViewById(R.id.imageView1);
        imageView_hp2 = findViewById(R.id.imageView2);
        imageView_hp3 = findViewById(R.id.imageView3);
        imageView_hp4 = findViewById(R.id.imageView4);
        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email = findViewById(R.id.TextInputEditEmail);
        textInputPassword = findViewById(R.id.textInputLayoutPassword);
        password = findViewById(R.id.TextInputEditPassword);
        buttonLogin = findViewById(R.id.buttonLogin);


        //Firebase
        mAuth = FirebaseAuth.getInstance();

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Account");


        //When login button is clicked
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, CreateNewAccount.class));

                //LoginUser.handleEvent()
                //loginUser(email.getText().toString(), password.getText().toString(),textInputEmail,textInputPassword );
                LoginUserController loginUser = new LoginUserController(email.getText().toString(),password.getText().toString(),MainActivity.this);
                loginUser.handleEvent(email.getText().toString(), password.getText().toString(),textInputEmail,textInputPassword);
            }
        });


        // press forget password will change the screen
        textView_hp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayRestaurantsList.class);
                startActivity(intent);
            }
        });

        // press create new account will change the screen
        textView_hp7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNewAccount.class);
                startActivity(intent);
            }
        });

    }
    // end the top part

    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            //startActivity(new Intent(MainActivity.this, HomePage.class));
        }
    }






}








