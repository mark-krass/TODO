package com.example.gambm.todolist;

public class UserInfo {

    private String goal, des;

    UserInfo(String goal, String des) {
        this.goal = goal;
        this.des = des;
    }

    public String getGoal() {
        return goal;
    }

    public String getDes() {
        return des;
    }
}
