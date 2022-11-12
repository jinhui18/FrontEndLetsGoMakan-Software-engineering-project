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


/**
 * @author Jin Hui
 * This class display the page to create an account after user clicked on the "Create new account" text in LoginUI class.
 * @version 1.0
 * @since 2022-11-11
 */
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


    /**
     * This method is called after the activity has launched but before it starts running.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
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

        textInputPassword.setError("Password must contain at least 8 characters,one letter, one upper and lowercase character and one special character.");

        //Firebase Authenticator
        mAuth = FirebaseAuth.getInstance();
        //Firebase Database
        mDataBase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //MVC related
        storeAccountModel = new StoreAccountModel(mAuth, mDataBase, AccountCreationUI.this);

        // need validate password and email address to fit the requirement
        confirmButton.setOnClickListener(this);


    }

    /**
     * This method is called when user clicks on the "CONFIRM" button.
     * This method will first check that the input of the name is not empty.
     * If that is true, this method will read the input of the email address and password entered by the user and calls the handleEvent() method in the Controller class.
     * If that is not true, an error message will be displayed.
     * @param view is the ID of the button that user selects.
     */
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
