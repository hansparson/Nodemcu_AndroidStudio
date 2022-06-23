package com.hansparson.blacktediot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogOut extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    GoogleSignInClient googleSignInClient;

    private ImageView foto_profil;
    private Button Log_Out;
    private TextView nama_user, email_user;

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
        setContentView(R.layout.activity_log_out);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Log_Out = findViewById(R.id.button_logout);
        foto_profil = findViewById(R.id.foto_profil);
        nama_user = findViewById(R.id.nama_user);
        email_user = findViewById(R.id.emailindo);

        googleSignInClient= GoogleSignIn.getClient(LogOut.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);

        if(firebaseUser!=null)
        {
            // When firebase user is not equal to null
            // Set image on image view
            Glide.with(LogOut.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(foto_profil);
            // set name on text view
            nama_user.setText(firebaseUser.getDisplayName());
            email_user.setText(firebaseUser.getEmail());
        }

        Log_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            firebaseAuth.signOut();
                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),GoogleLogin.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_sensor:
                                startActivity(new Intent(LogOut.this, MainActivity.class));
                                break;
                            case R.id.navigation_relay:
                                startActivity(new Intent(LogOut.this, RelayControl.class));
                                break;
                            case R.id.navigation_account:
                                break;
                        }
                        return false;
                    }
                });
    }
}