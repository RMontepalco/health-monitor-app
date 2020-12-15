package com.example.HealthMonitorApp;

public class GraphList {
    private GraphData one = new GraphData();
    private GraphData two = new GraphData();
    private GraphData three = new GraphData();
    private GraphData four = new GraphData();
    private GraphData five = new GraphData();
    private GraphData six = new GraphData();
    private GraphData seven = new GraphData();

    public GraphData getOne() {
        return one;
    }

    public void setOne(GraphData one) {
        this.one = one;
    }

    public GraphData getTwo() {
        return two;
    }

    public void setTwo(GraphData two) {
        this.two = two;
    }

    public GraphData getThree() {
        return three;
    }

    public void setThree(GraphData three) {
        this.three = three;
    }

    public GraphData getFour() {
        return four;
    }

    public void setFour(GraphData four) {
        this.four = four;
    }

    public GraphData getFive() {
        return five;
    }

    public void setFive(GraphData five) {
        this.five = five;
    }

    public GraphData getSix() {
        return six;
    }

    public void setSix(GraphData six) {
        this.six = six;
    }

    public GraphData getSeven() {
        return seven;
    }

    public void setSeven(GraphData seven) {
        this.seven = seven;
    }

    public void shiftData(GraphData newData) {
        one = two;
        two = three;
        three = four;
        four = five;
        five = six;
        six = seven;
        seven = newData;
    }
}
