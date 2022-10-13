package com.example.application;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class LoginPage extends MainActivity implements View.OnClickListener {
    TextView textView_hp1;
    TextView textView_hp2;
    TextView textView_hp3;
    TextView textView_hp4;
    TextView textView_hp5;
    TextView textView_hp6;
    TextView textView_hp7;
    TextView textView_hp8;
    TextView textView_hp9;


    ImageView imageView_hp1;
    ImageView imageView_hp2;
    ImageView imageView_hp3;
    ImageView imageView_hp4;
    ImageView imageView_hp5;
    ImageView imageView_hp6;

    EditText email;
    EditText password;

    Button loginButton;
    Button button22;
    private int counter =5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_page);

        //initialise all
        textView_hp1 = (TextView) findViewById(R.id.textView1);
        textView_hp2 = (TextView) findViewById(R.id.textView2);
        textView_hp3 = (TextView) findViewById(R.id.textView3);
        textView_hp4 = (TextView) findViewById(R.id.textView4);
        textView_hp5 = (TextView) findViewById(R.id.textView5);
        textView_hp6 = (TextView) findViewById(R.id.textView6);
        textView_hp7 = (TextView) findViewById(R.id.textView7);
        textView_hp8 = (TextView) findViewById(R.id.textView8);
        textView_hp9 = (TextView) findViewById(R.id.textView9);


        imageView_hp1 = (ImageView) findViewById(R.id.imageView1);
        imageView_hp2 = (ImageView) findViewById(R.id.imageView2);
        imageView_hp3 = (ImageView) findViewById(R.id.imageView3);
        imageView_hp4 = (ImageView) findViewById(R.id.imageView4);
        imageView_hp5 = (ImageView) findViewById(R.id.imageView5);
        imageView_hp6 = (ImageView) findViewById(R.id.imageView6);


        email = (EditText) findViewById(R.id.editTextTextEmail);
        password = (EditText) findViewById(R.id.editTextTextPassword);

        loginButton = (Button) findViewById(R.id.loginButton);
        button22 = (Button) findViewById(R.id.button2) ;

        button22.setOnClickListener(this);




    }

    public void validate(String email, String password){
        if ( (email == "Admin") && (password == "admin") ) {
            Intent intent = new Intent(LoginPage.this, HomePage.class  );
            startActivity(intent);
        }
        else{
            counter--;

            if (counter == 0){
                //prevent login
                loginButton.setEnabled(false);
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), HomePage.class  );
        startActivity(intent);

    }
}
