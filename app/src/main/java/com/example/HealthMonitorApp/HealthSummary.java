package com.example.HealthMonitorApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthSummary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthSummary extends Fragment {
    // Instance Variables
    private TextView genderTV, ageTV, weightTV, heightTV, heartRateTV, bloodPressureTV, activityTV, calorieIntakeTV, calorieOuttakeTV;
    private ImageView trendHR, trendBP, trendAct, trendCalIn, trendCalOut;
    private String loggedInUser;
    Switch convertSw;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthSummary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthSummary.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthSummary newInstance(String param1, String param2) {
        HealthSummary fragment = new HealthSummary();
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
        View v = inflater.inflate(R.layout.fragment_health_summary, container, false);

        // gets the currently logged in user's username
        loggedInUser = ((MenuScreen) getActivity()).getLoggedInUser();

        // connects to the database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("User");

        // Displays the user's health Stats
        genderTV = v.findViewById(R.id.statGender);
        ageTV = v.findViewById(R.id.statAge);
        weightTV = v.findViewById(R.id.statWeight);
        heightTV = v.findViewById(R.id.statHeight);
        heartRateTV = v.findViewById(R.id.statHeartRate);
        bloodPressureTV = v.findViewById(R.id.statBloodPressure);
        activityTV = v.findViewById(R.id.statActivity);
        calorieIntakeTV = v.findViewById(R.id.statCalorieIntake);
        calorieOuttakeTV = v.findViewById(R.id.statCalorieOuttake);

        // Displays arrow indication of user's weekly graph data trend
        trendHR = v.findViewById(R.id.trendHR);
        trendBP = v.findViewById(R.id.trendBP);
        trendAct = v.findViewById(R.id.trendAct);
        trendCalIn = v.findViewById(R.id.trendCalIn);
        trendCalOut = v.findViewById(R.id.trendCalOut);

        // Creates the convert switch
        convertSw = v.findViewById(R.id.swConvertUnits);

        // Converts units from imperial to metric, and vise versa
        convertSw.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b == true){
                ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        weightTV.setText("Weight: " + Integer.toString(currentUser.getWeightMetric()) + " kg");
                        heightTV.setText("Height: " + Integer.toString(currentUser.getHeightMetric()) + " cm");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                ref.child(loggedInUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User currentUser = snapshot.getValue(User.class);
                        weightTV.setText("Weight: " + Integer.toString(currentUser.getWeight()) + " lbs");
                        heightTV.setText("Height: " + Integer.toString(currentUser.getHeight()) + " in");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        ref.child(loggedInUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);

                // display health stats
                genderTV.setText("Gender: " + currentUser.getGender());
                ageTV.setText("Age: " + Integer.toString(currentUser.getAge()));
                weightTV.setText("Weight: " + Integer.toString(currentUser.getWeight()) + " lbs");
                heightTV.setText("Height: " + Integer.toString(currentUser.getHeight()) + " in");

                // Display weekly trend
                // Heart Rate
                double thr = (((double)currentUser.getUserGraphData().getTwo().getHeartRate()/(double)currentUser.getUserGraphData().getOne().getHeartRate())
                        + ((double)currentUser.getUserGraphData().getThree().getHeartRate()/(double)currentUser.getUserGraphData().getTwo().getHeartRate())
                        + ((double)currentUser.getUserGraphData().getFour().getHeartRate()/(double)currentUser.getUserGraphData().getThree().getHeartRate())
                        + ((double)currentUser.getUserGraphData().getFive().getHeartRate()/(double)currentUser.getUserGraphData().getFour().getHeartRate())
                        + (double)(currentUser.getUserGraphData().getSix().getHeartRate()/(double)currentUser.getUserGraphData().getFive().getHeartRate())
                        + (double)(currentUser.getUserGraphData().getSeven().getHeartRate()/(double)currentUser.getUserGraphData().getSix().getHeartRate())) / 6;
                if (thr > 1) {
                    trendHR.setImageResource(R.drawable.ic_trending_up);
                } else if (thr < 1) {
                    trendHR.setImageResource(R.drawable.ic_trending_down);
                } else {
                    trendHR.setImageResource(R.drawable.ic_trending_flat);
                }

                // Blood Pressure
                double tsbp = (((double)currentUser.getUserGraphData().getTwo().getSystolicBP()/(double)currentUser.getUserGraphData().getOne().getSystolicBP())
                        + ((double)currentUser.getUserGraphData().getThree().getSystolicBP()/(double)currentUser.getUserGraphData().getTwo().getSystolicBP())
                        + ((double)currentUser.getUserGraphData().getFour().getSystolicBP()/(double)currentUser.getUserGraphData().getThree().getSystolicBP())
                        + ((double)currentUser.getUserGraphData().getFive().getSystolicBP()/(double)currentUser.getUserGraphData().getFour().getSystolicBP())
                        + (double)(currentUser.getUserGraphData().getSix().getSystolicBP()/(double)currentUser.getUserGraphData().getFive().getSystolicBP())
                        + (double)(currentUser.getUserGraphData().getSeven().getSystolicBP()/(double)currentUser.getUserGraphData().getSix().getSystolicBP())) / 6;
                double tdbp = (((double)currentUser.getUserGraphData().getTwo().getDiastolicBP()/(double)currentUser.getUserGraphData().getOne().getDiastolicBP())
                        + ((double)currentUser.getUserGraphData().getThree().getDiastolicBP()/(double)currentUser.getUserGraphData().getTwo().getDiastolicBP())
                        + ((double)currentUser.getUserGraphData().getFour().getDiastolicBP()/(double)currentUser.getUserGraphData().getThree().getDiastolicBP())
                        + ((double)currentUser.getUserGraphData().getFive().getDiastolicBP()/(double)currentUser.getUserGraphData().getFour().getDiastolicBP())
                        + (double)(currentUser.getUserGraphData().getSix().getDiastolicBP()/(double)currentUser.getUserGraphData().getFive().getDiastolicBP())
                        + (double)(currentUser.getUserGraphData().getSeven().getDiastolicBP()/(double)currentUser.getUserGraphData().getSix().getDiastolicBP())) / 6;
                if (tsbp > 1 || tdbp > 1) {
                    trendBP.setImageResource(R.drawable.ic_trending_up);
                } else if (tsbp < 1 || tdbp < 1) {
                    trendBP.setImageResource(R.drawable.ic_trending_down);
                } else {
                    trendBP.setImageResource(R.drawable.ic_trending_flat);
                }

                // Activity
                double tact = (((double)currentUser.getUserGraphData().getTwo().getActivity()/(double)currentUser.getUserGraphData().getOne().getActivity())
                        + ((double)currentUser.getUserGraphData().getThree().getActivity()/(double)currentUser.getUserGraphData().getTwo().getActivity())
                        + ((double)currentUser.getUserGraphData().getFour().getActivity()/(double)currentUser.getUserGraphData().getThree().getActivity())
                        + ((double)currentUser.getUserGraphData().getFive().getActivity()/(double)currentUser.getUserGraphData().getFour().getActivity())
                        + (double)(currentUser.getUserGraphData().getSix().getActivity()/(double)currentUser.getUserGraphData().getFive().getActivity())
                        + (double)(currentUser.getUserGraphData().getSeven().getActivity()/(double)currentUser.getUserGraphData().getSix().getActivity())) / 6;
                if (tact > 1) {
                    trendAct.setImageResource(R.drawable.ic_trending_up);
                } else if (tact < 1) {
                    trendAct.setImageResource(R.drawable.ic_trending_down);
                } else {
                    trendAct.setImageResource(R.drawable.ic_trending_flat);
                }

                // Calorie Intake
                double tcalin = (((double)currentUser.getUserGraphData().getTwo().getCalorieIntake()/(double)currentUser.getUserGraphData().getOne().getCalorieIntake())
                        + ((double)currentUser.getUserGraphData().getThree().getCalorieIntake()/(double)currentUser.getUserGraphData().getTwo().getCalorieIntake())
                        + ((double)currentUser.getUserGraphData().getFour().getCalorieIntake()/(double)currentUser.getUserGraphData().getThree().getCalorieIntake())
                        + ((double)currentUser.getUserGraphData().getFive().getCalorieIntake()/(double)currentUser.getUserGraphData().getFour().getCalorieIntake())
                        + (double)(currentUser.getUserGraphData().getSix().getCalorieIntake()/(double)currentUser.getUserGraphData().getFive().getCalorieIntake())
                        + (double)(currentUser.getUserGraphData().getSeven().getCalorieIntake()/(double)currentUser.getUserGraphData().getSix().getCalorieIntake())) / 6;
                if (tcalin > 1) {
                    trendCalIn.setImageResource(R.drawable.ic_trending_up);
                } else if (tcalin < 1) {
                    trendCalIn.setImageResource(R.drawable.ic_trending_down);
                } else {
                    trendCalIn.setImageResource(R.drawable.ic_trending_flat);
                }

                // Calorie Outtake
                double tcalout = (((double)currentUser.getUserGraphData().getTwo().getCalorieOuttake()/(double)currentUser.getUserGraphData().getOne().getCalorieOuttake())
                        + ((double)currentUser.getUserGraphData().getThree().getCalorieOuttake()/(double)currentUser.getUserGraphData().getTwo().getCalorieOuttake())
                        + ((double)currentUser.getUserGraphData().getFour().getCalorieOuttake()/(double)currentUser.getUserGraphData().getThree().getCalorieOuttake())
                        + ((double)currentUser.getUserGraphData().getFive().getCalorieOuttake()/(double)currentUser.getUserGraphData().getFour().getCalorieOuttake())
                        + (double)(currentUser.getUserGraphData().getSix().getCalorieOuttake()/(double)currentUser.getUserGraphData().getFive().getCalorieOuttake())
                        + (double)(currentUser.getUserGraphData().getSeven().getCalorieOuttake()/(double)currentUser.getUserGraphData().getSix().getCalorieOuttake())) / 6;
                if (tcalout > 1) {
                    trendCalOut.setImageResource(R.drawable.ic_trending_up);
                } else if (tcalout < 1) {
                    trendCalOut.setImageResource(R.drawable.ic_trending_down);
                } else {
                    trendCalOut.setImageResource(R.drawable.ic_trending_flat);
                }

                // display today's health data from graph
                heartRateTV.setText("Heart Rate: " + currentUser.getUserGraphData().getSeven().getHeartRate() + " BPM");
                bloodPressureTV.setText("Blood Pressure: " + currentUser.getUserGraphData().getSeven().getSystolicBP() + "/" + currentUser.getUserGraphData().getSeven().getDiastolicBP() + " mmHg");
                activityTV.setText("Activity: " + currentUser.getUserGraphData().getSeven().getActivity() + " Hours");
                calorieIntakeTV.setText("Calorie Intake: " + currentUser.getUserGraphData().getSeven().getCalorieIntake() + " Cal");
                calorieOuttakeTV.setText("Calorie Outtake: " + currentUser.getUserGraphData().getSeven().getCalorieOuttake() + " Cal");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return v;
    }
}