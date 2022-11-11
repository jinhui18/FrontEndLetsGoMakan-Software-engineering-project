package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class SettingsContactUsUI extends AppCompatActivity {

    private EditText editTextTextEmailAddress,editTextTextSubject, editTextTextMessage;
    private Button buttonSendEmail;

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_contact_us);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextSubject = findViewById(R.id.editTextTextSubject);
        editTextTextMessage = findViewById(R.id.editTextTextMessage);
        buttonSendEmail = findViewById(R.id.buttonSendEmail);
        editTextTextEmailAddress.setText("lets_go_makan_support@gmail.com");


        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextTextSubject.getText().toString().isEmpty()){
                    if(!editTextTextMessage.getText().toString().isEmpty()){
                        sendMail();
                    }else{
                        Toast.makeText(SettingsContactUsUI.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                        editTextTextMessage.requestFocus();
                    }
                }else{
                    Toast.makeText(SettingsContactUsUI.this, "Subject cannot be empty", Toast.LENGTH_SHORT).show();
                    editTextTextSubject.requestFocus();
                }
            }
        });



    }

    private void sendMail() {
        String recipientList = editTextTextEmailAddress.getText().toString();
        String[] recipients = recipientList.split(",");

        String subject = editTextTextSubject.getText().toString();
        String message = editTextTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }
}
