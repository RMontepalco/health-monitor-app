package com.example.HealthMonitorApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuScreen extends AppCompatActivity {
    // Instance Variables
    private TextView usernameTV;
    private String loggedInUser;
    private Button logoutBtn;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        // gets the logged in user's username from the login screen
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            loggedInUser = extras.getString("loggedInUser");
        }

        // connects to the database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("User");

        // Creates the logoutBtn
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> logoutUser());

        // When the imageNavMenu image is pressed, the navigation menu will open.
        final DrawerLayout dl = findViewById(R.id.drawerLayout);
        findViewById(R.id.imageNavMenu).setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != this.getCurrentFocus()) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus()
                        .getApplicationWindowToken(), 0);
            }
            dl.openDrawer(GravityCompat.START);
        });

        // Allows the user to switch between different fragments in the MenuScreen.
        NavigationView nv = findViewById(R.id.navigationView);
        NavController nc = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(nv, nc);

        // Changes the name of the title text on the app bar with its corresponding fragment
        TextView textTitle = findViewById(R.id.textTitle);
        nc.addOnDestinationChangedListener((controller, destination, arguments) -> textTitle.setText(destination.getLabel()));

        // Displays the logged in user's username
        usernameTV = findViewById(R.id.navUsername);
        ref.child(loggedInUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                usernameTV.setText(currentUser.getUsername());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() { }

    // Shares the current user's username across all fragments to add and view their data
    public String getLoggedInUser() {
        return loggedInUser;
    }

    // Prompts the user if they are sure they want to log out.
    // The current user will log out and return to the LoginScreen.
    public void logoutUser() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Are you sure you want to logout?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialogInterface, j) -> {
                    Intent i = new Intent(this, LoginScreen.class);
                    startActivity(i);
                })
                .setNegativeButton("No", (dialogInterface, j) -> dialogInterface.cancel());
        AlertDialog ad = adb.create();
        ad.show();
    }

}