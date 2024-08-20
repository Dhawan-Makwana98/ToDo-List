package com.example.to_do_list.DashData;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.to_do_list.R;
import com.example.to_do_list.TD_List_Page.TD_List_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edtemail, edtpassword;
    private FirebaseAuth auth;
    private final Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtemail = findViewById(R.id.edt_emailId);
        edtpassword = findViewById(R.id.edt_password);
        Button btn_login = findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(v -> {
            String txtemail = edtemail.getText().toString().trim();
            String txtpassword = edtpassword.getText().toString().trim();

            // Validate email and password
            if (TextUtils.isEmpty(txtemail)) {
                edtemail.setError("Email is required");
                edtemail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(txtemail).matches()) {
                edtemail.setError("Enter a valid email address");
                edtemail.requestFocus();
            } else if (TextUtils.isEmpty(txtpassword)) {
                edtpassword.setError("Password is required");
                edtpassword.requestFocus();
            } else {
                // Proceed with login
                loginUser(txtemail, txtpassword);
            }
        });

        edtpassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtpassword.getRight() - edtpassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility() {
        int currentInputType = edtpassword.getInputType();
        if (currentInputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            edtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edtpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0); // Eye icon drawable
            edtpassword.setSelection(edtpassword.getText().length()); // Move cursor to the end

            // Reset visibility to password after 2 seconds
            handler.postDelayed(() -> {
                edtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edtpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0); // Eye off icon drawable
                edtpassword.setSelection(edtpassword.getText().length()); // Move cursor to the end
            }, 2000); // 2 seconds delay
        } else {
            edtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0); // Eye off icon drawable
            edtpassword.setSelection(edtpassword.getText().length()); // Move cursor to the end
        }
    }

    private void loginUser(String txtemail, String txtpassword) {
        auth.signInWithEmailAndPassword(txtemail, txtpassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    String username = firebaseUser.getDisplayName();

                    // Log userId and username
                    Log.d("dota", "User ID: " + userId);
                    Log.d("dota", "Username: " + username);

                    // Show a toast or perform other actions with the retrieved data
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, TD_List_Activity.class));
                    finish();
                }
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    Toast.makeText(LoginActivity.this, "Login failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
