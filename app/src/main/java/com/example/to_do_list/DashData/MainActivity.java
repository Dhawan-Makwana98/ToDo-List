package com.example.to_do_list.DashData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.to_do_list.R;
import com.example.to_do_list.TD_List_Page.TD_List_Activity;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_signup;
    TextView txt_guestMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the status bar color to the color specified for Application
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.appColor));

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        txt_guestMode = findViewById(R.id.txt_guestMode);

        btn_login.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        });

        btn_signup.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SignUpPage.class);
            startActivity(i);
        });

        // Guest mode text click listener (if applicable)
        txt_guestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(new Intent(MainActivity.this , TD_List_Activity.class));
                i.putExtra("guest","1");
                startActivity(i);
                // Handle guest mode action here
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // Exit the activity
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); // Dismiss the dialog
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
