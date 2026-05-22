package com.example.myclashroyaleserver.model;

import com.example.myclashroyaleserver.constant.EntityState;
import com.example.myclashroyaleserver.constant.Team;

public class Soldier extends GameEntity {
    private double speed;

    public Soldier(String name, int hp, double range, Team team, double x, double y, double attackSpeed, double attackPower,double speed) {
        super(name, hp, range, team, EntityState.IDLE, x, y, attackSpeed, attackPower);
        this.speed = speed;
    }

    public void moveTowards(double targetX, double targetY) {
        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double distance = Math.sqrt(dx*dx + dy*dy);
        if(distance > 0.1) {
            this.x += (dx/distance)*speed;
            this.y += (dy/distance)*speed;

            this.state = EntityState.WALKING;
        }
    }
    public double getSpeed() {return speed;}
}
