package com.example.android.farmernotepad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityFeedback extends AppCompatActivity {

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

    public void sendEmail(View view) {
        final EditText feedbackText = (EditText) findViewById(R.id.feedbackText);

        ProgressDialog pDialog = new ProgressDialog(ActivityFeedback.this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (GenericUtils.isOnline()) {
                    try {
                        EmailHandler sender = new EmailHandler("farmernotepad@gmail.com",
                                "farmernotepad123");
                        sender.sendMail("User Feedback", feedbackText.getText().toString(),
                                "farmernotepad@gmail.com", "farmernotepad@gmail.com");
                        pDialog.dismiss();
                        GenericUtils.toast(getApplicationContext(), "Feedback sent to developers");
                        Intent intent = new Intent(ActivityFeedback.this, MainActivity.class);
                        startActivity(intent);
                        ActivityFeedback.this.finish();
                    } catch (Exception e) {
                        Log.e("SendMail", e.getMessage(), e);
                        pDialog.dismiss();
                        GenericUtils.toast(getApplicationContext(), "Error sending email");
                    }
                } else {
                    pDialog.dismiss();
                    GenericUtils.toast(getApplicationContext(), "No internet connection found");
                }
            }

        }).start();

    }
}
