package com.example.mowgli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "LoginActivity";
    private EditText etEmail, etPassword;
    private TextView forgotPassword, signUp;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // check if user is logged in
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgotPassword);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

        }
    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email!");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            etPassword.setError("Password must contain at least 6 characters!");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE); //Keep progress bar spinning till we log user in.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            Log.i(TAG, "User logged in successfully!");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()){
                                //redirect to main activity. can change this to user profile later on
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                            else{
                                user.sendEmailVerification();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                                Log.i(TAG, "Verification email sent to user!");
                            }
                        }else{
                            Log.d(TAG, "Failed to log user in due to invalid credentials");
                            Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}