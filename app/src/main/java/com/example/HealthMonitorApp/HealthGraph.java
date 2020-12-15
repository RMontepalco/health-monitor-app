package com.example.HealthMonitorApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthGraph extends Fragment {
    // Instance Variables
    private String loggedInUser;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private TextView hr7, bp7, act7, calin7, calout7;
    private TextView hr6, bp6, act6, calin6, calout6;
    private TextView hr5, bp5, act5, calin5, calout5;
    private TextView hr4, bp4, act4, calin4, calout4;
    private TextView hr3, bp3, act3, calin3, calout3;
    private TextView hr2, bp2, act2, calin2, calout2;
    private TextView hr1, bp1, act1, calin1, calout1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthGraph newInstance(String param1, String param2) {
        HealthGraph fragment = new HealthGraph();
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
        View v = inflater.inflate(R.layout.fragment_health_graph, container, false);

        // gets the currently logged in user's username
        loggedInUser = ((MenuScreen) getActivity()).getLoggedInUser();

        // connects to the database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("User");

        // Display graph data
        hr7 = v.findViewById(R.id.hr7);
        bp7 = v.findViewById(R.id.bp7);
        act7 = v.findViewById(R.id.act7);
        calin7 = v.findViewById(R.id.calin7);
        calout7 = v.findViewById(R.id.calout7);

        hr6 = v.findViewById(R.id.hr6);
        bp6 = v.findViewById(R.id.bp6);
        act6 = v.findViewById(R.id.act6);
        calin6 = v.findViewById(R.id.calin6);
        calout6 = v.findViewById(R.id.calout6);

        hr5 = v.findViewById(R.id.hr5);
        bp5 = v.findViewById(R.id.bp5);
        act5 = v.findViewById(R.id.act5);
        calin5 = v.findViewById(R.id.calin5);
        calout5 = v.findViewById(R.id.calout5);

        hr4 = v.findViewById(R.id.hr4);
        bp4 = v.findViewById(R.id.bp4);
        act4 = v.findViewById(R.id.act4);
        calin4 = v.findViewById(R.id.calin4);
        calout4 = v.findViewById(R.id.calout4);

        hr3 = v.findViewById(R.id.hr3);
        bp3 = v.findViewById(R.id.bp3);
        act3 = v.findViewById(R.id.act3);
        calin3 = v.findViewById(R.id.calin3);
        calout3 = v.findViewById(R.id.calout3);

        hr2 = v.findViewById(R.id.hr2);
        bp2 = v.findViewById(R.id.bp2);
        act2 = v.findViewById(R.id.act2);
        calin2 = v.findViewById(R.id.calin2);
        calout2 = v.findViewById(R.id.calout2);

        hr1 = v.findViewById(R.id.hr1);
        bp1 = v.findViewById(R.id.bp1);
        act1 = v.findViewById(R.id.act1);
        calin1 = v.findViewById(R.id.calin1);
        calout1 = v.findViewById(R.id.calout1);

        ref.child(loggedInUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);

                hr7.setText(Integer.toString(currentUser.getUserGraphData().getSeven().getHeartRate()));
                bp7.setText(currentUser.getUserGraphData().getSeven().getSystolicBP() + "/" + currentUser.getUserGraphData().getSeven().getDiastolicBP());
                act7.setText(Integer.toString(currentUser.getUserGraphData().getSeven().getActivity()));
                calin7.setText(Integer.toString(currentUser.getUserGraphData().getSeven().getCalorieIntake()));
                calout7.setText(Integer.toString(currentUser.getUserGraphData().getSeven().getCalorieOuttake()));

                hr6.setText(Integer.toString(currentUser.getUserGraphData().getSix().getHeartRate()));
                bp6.setText(currentUser.getUserGraphData().getSix().getSystolicBP() + "/" + currentUser.getUserGraphData().getSix().getDiastolicBP());
                act6.setText(Integer.toString(currentUser.getUserGraphData().getSix().getActivity()));
                calin6.setText(Integer.toString(currentUser.getUserGraphData().getSix().getCalorieIntake()));
                calout6.setText(Integer.toString(currentUser.getUserGraphData().getSix().getCalorieOuttake()));

                hr5.setText(Integer.toString(currentUser.getUserGraphData().getFive().getHeartRate()));
                bp5.setText(currentUser.getUserGraphData().getFive().getSystolicBP() + "/" + currentUser.getUserGraphData().getFive().getDiastolicBP());
                act5.setText(Integer.toString(currentUser.getUserGraphData().getFive().getActivity()));
                calin5.setText(Integer.toString(currentUser.getUserGraphData().getFive().getCalorieIntake()));
                calout5.setText(Integer.toString(currentUser.getUserGraphData().getFive().getCalorieOuttake()));

                hr4.setText(Integer.toString(currentUser.getUserGraphData().getFour().getHeartRate()));
                bp4.setText(currentUser.getUserGraphData().getFour().getSystolicBP() + "/" + currentUser.getUserGraphData().getFour().getDiastolicBP());
                act4.setText(Integer.toString(currentUser.getUserGraphData().getFour().getActivity()));
                calin4.setText(Integer.toString(currentUser.getUserGraphData().getFour().getCalorieIntake()));
                calout4.setText(Integer.toString(currentUser.getUserGraphData().getFour().getCalorieOuttake()));

                hr3.setText(Integer.toString(currentUser.getUserGraphData().getThree().getHeartRate()));
                bp3.setText(currentUser.getUserGraphData().getThree().getSystolicBP() + "/" + currentUser.getUserGraphData().getThree().getDiastolicBP());
                act3.setText(Integer.toString(currentUser.getUserGraphData().getThree().getActivity()));
                calin3.setText(Integer.toString(currentUser.getUserGraphData().getThree().getCalorieIntake()));
                calout3.setText(Integer.toString(currentUser.getUserGraphData().getThree().getCalorieOuttake()));

                hr2.setText(Integer.toString(currentUser.getUserGraphData().getTwo().getHeartRate()));
                bp2.setText(currentUser.getUserGraphData().getTwo().getSystolicBP() + "/" + currentUser.getUserGraphData().getTwo().getDiastolicBP());
                act2.setText(Integer.toString(currentUser.getUserGraphData().getTwo().getActivity()));
                calin2.setText(Integer.toString(currentUser.getUserGraphData().getTwo().getCalorieIntake()));
                calout2.setText(Integer.toString(currentUser.getUserGraphData().getTwo().getCalorieOuttake()));

                hr1.setText(Integer.toString(currentUser.getUserGraphData().getOne().getHeartRate()));
                bp1.setText(currentUser.getUserGraphData().getOne().getSystolicBP() + "/" + currentUser.getUserGraphData().getOne().getDiastolicBP());
                act1.setText(Integer.toString(currentUser.getUserGraphData().getOne().getActivity()));
                calin1.setText(Integer.toString(currentUser.getUserGraphData().getOne().getCalorieIntake()));
                calout1.setText(Integer.toString(currentUser.getUserGraphData().getOne().getCalorieOuttake()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return v;
    }
}