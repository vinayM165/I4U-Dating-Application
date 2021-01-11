package com.example.i4u_.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.i4u_.Adapter.DialogueAdapter;
import com.example.i4u_.Adapter.MatchedAdapter;
import com.example.i4u_.Adapter.MyLikesAdapter;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MatchedFragment extends Fragment {


    public MatchedFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView,recyclerViewC;
    MatchedAdapter matchedAdapter;
    DialogueAdapter dialogueAdapter;
    List<UserModel> list;
    List<String> stringList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_matched, container, false);
        recyclerView = view.findViewById(R.id.matched_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayout);
        list = new ArrayList<>();
        matchedAdapter = new MatchedAdapter(getContext(),list);
        recyclerView.setAdapter(matchedAdapter);

        recyclerViewC = view.findViewById(R.id.matched_chat_recycle);
        recyclerViewC.setHasFixedSize(true);
        LinearLayoutManager linearLayout1 = new LinearLayoutManager(getContext());
        linearLayout1.setReverseLayout(true);
        linearLayout1.getStackFromEnd();
        recyclerViewC.setLayoutManager(linearLayout1);
        dialogueAdapter = new DialogueAdapter(getContext(),list);
        recyclerViewC.setAdapter(dialogueAdapter);
        findMatched();
        return view;
    }



    private void findMatched() {
        stringList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Matched").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
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
                Toast.makeText(getContext(), "Failed to load.", Toast.LENGTH_SHORT).show();
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
                            Log.d("vinay", "onDataChange: " + model.getUserName());
                            list.add(model);
                        }
                    }
                }
                matchedAdapter.notifyDataSetChanged();
                dialogueAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}