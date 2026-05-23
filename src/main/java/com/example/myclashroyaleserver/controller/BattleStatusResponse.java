package com.example.myclashroyaleserver.controller;

public class BattleStatusResponse {
    private int currentElixir;
    private int aliveTroopCount;
    private int leftTowerHealth;
    private int rightTowerHealth;

    public BattleStatusResponse(int currentElixir,int aliveTroopCount,int leftTowerHealth,int rightTowerHealth) {
        this.currentElixir = currentElixir;
        this.aliveTroopCount = aliveTroopCount;
        this.leftTowerHealth = leftTowerHealth;
        this.rightTowerHealth = rightTowerHealth;
    }

    public int getCurrentElixir() {
        return currentElixir;
    }

    public int getAliveTroopCount() {
        return aliveTroopCount;
    }

    public int getLeftTowerHealth() {
        return leftTowerHealth;
    }

    public int getRightTowerHealth() {
        return rightTowerHealth;
    }
}
