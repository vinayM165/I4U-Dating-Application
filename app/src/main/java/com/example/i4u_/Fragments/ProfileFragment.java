package com.example.i4u_.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.i4u_.EditProfileActivity;
import com.example.i4u_.Model.UserModel;
import com.example.i4u_.OptionsActivity;
import com.example.i4u_.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    LinearLayout l1,l2,l3,l4;
    DatabaseReference reference;

    public ProfileFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView name = view.findViewById(R.id.profile_name);
        TextView bio = view.findViewById(R.id.profile_bio);
        TextView fullname = view.findViewById(R.id.profile_fulllname);
        ImageView image = (ImageView)view.findViewById(R.id.profile_image);
        ImageView options = (ImageView)view.findViewById(R.id.profile_options);

        reference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel model = snapshot.getValue(UserModel.class);
              Glide.with(getContext()).load(model.getProfileUrl()).into(image);
                name.setText(model.getUserName());
                fullname.setText(model.getFullName());
                bio.setText(model.getBio());

          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {
              Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
          }
      });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OptionsActivity.class));
            }
        });


        l1 = view.findViewById(R.id.profile_l1);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
        l2 = view.findViewById(R.id.prfile_l2);
        l3 = view.findViewById(R.id.prfile_l3);
        l4 = view.findViewById(R.id.prfile_l4);
        return view;
    }



}