package com.example.myclashroyaleserver.factory;

import com.example.myclashroyaleserver.constant.Team;
import com.example.myclashroyaleserver.model.Soldier;
import com.example.myclashroyaleserver.model.Tower;

/**
 * 🏭 兵种制造工厂（经典设计模式：工厂模式）
 * 作用：将数值配置与业务逻辑隔离，Main 函数再也不用关心血量、攻击力等琐碎细节。
 * 完全适配你项目中的有参构造器，杜绝无参构造和setter的滥用。
 */
public class UnitFactory {
    /**
     * 👑 生产一个王子
     * 参数：(名称, 血量, 攻击范围, 阵营, X, Y, 攻击间隔, 攻击力, 移动速度)
     */
    public static Soldier createArcher(Team team, double x, double y) {
        String name = team == Team.BLUE ? "蓝方弓箭手" : "红方弓箭手";
        return new Soldier(name, 120, 5.0, team, x, y, 1.0, 15, 1.0);
    }

    /**
     * 🗼 生产一座公主塔
     * 参数：(名称, 血量, 攻击范围, 阵营, X, Y, 攻击间隔, 攻击力)
     */
    public static Tower createPrincessTower(Team team, double x, double y) {
        String name = team == Team.BLUE ? "蓝方公主塔" : "红方公主塔";
        return new Tower(name, 500, 7.5, team, x, y, 1.2, 25);
    }

    // 生产一个巨人
    public static Soldier createGiant(Team team, double x, double y) {
        String name = team == Team.BLUE ? "蓝方巨人" : "红方巨人";
        return new Soldier(name, 800, 1.0, team, x, y, 1.5, 60, 0.8);
    }
}
