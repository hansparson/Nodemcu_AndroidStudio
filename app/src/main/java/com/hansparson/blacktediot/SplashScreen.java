package com.hansparson.blacktediot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    ImageView logos;
    LottieAnimationView lottie;
    private static final int READ_PHONE_STATE_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logos = findViewById(R.id.logos);
        lottie = findViewById(R.id.lottie);

        logos.animate().alpha(1).setDuration(2700).setStartDelay(2000);
        lottie.animate().translationX(1000).setDuration(2000).setStartDelay(3000);

        ///TAMBAHAN


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission(Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE_CODE);
            }
        },5000);
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void checkPermission(String readPhoneState, int readPhoneStateCode) {
        if (ContextCompat.checkSelfPermission(SplashScreen.this, readPhoneState) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(SplashScreen.this, new String[] { readPhoneState }, readPhoneStateCode);
        }
        else {
            Toast.makeText(SplashScreen.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int readPhoneStateCode,
                                           @NonNull String[] readPhoneState,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(readPhoneStateCode,
                readPhoneState,
                grantResults);

        if (readPhoneStateCode == READ_PHONE_STATE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashScreen.this, "Selamat Datang", Toast.LENGTH_SHORT) .show();
                Intent i = new Intent(getApplicationContext(),GoogleLogin.class);
                startActivity(i);
            }
            else {
                Toast.makeText(SplashScreen.this, "Invalid Permission Denied", Toast.LENGTH_SHORT) .show();
                Intent i = new Intent(getApplicationContext(),GoogleLogin.class);
                startActivity(i);
            }
        }
    }

}