package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Button sendButton = (Button) findViewById(R.id.sendFeedback);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(view);

            }
        });
    }

    public void sendEmail(View view){
        final EditText feedbackText = (EditText) findViewById(R.id.feebackText);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    EmailHandler sender = new EmailHandler("farmernotepad@gmail.com",
                            "farmernotepad123");
                    sender.sendMail("User Feedback", feedbackText.getText().toString(),
                            "farmernotepad@gmail.com", "farmernotepad@gmail.com");
                    Toast.makeText(FeedbackActivity.this,"Feedback sent to devs.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                    startActivity(intent);
                    FeedbackActivity.this.finish();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(FeedbackActivity.this,"Error sending email.",Toast.LENGTH_SHORT).show();
                }
            }

        }).start();

    }
}
