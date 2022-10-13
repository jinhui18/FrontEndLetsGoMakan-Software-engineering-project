package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView textView_hp1;
    TextView textView_hp2;
    TextView textView_hp3;
    TextView textView_hp4;
    TextView textView_hp5;
    TextView textView_hp6;
    TextView textView_hp7;

    ImageView imageView_hp1;
    ImageView imageView_hp2;
    ImageView imageView_hp3;
    ImageView imageView_hp4;

    Button buttonLogin;

    TextInputLayout textInputEmail;
    TextInputEditText email;

    TextInputLayout textInputPassword;
    TextInputEditText password;

    FirebaseAuth mAuth;
    
    private int counter =5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise all
        textView_hp1 = (TextView) findViewById(R.id.textView1);
        textView_hp2 = (TextView) findViewById(R.id.textView2);
        textView_hp3 = (TextView) findViewById(R.id.textView3);
        textView_hp4 = (TextView) findViewById(R.id.textView4);
        textView_hp5 = (TextView) findViewById(R.id.textView5);
        textView_hp6 = (TextView) findViewById(R.id.textView6);
        textView_hp7 = (TextView) findViewById(R.id.textView7);



        imageView_hp1 = (ImageView) findViewById(R.id.imageView1);
        imageView_hp2 = (ImageView) findViewById(R.id.imageView2);
        imageView_hp3 = (ImageView) findViewById(R.id.imageView3);
        imageView_hp4 = (ImageView) findViewById(R.id.imageView4);


        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email =  findViewById(R.id.TextInputEditEmail);

        textInputPassword = findViewById(R.id.textInputLayoutPassword);
        password = findViewById(R.id.TextInputEditPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin) ;

        //When login button is clicked
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(email.getText().toString(),password.getText().toString());
            }
        });

        protected void onStart() {
            super.onStart();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null){
                startActivity(new Intent(MainActivity.this, HomePage.class));
            }
        }


        // press forget password will change the screen
        textView_hp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResetPassword.class  );
                startActivity(intent);
            }
        });

        // press create new account will change the screen
        textView_hp7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateNewAccount.class  );
                startActivity(intent);
            }
        });

    }

    public void validate(String email, String password){
        if ( (email.equals("admin") ) && (password.equals("admin")) ) {
            Intent intent = new Intent(MainActivity.this, CreateNewAccount.class  );
            startActivity(intent);
        }
        // If login fail, counter will minus off, prompt customer to reset password??
        else {
            counter--;

            if (counter < 0){
                // print out a message, idk how to change position of toast
                Toast toast = Toast.makeText(getApplicationContext(), "Please reset your password",Toast.LENGTH_SHORT);
                toast.setGravity( Gravity.CENTER_VERTICAL , 0, 0);
                toast.show();
            }
            Boolean validateEmail,validatePassword;
            validateEmail = validateEmail(email);
            validatePassword = validatePassword(password);
            if (validateEmail && validatePassword  ){
                Intent intent = new Intent(MainActivity.this, HomePage.class  );
                startActivity(intent);
            }
        }
    }

    private boolean validateEmail(String email) {

        if (email.equals("")) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }


    private boolean validatePassword(String password) {

        if (password.equals("")) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }



}