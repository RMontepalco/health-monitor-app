package com.example.HealthMonitorApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity {
    private Button loginBtn, registerBtn;
    EditText username, password;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Takes in username and password credentials from text boxes
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Initiates the database
        ref = FirebaseDatabase.getInstance().getReference().child("User");

        // Creates the loginBtn
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(v -> loginUser());

        // Creates the registerBtn
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(v -> openRegisterScreen());
    }

    @Override
    public void onBackPressed() { }

    // Searches the database for the username and password
    // If neither the username and password match, or if the username does not exist, do not log in.
    // If the username exists and the password matches, login the user and open the MenuScreen.
    public void loginUser(){
        String strUser = username.getText().toString();
        String pass = password.getText().toString();

        // username and password are not empty
        if (!strUser.equals("") && !pass.equals("")) {
            ref.child(strUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        // Searches database for User containing username
                        User user = snapshot.getValue(User.class);

                        // password matches username
                        if(pass.equals(user.getPassword())) {
                            Toast.makeText(LoginScreen.this,
                                    "Login Successful.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginScreen.this, MenuScreen.class);
                            i.putExtra("loggedInUser",user.getUsername());
                            startActivity(i);
                        } else {
                            Toast.makeText(LoginScreen.this,
                                    "Invalid username/password.", Toast.LENGTH_SHORT).show();
                    }
                    } catch(Exception e) {
                        // User username does not exist in the database
                        Toast.makeText(LoginScreen.this,
                                "Invalid username/password.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(LoginScreen.this,
                    "Invalid username/password.", Toast.LENGTH_SHORT).show();
        }
    }

    // Opens the RegisterScreen
    private void openRegisterScreen() {
        Intent i = new Intent(this, RegisterScreen.class);
        startActivity(i);
    }

}