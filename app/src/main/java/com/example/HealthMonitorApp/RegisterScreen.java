package com.example.HealthMonitorApp;

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

public class RegisterScreen extends AppCompatActivity {
    private Button submitBtn;
    private User user;
    EditText username, password, confirmPassword, gender, age, weight, height;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // Takes in text boxes and stores them
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        gender = (EditText) findViewById(R.id.gender);
        age = (EditText) findViewById(R.id.age);
        weight = (EditText) findViewById(R.id.weight);
        height = (EditText) findViewById(R.id.height);

        // initiates the database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("User");

        // Creates the submitBtn
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(v -> registerUser());
    }

    // Creates a new Object User containing these parameters and stores it in the database.
    // All fields must be filled in
    // User must enter password twice to verify password correctness
    // Weight and height must be greater than 0
    public void registerUser() {
        user = new User();
        try {
            // Sets the user's credentials
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.setGender(gender.getText().toString());
            user.setAge(Integer.parseInt(age.getText().toString()));
            user.setWeight(Integer.parseInt(weight.getText().toString()));
            user.setHeight(Integer.parseInt(height.getText().toString()));
            user.setWeightMetric((int)(Integer.parseInt(weight.getText().toString()) / 2.205));
            user.setHeightMetric((int)(Integer.parseInt(height.getText().toString()) * 2.54));
            user.setUserGraphData(new GraphList());

            // Password verification
            String cf = confirmPassword.getText().toString();

            // username or gender are empty
            if (user.getUsername().equals("") || user.getGender().equals("")) {
                Toast.makeText(RegisterScreen.this,
                        "All fields must be completed.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Checks if inputted username already exists
            // If username does not exist, continue with account creation.
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userNameRef = rootRef.child("User").child(user.getUsername());
            userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Username does not exist, continue checking credentials
                    if(!dataSnapshot.exists()) {
                        // password or confirmPassword are empty or passwords do not match
                        if (user.getPassword().equals("") || cf.equals("") || !user.getPassword().equals(cf)) {
                            Toast.makeText(RegisterScreen.this,
                                    "Passwords do not match.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // age, weight, or height are less than 1
                        if (user.getAge() < 1 || user.getWeight() < 1 || user.getHeight() < 1) {
                            Toast.makeText(RegisterScreen.this,
                                    "Invalid age, weight, or height.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // stores User in database and returns the user to the login screen.
                        ref.child(user.getUsername()).setValue(user);
                        Toast.makeText(RegisterScreen.this,
                                "Account Registration Complete.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegisterScreen.this, LoginScreen.class);
                        startActivity(i);
                    }
                    // Username already exists, cancel creation.
                    else {
                        Toast.makeText(RegisterScreen.this,
                                "Username already exists.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

        }
        // At lease one or more of the user credentials are empty
        catch (Exception e) {
            Toast.makeText(RegisterScreen.this,
                    "All fields must be completed.", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}