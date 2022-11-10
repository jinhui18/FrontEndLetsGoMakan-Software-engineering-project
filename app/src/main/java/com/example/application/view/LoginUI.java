package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.ShowMap;
import com.example.application.controller.Controller;
import com.example.application.model.LoginUserModel;
import com.example.application.model.Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginUI extends AppCompatActivity {

    //Widgets
    private TextView textView_hp1, textView_hp2, textView_hp3, textView_hp4,textView_hp5 ,textView_hp6, textView_hp7;
    private ImageView imageView_hp1, imageView_hp2, imageView_hp3, imageView_hp4;
    private Button buttonLogin;
    private TextInputLayout textInputEmail;
    private TextInputEditText email;
    private TextInputLayout textInputPassword;
    private TextInputEditText password;
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //MVC
    private Model loginUserModel;



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
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //MVC
        loginUserModel = new LoginUserModel(mAuth, mDatabase, LoginUI.this);

        //CLicking of buttons
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Object> list = new ArrayList<Object>();
                list.add(email); list.add(textInputEmail); list.add(password); list.add(textInputPassword);
                Controller loginController = new Controller(loginUserModel,list);
                loginController.handleEvent();
            }
        });


        // press forget password will change the screen
        textView_hp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginUI.this, ChangePreferencesUI.class);
                startActivity(intent);
            }
        });

        // press create new account will change the screen
        textView_hp7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginUI.this, AccountCreationUI.class);
                startActivity(intent);
            }
        });

    }
    // end the top part

    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(LoginUI.this, ShowMap.class);
            startActivity(intent);
            //startActivity(new Intent(MainActivity.this, HomePage.class));
        }
    }

}








