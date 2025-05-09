package com.example.chatterly.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatterly.R;
import com.example.chatterly.data.local.AuthenticationAPI;
import com.example.chatterly.data.local.TokenManager;
import com.example.chatterly.data.repository.AuthenticationRepository;
import com.example.chatterly.model.authentication.LoginModel;
import com.example.chatterly.model.authentication.RegisterModel;
import com.example.chatterly.model.authentication.TokensModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    @Inject
    AuthenticationAPI authenticationAPI;

    @Inject
    TokenManager tokenManager;

    @Inject
    AuthenticationRepository authenticationRepository;

    private EditText firstnameEditText, lastnameEditText, emailEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        lastnameEditText = findViewById(R.id.lastnameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.registerButton).setOnClickListener(v -> handleRegister());
        findViewById(R.id.loginTextView).setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void handleRegister() {
        String email = emailEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String firstname = firstnameEditText.getText().toString().trim();
        String lastname = lastnameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (email.isEmpty() || username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        authenticationAPI.registerRequest(new RegisterModel(firstname, lastname, true, email, username, password)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                authenticationAPI.loginRequest(new LoginModel(username, password)).enqueue(new Callback<TokensModel>() {
                    @Override
                    public void onResponse(Call<TokensModel> call, Response<TokensModel> response) {
                        tokenManager.saveTokens(response.body());
                        authenticationRepository.loadUser().thenApply(user -> {
                            runOnUiThread(() -> {
                                if (user != null) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            return user;
                        });
                    }

                    @Override
                    public void onFailure(Call<TokensModel> call, Throwable t) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Login after registration failed. Try again later.", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Registration failed. Try again later.", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}