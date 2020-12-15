package com.example.HealthMonitorApp;

public class User {
    // User account credentials
    private String username;
    private String password;
    private String gender;
    private int age;
    private int weight;
    private int height;
    private int weightMetric;
    private int heightMetric;
    public GraphList userGraphData;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GraphList getUserGraphData() { return userGraphData; }

    public void setUserGraphData(GraphList userGraphData) {
        this.userGraphData = userGraphData;
    }

    public int getWeightMetric() { return weightMetric; }

    public void setWeightMetric(int weightMetric) {
        this.weightMetric = weightMetric;
    }

    public int getHeightMetric() { return heightMetric; }

    public void setHeightMetric(int heightMetric) {
        this.heightMetric = heightMetric;
    }

}
