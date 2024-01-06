package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private ListView messageListView;
    private MessageAdapter messageAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference messagesDatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
//        int atIndex = firebaseUser.getEmail().indexOf("@");
//        String username = firebaseUser.getEmail().substring(0,atIndex);
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(username)
//                .build();
//        firebaseUser.updateProfile(profileUpdates);



        if (firebaseUser != null) {
            // Your code here
            int atIndex = firebaseUser.getEmail().indexOf("@");
            String username = firebaseUser.getEmail().substring(0, atIndex);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();
            firebaseUser.updateProfile(profileUpdates);
            // Rest of your code
        } else {
            // Handle the case where the user is not authenticated
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }





        messagesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("messages");
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messageListView = findViewById(R.id.messageListView);

        messageAdapter = new MessageAdapter(this, new ArrayList<>());
        messageListView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        messagesDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MyMessage message = snapshot.getValue(MyMessage.class);
                messageAdapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendMessage(){
        String messageText = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(messageText)){
            String currentUserUid = firebaseUser.getUid();
            MyMessage message = new MyMessage(firebaseUser.getDisplayName(),messageText, isMessageOutgoing(currentUserUid));
            messagesDatabaseReference.push().setValue(message);
            messageEditText.setText("");
        }else{
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMessageOutgoing(String senderUserId) {
        return firebaseUser != null && firebaseUser.getUid().equals(senderUserId);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is not authenticated, redirect to login screen
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);
    }

}