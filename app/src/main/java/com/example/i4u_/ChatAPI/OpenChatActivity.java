package com.example.i4u_.ChatAPI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.i4u_.Adapter.OpenChatAdapter;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.R;
import com.example.i4u_.Util.Calculateuid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OpenChatActivity extends AppCompatActivity {
    EditText msgBox;
    ImageButton imageButton;
    RecyclerView recyclerView;
    TextView username;
    ImageView back;
    List<Message> messageList;
    OpenChatAdapter openChatAdapter;
    String finalTarget;
    String target;
    FirebaseUser firebaseUser;
    ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_chat);
        profile = findViewById(R.id.openchat_imgProfile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        target = intent.getStringExtra("targetchatid");
        assert target != null;
        assert firebaseUser != null;
        finalTarget= new Calculateuid().setOneToOneChat(firebaseUser.getUid(),target);
        if(finalTarget=="error"){
            Toast.makeText(this, "Some error has occured", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        Log.d("vinay", "target:" + target + "finalTarget " + finalTarget);
        imageButton =findViewById(R.id.btnSendMessage);
        msgBox = findViewById(R.id.etMessage);
        username = findViewById(R.id.tvUserName);
        back = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        messageList = new ArrayList<>();
        openChatAdapter = new OpenChatAdapter(getBaseContext(),messageList);
        recyclerView.setAdapter(openChatAdapter);
        FirebaseDatabase.getInstance().getReference().child("Users").child(target).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
                assert model != null;
                Glide.with(getBaseContext()).load(model.getProfileUrl()).into(profile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        readMessages();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(target).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
                username.setText(model.getUserName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats").child(finalTarget).push();
                String text = msgBox.getText().toString();
                if(!TextUtils.isEmpty(text)){
                    Message message = new Message(text, System.currentTimeMillis());
                    message.setSenderID(firebaseUser.getUid());
                    message.setReceiverID(target);
                    msgBox.setText("");
                    reference.setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(OpenChatActivity.this,"Message Sent!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(OpenChatActivity.this, "Message Not Sent!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void readMessages() {
        assert finalTarget != null;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(finalTarget);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                openChatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OpenChatActivity.this, "Failed to load!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}