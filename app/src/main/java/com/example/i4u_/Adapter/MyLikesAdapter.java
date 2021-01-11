package com.example.i4u_.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.i4u_.Model.UserModel;
import com.example.i4u_.R;
import com.example.i4u_.TargetProfileActivity;

import java.util.List;

public class MyLikesAdapter extends RecyclerView.Adapter<MyLikesAdapter.MyLikesHolder> {
    private final Context mContext;
    private final List<UserModel> mUsers;

    public MyLikesAdapter(Context mContext, List<UserModel> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public MyLikesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mylikes_item,parent,false);
        return new MyLikesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLikesHolder holder, int position) {
        UserModel model = mUsers.get(position);
        holder.fullname.setText(model.getFullName());
        holder.Username.setText(model.getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TargetProfileActivity.class);
                intent.putExtra("tergetid",model.getUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class MyLikesHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView Username,fullname;
        Button btn_follow;
        public MyLikesHolder(@NonNull View itemView) {
            super(itemView);
            profile  = itemView.findViewById(R.id.mylikes_image_profile);
            Username = itemView.findViewById(R.id.mylikeitem_username);
            fullname = itemView.findViewById(R.id.mylikeitem_fullname);

        }
    }

}
