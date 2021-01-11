package com.example.i4u_;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.i4u_.RegisterActivity.M;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login;
    TextView txt_signup;

    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_login_btn);
        txt_signup = findViewById(R.id.txt_signup);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        txt_signup.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                        pd.setMessage("Please wait...");
                        pd.show();

                        String str_email = email.getText().toString();
                        String str_password = password.getText().toString();

                        if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                            Toast.makeText(LoginActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                        } else {

                            auth.signInWithEmailAndPassword(str_email, str_password)
                                    .addOnCompleteListener(LoginActivity.this, task -> {
                                        if (task.isSuccessful()) {

                                            pd.dismiss();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            pd.dismiss();
                                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }
}