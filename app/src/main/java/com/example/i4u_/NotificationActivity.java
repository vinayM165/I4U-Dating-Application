package com.example.i4u_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.i4u_.Adapter.NotificationAdapter;
import com.example.i4u_.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    NotificationAdapter notificationAdapter;
    List<UserModel> list;
    List<String> sList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        RecyclerView recyclerView = findViewById(R.id.notification_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        list = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(notificationAdapter);
        sList = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("MyLikes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child(uid).exists()){
                        Log.d("vinay", "onDataChange: " + dataSnapshot.getKey());
                        String model = dataSnapshot.getKey();
                        sList.add(model);
                    }
                }
                readUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUser() {
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    for(String i : sList){
                        assert model != null;
                        if(i.equals(model.getUid())) {
                            list.add(model);
                        }
                    }
                }
                Log.d("vinay", "onDataChange: " + list.size());
                notificationAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}