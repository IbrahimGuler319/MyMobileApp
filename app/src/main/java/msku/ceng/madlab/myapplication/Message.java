package msku.ceng.madlab.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Message extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private LinearLayout messageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messageContainer = findViewById(R.id.messageContainer);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString();

        displayMessage("User1", message);

        messageEditText.getText().clear();
    }

    private void displayMessage(String sender, String text) {

        TextView messageTextView = new TextView(this);
        messageTextView.setText(sender + ": " + text);

        messageContainer.addView(messageTextView);
    }
}
