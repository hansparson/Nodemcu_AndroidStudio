package com.hansparson.blacktediot;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "6f29de1f-7fda-4f40-b1bf-7b6ec41d46fc";

    DatabaseReference Referensi;

    FirebaseAuth firebaseAuth;

    private TextView temperature;
    private TextView kelembapan;
    private TextView intensitas_cahaya;
    private TextView curah_hujan;


    boolean doubleBackToExitPressedOnce = false;
    @Override //////// Tombol keluar Back 2 Kali
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "tekan mundur sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override /// Registrasi Firebase Cloud Message (Cuma cari Token ID)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast
                       System.out.println(token);
//                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        temperature = findViewById(R.id.temperature);
        kelembapan = findViewById(R.id.kelembapan);
        curah_hujan = findViewById(R.id.curah_hujan);
        intensitas_cahaya = findViewById(R.id.intensitas_cahaya);

        getdata();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_sensor);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_sensor:
                                break;
                            case R.id.navigation_relay:
                                startActivity(new Intent(MainActivity.this, RelayControl.class));
                                break;
                            case R.id.navigation_account:
                                startActivity(new Intent(MainActivity.this, LogOut.class));
                                break;
                        }
                        return false;
                    }
                });

    }

    private void getdata() {
        Referensi = FirebaseDatabase.getInstance().getReference().child("Sensors");
        Referensi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Nilai_Temperature = snapshot.child("Temperature").getValue().toString();
                String Nilai_Kelembapan = snapshot.child("Kelembapan").getValue().toString();
                String Nilai_Cahaya = snapshot.child("Intensitas_Cahaya").getValue().toString();
                String Nilai_Hujan = snapshot.child("Curah_Hujan").getValue().toString();

                temperature.setText(Nilai_Temperature);
                kelembapan.setText(Nilai_Kelembapan);
                intensitas_cahaya.setText(Nilai_Cahaya);
                curah_hujan.setText(Nilai_Hujan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}