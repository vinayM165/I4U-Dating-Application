package com.example.i4u_.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.i4u_.Adapter.MyLikesAdapter;
import com.example.i4u_.LoginActivity;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class LikesFragment extends Fragment {
    RecyclerView recyclerView;
    List<UserModel> list;
    List<String> stringList;
    MyLikesAdapter myLikesAdapter;


    public LikesFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_likes, container, false);
        recyclerView = view.findViewById(R.id.likes_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        myLikesAdapter = new MyLikesAdapter(getContext(),list);
        recyclerView.setAdapter(myLikesAdapter);
        findMyLikes();
        return view;
    }

    private void findMyLikes() {
        stringList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("MyLikes").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String uid = dataSnapshot.getKey();
                    stringList.add(uid);
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
                    for(String i : stringList){
                        assert model != null;
                        if(i.equals(model.getUid())) {
                            Log.d("vinay", "model: "+model.getUserName());
                            list.add(model);
                        }
                    }
                }
                myLikesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
     }
