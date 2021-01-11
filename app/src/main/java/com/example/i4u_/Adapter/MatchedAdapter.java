package com.example.i4u_.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.R;
import com.example.i4u_.TargetProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MatchedAdapter extends RecyclerView.Adapter<MatchedAdapter.MatchedHolder> {
Context context;
List<UserModel> uList;

    public MatchedAdapter(Context context, List<UserModel> uList) {
        this.context = context;
        this.uList = uList;
    }

    @NonNull
    @Override
    public MatchedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.matched_item,parent,false);
        return new MatchedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchedHolder holder, int position) {
            UserModel model = uList.get(position);
            holder.username.setText(model.getFullName());
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

    }

    @Override
    public int getItemCount() {
        return uList.size();
    }

    static class MatchedHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView username,fullname;
        public MatchedHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.matchitem_username);
            profile = itemView.findViewById(R.id.matched_profile);
        }
    }

}
