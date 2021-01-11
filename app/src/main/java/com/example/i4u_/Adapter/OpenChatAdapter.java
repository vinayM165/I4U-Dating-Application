package com.example.i4u_.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.i4u_.ChatAPI.Message;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OpenChatAdapter extends RecyclerView.Adapter<OpenChatAdapter.ChatHolder> {
    private final int MESSAGE_TYPE_RIGHT =0,MESSAGE_TYPE_LEFT =1;
    private final Context mContext;
    private final List<Message> messageList;
    FirebaseUser firebaseUser ;

    public OpenChatAdapter(Context mContext, List<Message> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MESSAGE_TYPE_LEFT){
             view = LayoutInflater.from(mContext).inflate(R.layout.item_left,parent,false);
        }else{
             view = LayoutInflater.from(mContext).inflate(R.layout.item_right,parent,false);
        }
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
            Message message = messageList.get(position);
            holder.textMessage.setText(message.getMessageText());
            if(message.getSenderID().equals(firebaseUser.getUid())){
                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        assert model != null;
                        Glide.with(mContext).load(model.getProfileUrl()).into(holder.img);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else{
                String senderid = message.getSenderID();
                FirebaseDatabase.getInstance().getReference().child("Users").child(senderid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        assert model != null;
                        Glide.with(mContext).load(model.getProfileUrl()).into(holder.img);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(messageList.get(position).getSenderID().equals(firebaseUser.getUid())){
            return MESSAGE_TYPE_RIGHT;
        }else{
            return MESSAGE_TYPE_LEFT;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ChatHolder extends RecyclerView.ViewHolder{
        TextView textMessage;
        ImageView img;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.tvMessage);
            img = itemView.findViewById(R.id.item_right_userImage);
        }
    }
}
