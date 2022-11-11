package com.example.application.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.controller.Controller;
import com.example.application.model.Model;
import com.example.application.model.StoreAccountModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AccountCreationUI extends AppCompatActivity implements View.OnClickListener {

    //Widgets
    private TextInputEditText name;
    private TextInputLayout textInputPassword;
    private Button confirmButton;
    private TextInputEditText password;
    private TextInputLayout textInputEmail;
    private TextInputEditText email;

    //Firebase Authenticator
    private FirebaseAuth mAuth;

    //Firebase Database
    private DatabaseReference mDataBase;

    //MVC
    private Model storeAccountModel;



    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Instantiating widgets
        name = findViewById(R.id.TextInputEditName);
        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email = findViewById(R.id.TextInputEditEmail);
        textInputPassword = findViewById(R.id.textInputLayoutPassword);
        password = findViewById(R.id.TextInputEditPassword);
        confirmButton = (Button) findViewById(R.id.confirmButton);

        //Firebase Authenticator
        mAuth = FirebaseAuth.getInstance();
        //Firebase Database
        mDataBase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //MVC related
        storeAccountModel = new StoreAccountModel(mAuth, mDataBase, AccountCreationUI.this);

        // need validate password and email address to fit the requirement
        confirmButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        ArrayList<Object> list = new ArrayList<Object>();
        if (!name.getText().toString().isEmpty()){
            list.add(name); list.add(email); list.add(textInputEmail);
            list.add(password); list.add(textInputPassword);

            Controller storeAccountController = new Controller(storeAccountModel,list);
            storeAccountController.handleEvent();
        }
        else{
            Toast.makeText(AccountCreationUI.this,"Name cannot be empty", Toast.LENGTH_SHORT).show();
            name.requestFocus();
        }

    }
}
