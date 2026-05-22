package com.example.myclashroyaleserver.model;

import com.example.myclashroyaleserver.constant.Team;
import com.example.myclashroyaleserver.constant.EntityState;

public class Tower extends GameEntity{
    public Tower(String name, int hp, double range, Team team,
                 double x, double y, double attackSpeed, double attackPower) {
        super(name, hp, range, team, EntityState.IDLE, x, y, attackSpeed, attackPower);
    }
}
