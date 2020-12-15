package com.example.HealthMonitorApp;

public class GraphData {
    private int heartRate;
    private int systolicBP;
    private int diastolicBP;
    private int activity;
    private int calorieIntake;
    private int calorieOuttake;

    /*
    public GraphData(int heartRate, int systolicBP, int diastolicBP, int activity, int calorieIntake, int calorieOuttake) {
        this.heartRate = heartRate;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
        this.activity = activity;
        this.calorieIntake = calorieIntake;
        this.calorieOuttake = calorieOuttake;
    }
    */

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getSystolicBP() {
        return systolicBP;
    }

    public void setSystolicBP(int systolicBP) {
        this.systolicBP = systolicBP;
    }

    public int getDiastolicBP() {
        return diastolicBP;
    }

    public void setDiastolicBP(int diastolicBP) {
        this.diastolicBP = diastolicBP;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getCalorieIntake() {
        return calorieIntake;
    }

    public void setCalorieIntake(int calorieIntake) {
        this.calorieIntake = calorieIntake;
    }

    public int getCalorieOuttake() {
        return calorieOuttake;
    }

    public void setCalorieOuttake(int calorieOuttake) {
        this.calorieOuttake = calorieOuttake;
    }

    public int addCalorieIntake(int c) {
        return calorieIntake += c;
    }
}
