package com.example.i4u_.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.ChatAPI.OpenChatActivity;
import com.example.i4u_.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DialogueAdapter extends RecyclerView.Adapter<DialogueAdapter.DialogueHolder> {
    Context context;
    List<UserModel> uList;

    public DialogueAdapter(Context context, List<UserModel> uList) {
        this.context = context;
        this.uList = uList;
    }
    @NonNull
    @Override
    public DialogueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialogue_item,parent,false);
        return new DialogueHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogueHolder holder, int position) {
            UserModel model = uList.get(position);
            holder.username.setText(model.getUserName());
            FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
                assert model != null;
                Glide.with(context).load(model.getProfileUrl()).into(holder.profile);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OpenChatActivity.class);
                    intent.putExtra("targetchatid",model.getUid());
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return uList.size();
    }

    static class DialogueHolder extends RecyclerView.ViewHolder{
        TextView username;
        ImageView profile;
        public DialogueHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.dialogue_userName);
            profile = itemView.findViewById(R.id.dialogue_userImage);
        }
    }

}
