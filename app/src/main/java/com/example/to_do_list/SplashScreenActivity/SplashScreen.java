package com.example.to_do_list.SplashScreenActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.to_do_list.DashData.MainActivity;
import com.example.to_do_list.R;
import com.example.to_do_list.TD_List_Page.TD_List_Activity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    // Duration of the splash screen display in milliseconds
    private static final int SPLASH_DISPLAY_LENGTH = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Set the status bar color to match the app's color theme
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.appColor));

        // Initialize ImageView and load GIF using Glide
        ImageView gifImageView = findViewById(R.id.gifImageView);
        Glide.with(this)
                .asGif()
                .load(R.drawable.splash) // Ensure splash.gif is in res/drawable
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Optimize GIF caching
                .into(gifImageView);

        // Delay for splash screen display before transitioning to main activity
        new Handler().postDelayed(() -> {
            // Start the main activity and finish the splash screen activity
            startActivity(new Intent(SplashScreen.this, TD_List_Activity.class));
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
