package com.example.mowgli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mowgli.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private ProgressBar progressBar;
    public static final String TAG = "SignUpActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);
        registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        etUsername = findViewById(R.id.username);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.confirmPassword);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        if (username.isEmpty()){
            etUsername.setError("Username is required!");
            etUsername.requestFocus();
            return;
        }
        if (email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide a valid email address");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            etPassword.setError("Password must be at least 6 characters!");
            etPassword.requestFocus();
            return;

        }

        if (confirmPassword.isEmpty()){
            etConfirmPassword.setError("Re-enter the password!");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)){
            etConfirmPassword.setError("The passwords do not match. Re-enter the correct one!");
            etConfirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  User user = new User(username, email);

                  FirebaseDatabase.getInstance().getReference("Users")
                          .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                          .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {

                          if (task.isSuccessful()){
                              Log.i(TAG, "User successfully registered!");
                              Toast.makeText(SignUpActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                              progressBar.setVisibility(View.GONE);

                              FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                              currentUser.sendEmailVerification();
                              Log.i(TAG, "Verification email sent to user");

                              //redirect to main activity
                              startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                          }else {
                              Log.d(TAG, "Failed to register user");
                              Toast.makeText(SignUpActivity.this, "Failed to register user! Try again!", Toast.LENGTH_LONG).show();
                              progressBar.setVisibility(View.GONE);
                          }
                      }
                  });

              }else{
                  Log.d(TAG, "Failed to register user");
                  Toast.makeText(SignUpActivity.this, "Failed to register user! Try again!", Toast.LENGTH_LONG).show();
                  progressBar.setVisibility(View.GONE);
              }
            }
        });

    }
}