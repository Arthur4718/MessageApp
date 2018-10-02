package com.devarthur.messageapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {


    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        setupDisplayName();
        //Call the reference used to write and read data on Firebase.
        mDatabaseReference = FirebaseDatabase.getInstance() .getReference();



        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);


        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return false;
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void setupDisplayName(){
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null);

        if(mDisplayName == null) mDisplayName = "Anonymous";
    }




    private void sendMessage() {

        Log.d("App", "Message Sent");

        String userInput = mInputText.getText().toString();
        if(!userInput.equals("")){
            InstantMessage userMessage = new InstantMessage(userInput, mDisplayName);
            mDatabaseReference.child("messages").push().setValue(userMessage);
            mInputText.setText("");

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        mChatListView.setAdapter(mAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        //Stop checking for messages if the app is no longer visible to the user.
        mAdapter.cleanup();

    }

}
