package com.example.i4u_.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i4u_.Model.UserModel;
import com.example.i4u_.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    Context mContext;
    List<UserModel> mList;

    public NotificationAdapter(Context mContext, List<UserModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notificaion_item,parent,false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
            UserModel model = mList.get(position);
            holder.username.setText(model.getUserName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class NotificationHolder extends RecyclerView.ViewHolder{
        ImageView not_profile;
        TextView username;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.notification_username);
        }
    }

}
