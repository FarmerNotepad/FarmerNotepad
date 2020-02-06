package com.example.android.farmernotepad;

import androidx.appcompat.app.AppCompatActivity;

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

        final EditText feedbackText = (EditText) findViewById(R.id.feebackText);
        Button sendButton = (Button) findViewById(R.id.sendFeedback);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail(view);

            }
        });
    }

    public void sendEmail(View view){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    EmailHandler sender = new EmailHandler("farmernotepad@gmail.com",
                            "farmernotepad123");
                    sender.sendMail("Hello from JavaMail", "Body from JavaMail",
                            "farmernotepad@gmail.com", "farmernotepad@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();

    }
}
