package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final EditText feedbackText = (EditText) findViewById(R.id.feebackText);
        Button sendButton = (Button) findViewById(R.id.sendFeedback);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EmailHandler sender = new EmailHandler("farmernotepad@gmail.com","farmernotepad123");
                    sender.sendMail("User feedback",feedbackText.getText().toString(),"farmernotepad@gmail.com","nickspo93@hotmail.com");
                }
                catch (Exception e) {
                    Log.e("Send Mail", e.getMessage(), e);
                }
            }
        });
    }
}
