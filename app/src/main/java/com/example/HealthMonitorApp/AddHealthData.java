package com.example.HealthMonitorApp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHealthData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHealthData extends Fragment {
    // Instance Variables
    private String loggedInUser;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private Button submitGraphData;
    private ImageButton submitCalIn,updateW, updateH, updateA, updateG;
    EditText HR, SBP, DBP, Act, CalIn, CalOut, updateCalIn, uWeight, uHeight, uAge, uGender;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddHealthData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHealthData.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHealthData newInstance(String param1, String param2) {
        AddHealthData fragment = new AddHealthData();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_health_data, container, false);

        // gets the currently logged in user's username
        loggedInUser = ((MenuScreen) getActivity()).getLoggedInUser();

        // connects to the database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("User");

        // Stores user inputted health data
        HR = (EditText) v.findViewById(R.id.addHeartRate);
        SBP = (EditText) v.findViewById(R.id.addSystolicBP);
        DBP = (EditText) v.findViewById(R.id.addDiastolicBP);
        Act = (EditText) v.findViewById(R.id.addActivity);
        CalIn = (EditText) v.findViewById(R.id.addCalorieIntake);
        CalOut = (EditText) v.findViewById(R.id.addCalorieOuttake);
        updateCalIn = (EditText) v.findViewById(R.id.updateCalorieIntake);
        uWeight = (EditText) v.findViewById(R.id.updateWeight);
        uHeight = (EditText) v.findViewById(R.id.updateHeight);
        uAge = (EditText) v.findViewById(R.id.updateAge);
        uGender = (EditText) v.findViewById(R.id.updateGender);

        // Creates submit health data button
        submitGraphData = v.findViewById(R.id.submitBtnGraphData);
        submitGraphData.setOnClickListener(view -> submitGraphData());

        // Creates submit Calorie intake button
        submitCalIn = v.findViewById(R.id.updateBtnCalorieIntake);
        submitCalIn.setOnClickListener(viewCal -> updateCalorieIntake());

        // Creates update weight, height, age, and gender buttons
        updateW = v.findViewById(R.id.btnUpdateWeight);
        updateW.setOnClickListener(viewW -> updateWeight());

        updateH = v.findViewById(R.id.btnUpdateHeight);
        updateH.setOnClickListener(viewH -> updateHeight());

        updateA = v.findViewById(R.id.btnUpdateAge);
        updateA.setOnClickListener(viewA -> updateAge());

        updateG = v.findViewById(R.id.btnUpdateGender);
        updateG.setOnClickListener(viewG -> updateGender());

        return v;
    }

    // Submit's user's entered health data and stores it in their graph list.
    public void submitGraphData() {
        GraphData gd = new GraphData();

        try {
            gd.setHeartRate(Integer.parseInt(HR.getText().toString()));
            gd.setSystolicBP(Integer.parseInt(SBP.getText().toString()));
            gd.setDiastolicBP(Integer.parseInt(DBP.getText().toString()));
            gd.setActivity(Integer.parseInt(Act.getText().toString()));
            gd.setCalorieIntake(Integer.parseInt(CalIn.getText().toString()));
            gd.setCalorieOuttake(Integer.parseInt(CalOut.getText().toString()));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gd.getHeartRate() > 0 && gd.getSystolicBP() > 0 && gd.getDiastolicBP() > 0 && gd.getActivity() > 0 && gd.getCalorieIntake() > 0 && gd.getCalorieOuttake() > 0) {
            ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User currentUser = snapshot.getValue(User.class);
                    currentUser.getUserGraphData().shiftData(gd);
                    ref.child(loggedInUser).child("userGraphData").setValue(currentUser.getUserGraphData());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            // After successful data submission, close the user's keyboard
            InputMethodManager imm = (InputMethodManager) ((MenuScreen)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != ((MenuScreen)getActivity()).getCurrentFocus()) {
                imm.hideSoftInputFromWindow(((MenuScreen) getActivity()).getCurrentFocus()
                        .getApplicationWindowToken(), 0);
            }

            // Clear text boxes
            HR.getText().clear();
            SBP.getText().clear();
            DBP.getText().clear();
            Act.getText().clear();
            CalIn.getText().clear();
            CalOut.getText().clear();

            Toast.makeText(getActivity(), "Health Data Saved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }

    }

    // Updates today's calorie intake by adding the inputted calories intake.
    public void updateCalorieIntake() {
        try {
            int c = Integer.parseInt(updateCalIn.getText().toString());
            if (c > 0) {
                ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        ref.child(loggedInUser).child("userGraphData").child("seven").child("calorieIntake").setValue(currentUser.getUserGraphData().getSeven().addCalorieIntake(c));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                // After successful data submission, close the user's keyboard
                InputMethodManager imm = (InputMethodManager) ((MenuScreen)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != ((MenuScreen)getActivity()).getCurrentFocus()) {
                    imm.hideSoftInputFromWindow(((MenuScreen) getActivity()).getCurrentFocus()
                            .getApplicationWindowToken(), 0);
                }

                // Clear text box
                updateCalIn.getText().clear();

                Toast.makeText(getActivity(), "Calorie Count Updated.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateWeight() {
        try {
            int w = Integer.parseInt(uWeight.getText().toString());
            if (w > 0) {
                ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        ref.child(loggedInUser).child("weight").setValue(w);
                        ref.child(loggedInUser).child("weightMetric").setValue((int)(w / 2.205));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                // After successful data submission, close the user's keyboard
                InputMethodManager imm = (InputMethodManager) ((MenuScreen)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != ((MenuScreen)getActivity()).getCurrentFocus()) {
                    imm.hideSoftInputFromWindow(((MenuScreen) getActivity()).getCurrentFocus()
                            .getApplicationWindowToken(), 0);
                }

                // Clear text box
                uWeight.getText().clear();

                Toast.makeText(getActivity(), "Weight Updated.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateHeight() {
        try {
            int h = Integer.parseInt(uHeight.getText().toString());
            if (h > 0) {
                ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        ref.child(loggedInUser).child("height").setValue(h);
                        ref.child(loggedInUser).child("heightMetric").setValue((int)(h * 2.54));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                // After successful data submission, close the user's keyboard
                InputMethodManager imm = (InputMethodManager) ((MenuScreen)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != ((MenuScreen)getActivity()).getCurrentFocus()) {
                    imm.hideSoftInputFromWindow(((MenuScreen) getActivity()).getCurrentFocus()
                            .getApplicationWindowToken(), 0);
                }

                // Clear text box
                uHeight.getText().clear();

                Toast.makeText(getActivity(), "Height Updated.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateAge() {
        try {
            int a = Integer.parseInt(uAge.getText().toString());
            if (a > 0) {
                ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        ref.child(loggedInUser).child("age").setValue(a);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                // After successful data submission, close the user's keyboard
                InputMethodManager imm = (InputMethodManager) ((MenuScreen)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != ((MenuScreen)getActivity()).getCurrentFocus()) {
                    imm.hideSoftInputFromWindow(((MenuScreen) getActivity()).getCurrentFocus()
                            .getApplicationWindowToken(), 0);
                }

                // Clear text box
                uAge.getText().clear();

                Toast.makeText(getActivity(), "Age Updated.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateGender() {
        try {
            String g = uGender.getText().toString();
            if (!g.equals("")) {
                ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        ref.child(loggedInUser).child("gender").setValue(g);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                // After successful data submission, close the user's keyboard
                InputMethodManager imm = (InputMethodManager) ((MenuScreen)getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != ((MenuScreen)getActivity()).getCurrentFocus()) {
                    imm.hideSoftInputFromWindow(((MenuScreen) getActivity()).getCurrentFocus()
                            .getApplicationWindowToken(), 0);
                }

                // Clear text box
                uGender.getText().clear();

                Toast.makeText(getActivity(), "Gender Updated.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

}