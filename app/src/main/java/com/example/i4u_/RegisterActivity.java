package com.example.i4u_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.i4u_.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class RegisterActivity extends AppCompatActivity {
    public static final String M = "M",F = "F";
    FirebaseAuth fAuth;
    EditText uName,fullName,contact,email,password,age;
    ProgressDialog pd;
    Button register;
    RadioRealButton male;
    RadioRealButton female ;
    RadioRealButtonGroup group;
    DatabaseReference reference;
    String Gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();
        uName = findViewById(R.id.login_username);
        fullName = findViewById(R.id.reg_fullname);
        contact = findViewById(R.id.reg_contact);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.login_password);
        register = findViewById(R.id.reg_register_btn);
        male = (RadioRealButton) findViewById(R.id.male_btn);
        female = (RadioRealButton) findViewById(R.id.female_btn);
         group = (RadioRealButtonGroup) findViewById(R.id.radio_grp);
         age = findViewById(R.id.reg_age);

        group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                Toast.makeText(RegisterActivity.this, "button clicked on position" + position, Toast.LENGTH_SHORT).show();
                Gender =position==0?"M" : "F";
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String str_username = uName.getText().toString();
                String str_fullname = fullName.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();
                String str_contact = contact.getText().toString();
                String str_age = age.getText().toString();

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_contact) || Gender == "" ){
                    Toast.makeText(RegisterActivity.this, "All fields are required! Gender : " +Gender, Toast.LENGTH_SHORT).show();
                } else if(str_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    pd = new ProgressDialog(RegisterActivity.this);
                    pd.setMessage("Please wait...");
                    pd.show();
                    register(str_username, str_fullname, str_email, str_password, str_contact,str_age);
                }
            }
        });
    }

    public void register(final String username, final String fullname, String email, String password, final String contact,final String age){
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userID = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            UserModel userModel = new UserModel(userID, fullname, username,age, contact, email, password, Gender);
                            userModel.setBio("");
                            userModel.setProfileUrl("https://firebasestorage.googleapis.com/v0/b/i4u-efa08.appspot.com/o/Dafault_profile%2Fprofile_circle.png?alt=media&token=9556bb87-5f8c-4d91-843c-bd860a22759a");
                            Log.d("vinay", " " + username + userModel.getAge() + userModel.getContactNo() + userModel.getUserGender());
                            reference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Registration Complete!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Some problem occured.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Exception " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}




