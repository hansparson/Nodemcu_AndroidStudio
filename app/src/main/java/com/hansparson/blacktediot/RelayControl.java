package com.hansparson.blacktediot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RelayControl extends AppCompatActivity {

    DatabaseReference Referensi;
    private LabeledSwitch Relay1;
    private LabeledSwitch Relay2;

    boolean doubleBackToExitPressedOnce = false;
    @Override
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relay_control);

        Relay1 = findViewById(R.id.relay_1);
        Relay2 = findViewById(R.id.relay_2);

        getdata();

        Relay1.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                Toast.makeText(RelayControl.this, "relay 1 " +isOn, Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Switch").child("Relay_1").setValue(isOn);
            }
        });

        Relay2.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
//                Toast.makeText(RelayControl.this, "relay 2 " +isOn, Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Switch").child("Relay_2").setValue(isOn);
            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                        findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_relay);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_sensor:
                                startActivity(new Intent(RelayControl.this, MainActivity.class));
                                break;
                            case R.id.navigation_relay:

                                break;
                            case R.id.navigation_account:
                                startActivity(new Intent(RelayControl.this, LogOut.class));

                                break;
                        }
                        return false;
                    }
                });

    }

    private void getdata() {
        Referensi = FirebaseDatabase.getInstance().getReference().child("Switch");
        Referensi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean relay1 = (Boolean) snapshot.child("Relay_1").getValue();
                boolean relay2 = (Boolean) snapshot.child("Relay_2").getValue();

                Relay1.setOn(relay1);
                Relay2.setOn(relay2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}