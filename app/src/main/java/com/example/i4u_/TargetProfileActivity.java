package com.example.i4u_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i4u_.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TargetProfileActivity extends AppCompatActivity {
    TextView username,age,bio,Gender,block;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        username = findViewById(R.id.target_username);
        age = findViewById(R.id.target_age);
        bio = findViewById(R.id.target_bio);
        Gender = findViewById(R.id.target_gender);
        block = findViewById(R.id.target_block);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        String targetID = intent.getStringExtra("tergetid");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(targetID);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
                assert model!= null;
                username.setText(model.getUserName()+", ");
                age.setText(model.getAge());
                bio.setText(model.getBio());
                Gender.setText(model.getUserGender().equals("M") ?"Male":"Female");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TargetProfileActivity.this)
                        .setTitle("Attention")
                        .setMessage("Block This User?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Blocked").child(firebaseUser.getUid()).child(targetID).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(TargetProfileActivity.this, "Blocked User", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
            }
        });

    }
}