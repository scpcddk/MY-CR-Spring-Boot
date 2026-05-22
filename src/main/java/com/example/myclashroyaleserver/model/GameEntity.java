package com.example.myclashroyaleserver.model;

import com.example.myclashroyaleserver.constant.Team;
import com.example.myclashroyaleserver.constant.EntityState;

public abstract class GameEntity {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected double range;//攻击范围
    protected Team team;//队伍
    protected EntityState state;//状态
    protected double x;
    protected double y;
    protected double attackSpeed;//攻击速度
    protected double attackPower;//攻击力

    public GameEntity(String name, int hp, double range, Team team, EntityState state, double x, double y, double attackSpeed, double attackPower) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.range = range;
        this.team = team;
        this.state = state;
        this.x = x;
        this.y = y;
        this.attackSpeed = attackSpeed;
        this.attackPower = attackPower;
    }
    public void takeDamage(int damage) {
        this.hp -= damage;
        if(this.hp <= 0) {
            this.hp = 0;
            this.state = EntityState.DEAD;
        }
    }

    //帧打印战场状态
    public String statusString(){
        return String.format("name:%s team:%s state:%s hp:%d/%.0f x:%.2f y:%.2f atk:%.1f atkSpd:%.2f range:%.1f",
                name, team, state, hp, (double)maxHp, x, y, attackPower, attackSpeed, range);
    }
    public String getName() {return name;}
    public int getHp() {return hp;}
    public int getMaxHp() {return maxHp;}
    public double getRange() {return range;}
    public Team getTeam() {return team;}
    public EntityState getState() {return state;}
    public double getX() {return x;}
    public double getY() {return y;}
    public double getAttackSpeed() {return attackSpeed;}
    public double getAttackPower() {return attackPower;}
    public void setState(EntityState state) {this.state = state;}
}
