package com.example.gigglegossip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {

    String reciverimg, reciverUid, reciverName, SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    ImageView sendbtn;
    EditText textmsg;
    RecyclerView mmessangesadpter;
    ArrayList<msgModelclass> messagessArraylist;
    messagesAdpter messagesAdpter;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String reciverIImg;
    String senderRoom, reciverRoom;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_win);

        // Initialize Firebase Auth and Database
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Initialize UI elements
        mmessangesadpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessangesadpter.setLayoutManager(linearLayoutManager);

        messagessArraylist = new ArrayList<>();
        messagesAdpter = new messagesAdpter(chatWin.this, messagessArraylist);
        mmessangesadpter.setAdapter(messagesAdpter);

        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        profile = findViewById(R.id.profileimgg);
        reciverNName = findViewById(R.id.recivername);

        // Get intent extras
        reciverName = getIntent().getStringExtra("nameee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        // Display receiver info
        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText(reciverName);

        SenderUID = firebaseAuth.getUid();

        senderRoom = SenderUID + reciverUid;
        reciverRoom = reciverUid + SenderUID;

        // Fetch user details and messages
        DatabaseReference reference = database.getReference().child("user").child(SenderUID);
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.child("profilepic").getValue() != null) {
                    senderImg = snapshot.child("profilepic").getValue().toString();
                    reciverIImg = reciverimg;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chatWin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagessArraylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelclass messages = dataSnapshot.getValue(msgModelclass.class);
                    messagessArraylist.add(messages);
                }
                messagesAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chatWin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(chatWin.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");  // Clear the input field
                Date date = new Date();
                msgModelclass messagess = new msgModelclass(message, SenderUID, date.getTime());

                DatabaseReference senderMessagesRef = database.getReference().child("chats").child(senderRoom).child("messages");
                DatabaseReference receiverMessagesRef = database.getReference().child("chats").child(reciverRoom).child("messages");

                senderMessagesRef.push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            receiverMessagesRef.push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Success
                                        Toast.makeText(chatWin.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Failed to send message to receiver room
                                        Toast.makeText(chatWin.this, "Failed to send message to receiver room", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // Failed to send message to sender room
                            Toast.makeText(chatWin.this, "Failed to send message to sender room", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
