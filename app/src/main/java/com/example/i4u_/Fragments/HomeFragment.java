package com.example.i4u_.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.i4u_.Adapter.UserAdapter;
import com.example.i4u_.ChatAPI.DialogueActivity;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.NotificationActivity;
import com.example.i4u_.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList;
    ProgressBar progress_circular;
    FirebaseUser firebaseUser;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView title =view.findViewById(R.id.home_fulllname);
        ImageView notification = view.findViewById(R.id.home_notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DialogueActivity.class));
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), userList);
        progress_circular = view.findViewById(R.id.progress_circular);
        recyclerView.setAdapter(userAdapter);

        findUsers();
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void findUsers(){

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
             String  gender = snapshot.child("userGender").getValue(String.class);
             switch (gender){
                 case "M" :
                    Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("userGender").equalTo("F");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                UserModel model = dataSnapshot.getValue(UserModel.class);
//                                checkLikedBlock(model);
                                userList.add(model);
                            }
                            userAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
                        }
                    });
                     break;
                 case "F" :
                     Query query1 = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("userGender").equalTo("M");
                     query1.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                 UserModel model = dataSnapshot.getValue(UserModel.class);
                                 userList.add(model);
                             }
                             userAdapter.notifyDataSetChanged();
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {
                             Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
                         }
                     });
                     break;
             }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
           }
       });
    }

    private void checkLikedBlock(UserModel model) {
        FirebaseDatabase.getInstance().getReference().child("Blocked").child(model.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("MyLikes").child(firebaseUser.getUid()).child(model.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists()){
                                userList.add(model);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
