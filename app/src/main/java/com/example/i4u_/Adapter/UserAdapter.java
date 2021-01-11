package com.example.i4u_.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


import com.example.i4u_.MainActivity;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.NotificationActivity;
import com.example.i4u_.R;
import com.example.i4u_.TargetProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final List<UserModel> uList;
    private final Context mContext;

    public UserAdapter(Context context, List<UserModel> userModelList){
        uList = userModelList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UserModel model = uList.get(position);
            holder.name.setText(model.getUserName());
            holder.age.setText(model.getAge());
//          Glide.with(mContext).load(model.getProfileUrl()).into(holder.imageView);
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   String userId = FirebaseAuth.getInstance().getUid();
                   DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("MyLikes").child(Objects.requireNonNull(userId)).child(model.getUid());
                   FirebaseDatabase.getInstance().getReference().child("MyLikes").child(model.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if(snapshot.exists()){
                               DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Matched");
                               reference1.child(userId).child(model.getUid()).setValue(true);
                               reference1.child(model.getUid()).child(userId).setValue(true);
                           }
//                           reference.removeValue();
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(mContext, "Failed to write", Toast.LENGTH_SHORT).show();
                       }
                   });
                    reference.setValue(true);

                    uList.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uList.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @SuppressLint("NonConstantResourceId")
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.edit:
                                    new AlertDialog.Builder(mContext)
                                            .setTitle("Attention")
                                            .setMessage("Block This User?")
                                            .setCancelable(true)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseDatabase.getInstance().getReference().child("Blocked").child(FirebaseAuth.getInstance().getUid()).child(model.getUid()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                                Toast.makeText(mContext, "Blocked User", Toast.LENGTH_SHORT).show();
                                                            mContext.startActivity(new Intent(mContext, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                        }
                                                    });
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).show();
                                    uList.remove(position);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.report:
                                    Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.inflate(R.menu.post_menu);
                    popupMenu.show();
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TargetProfileActivity.class);
                    intent.putExtra("tergetid",model.getUid());
                    mContext.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return uList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView,cancel,like,menu;
        TextView name,age;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_img);
            name = itemView.findViewById(R.id.user_name);
            age = itemView.findViewById(R.id.user_age);
            cancel = itemView.findViewById(R.id.user_cancel);
            like = itemView.findViewById(R.id.user_like);
            menu = itemView.findViewById(R.id.user_more);
        }
    }
}
