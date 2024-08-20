package com.example.to_do_list.DashData;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.to_do_list.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpPage extends AppCompatActivity {
    EditText edtemail, edtpassword, edtusername, edtConfirmPassword;
    Button btn_register;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private final Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        edtemail = findViewById(R.id.edt_emailId);
        edtpassword = findViewById(R.id.edt_Password);
        edtConfirmPassword = findViewById(R.id.edt_Confirmpswd);
        edtusername = findViewById(R.id.edt_UserName);
        btn_register = findViewById(R.id.btn_signup);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btn_register.setOnClickListener(v -> {
            String txtemail = edtemail.getText().toString().trim();
            String txtpassword = edtpassword.getText().toString().trim();
            String txtConfirmPassword = edtConfirmPassword.getText().toString().trim();
            String txtusername = edtusername.getText().toString().trim();

            if (TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword) || TextUtils.isEmpty(txtusername)) {
                Toast.makeText(SignUpPage.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
            } else if (txtpassword.length() < 6) {
                Toast.makeText(SignUpPage.this, "Password Invalid", Toast.LENGTH_SHORT).show();
            } else if (!txtpassword.equals(txtConfirmPassword)) {
                Toast.makeText(SignUpPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txtusername, txtemail, txtpassword);
            }
        });

        edtpassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtpassword.getRight() - edtpassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(edtpassword);
                    return true;
                }
            }
            return false;
        });

        edtConfirmPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtConfirmPassword.getRight() - edtConfirmPassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(edtConfirmPassword);
                    return true;
                }
            }
            return false;
        });
    }

    private void registerUser(String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpPage.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();

                    // Update the user profile with the username
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();

                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            // Save the user data to the database under the user's UID
                            User user = new User(username, email);
                            databaseReference.child(userId).setValue(user).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(SignUpPage.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Log.d("dota", "username--" + username);
                                    Log.d("dota", "userId--" + userId);
                                    startActivity(new Intent(SignUpPage.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignUpPage.this, "Failed to register user in database: " + Objects.requireNonNull(task2.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SignUpPage.this, "Failed to update user profile: " + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                // Capture the error message from the registration failure
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Toast.makeText(SignUpPage.this, "User Registration Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0); // Set the eye icon
            // Delay resetting the visibility
            handler.postDelayed(() -> {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0); // Reset the eye off icon
                editText.setSelection(editText.getText().length()); // Move cursor to the end
            }, 2000); // 2 seconds delay
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0); // Set the eye off icon
        }
    }
}
