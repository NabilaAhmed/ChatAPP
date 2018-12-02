package com.example.android.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class chat_Room extends AppCompatActivity {
    String chatMess,chatUser;

    EditText message;
    Button send;
    TextView Messages;
    String userName, Room_Name;
DatabaseReference databaseReference;
    private String root_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room);


        send = (Button) findViewById(R.id.SEND);
        message = (EditText) findViewById(R.id.editText1);
        Messages = (TextView) findViewById(R.id.textView1);
        userName = getIntent().getExtras().get("User_name").toString();
        Room_Name = getIntent().getExtras().get("Room_name").toString();
        setTitle("Room-"+Room_Name);
        databaseReference= FirebaseDatabase.getInstance().getReference().child(Room_Name);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map=new HashMap<String, Object>();
                root_key=databaseReference.push().getKey();
//                map.put("","");
                databaseReference.updateChildren(map);
                DatabaseReference message_room=databaseReference.child(root_key);
                Map<String,Object> map1=new HashMap<String, Object>();
                map1.put("Name :",userName);
                map1.put("Message",message.getText().toString());
                message_room.updateChildren(map1);

            }
        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                appendChatConversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {
        Iterator i=dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
          chatMess= (String) ((DataSnapshot)i.next()).getValue();
          chatUser= (String) ((DataSnapshot)i.next()).getValue();
            Messages.append(chatUser+" :"+chatMess+" \n");

        }

    }
}
