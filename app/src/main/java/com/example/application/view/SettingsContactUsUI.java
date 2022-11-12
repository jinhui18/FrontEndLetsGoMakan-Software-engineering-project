package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

/**
 * @author Jin Hui
 * This class display the contact us page for our application for users to send an email to our customer service support team.
 * @version 1.0
 * @since 2022-11-11
 */
public class SettingsContactUsUI extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTextEmailAddress,editTextTextSubject, editTextTextMessage;
    private Button buttonSendEmail;

    /**
     * This method is called after the activity has launched but before it starts running.
     * This method sets on click listener for the "SEND EMAIL" button in the contact us page and make it clickable.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
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


        buttonSendEmail.setOnClickListener(this);
    }

    /**
     * When user press "SEND EMAIL" button, a pop up will appear containing all Google Application in their phone.
     * User can select the Gmail Icon in the pop up and the message will be pull from the application to their Gmail and user can press send email to send out the email directly.
     */
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

    /**
     * This method is called when user clicks on the "SEND EMAIL" button on the contact us page.
     * This method implements the logic when user decides to send an email to our support team.
     * It will check if the Subject and Message text is not empty.
     * If either the Subject or Message text is empty, a text will pop up on the user's screen telling them that the text cannot be empty respectively.
     * If both is valid, it will call the sendMail() method.
     * @param view is the ID of the button that user selects.
     */
    @Override
    public void onClick(View view) {
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

}
