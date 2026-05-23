package com.example.myclashroyaleserver.engine;

import org.springframework.stereotype.Component;
import com.example.myclashroyaleserver.constant.EntityState;
import com.example.myclashroyaleserver.constant.Team;
import com.example.myclashroyaleserver.factory.UnitFactory;
import com.example.myclashroyaleserver.model.GameEntity;
import com.example.myclashroyaleserver.model.Soldier;
import com.example.myclashroyaleserver.model.Tower;
import com.example.myclashroyaleserver.util.MathUtil;
import com.example.myclashroyaleserver.model.Card;

import java.util.ArrayList;
import java.util.List;

@Component
public class BattleField {
    //实体集合：敌我双方所有的士兵，建筑
    private final List<GameEntity> entities = new ArrayList<>();
    private final java.util.Map<String,UnitCreator> cardRegistry = new java.util.HashMap<>();

    // 💧 蓝方和红方的圣水管理器
    private final ElixirManager blueElixir = new ElixirManager();
    private final ElixirManager redElixir = new ElixirManager();

    //动态出生：将卡牌放入战场
    public void addEntity(GameEntity entity) {
        entities.add(entity);
        System.out.println("[战场广播] 📥 " + entity.getTeam() + " 方的 " + entity.getName() + " 进入了战场！");
    }

    /*
    * 🧠 核心心跳刷新逻辑：每 1/30 秒驱动全场进行一次【扫描 -> 决策 -> 行动】
    */
    public void update() {
        // 💧 1. 圣水随时间流动而增长
        blueElixir.update();
        redElixir.update();

        // 打印当前圣水状态
        System.out.printf("[资源看板] 💧 蓝方圣水: %d | 🔴 红方圣水: %d\n",
                blueElixir.getAvailableElixir(), redElixir.getAvailableElixir());

        // 2. 行为决策阶段：遍历全场活物
        for(GameEntity entity : entities) {
            if(entity.getState() == EntityState.DEAD)
                continue;

            // 🛰️ 雷达扫描：抓出离当前单位最近的敌方
            GameEntity closestEnemy = findClosestEnemy(entity);

            if(closestEnemy != null) {
                // 计算两者的物理距离
                double distance = MathUtil.getDistance(entity.getX(), entity.getY(), closestEnemy.getX(), closestEnemy.getY());

                // 决策分支 A：如果敌人进入了我的攻击范围
                if (distance <= entity.getRange()) {
                    entity.setState(EntityState.ATTACKING);
                    // 挥刀对砍：对方扣血（此处强转 int 扣血）
                    closestEnemy.takeDamage((int)entity.getAttackPower());
                    System.out.println("[战斗] ⚔️ " + entity.getName() + " 正在猛烈攻击 " + closestEnemy.getName()
                            + "！对方剩余血量: " + closestEnemy.getHp());
                }

                // 决策分支 B：如果敌人太远，够不着
                else {
                    switch(entity) {
                        case Soldier soldier -> {
                            //士兵才移动
                            soldier.moveTowards(closestEnemy.getX(),closestEnemy.getY());
                            System.out.println("[移动] 🏃 " + soldier.getName() + " 正在奔向 " + closestEnemy.getName() + "，当前坐标: (" + String.format("%.2f", soldier.getX()) + ", " + String.format("%.2f", soldier.getY()) + ")");
                        }
                        case Tower tower -> {
                            //塔不动
                            tower.setState(EntityState.IDLE);
                        }
                        default -> {}
                    }
                }
            }
            else {
                //没有存活敌人
                entity.setState(EntityState.IDLE);
            }
        }

        //3.清理战场
        clearDeadEntities();
    }

    // 🛰️ 经典扫描算法：寻找距离 source 最近的异营实体
    private GameEntity findClosestEnemy(GameEntity source) {
        GameEntity closestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        for(GameEntity target:entities) {
            if(target == source)
                continue;//排除自己
            if(target.getState() == EntityState.DEAD)
                continue;//排除死人
            if(target.getTeam() == source.getTeam())
                continue;//排除队友

            double dist = MathUtil.getDistance(source.getX(),source.getY(),target.getX(),target.getY());

            if(dist < minDistance){
                minDistance = dist;
                closestEnemy = target;
            }
        }
        return closestEnemy;
    }

    //🗑️ 现代化安全收尸逻辑（彻底攻克 Java 并发修改异常大坑）
    private void clearDeadEntities() {
        // 利用 Java 8+ 的 removeIf 配合 Lambda 表达式，在底层由 JDK 保证线程安全和迭代器安全
        entities.removeIf(entity -> {
            if(entity.getHp() <= 0 || entity.getState() == EntityState.DEAD) {
                System.out.println("[战场广播] 💀 " + entity.getTeam() + " 方的 " + entity.getName() + " 已阵亡，清出战场。");
                return true;//返回true代表从ArrayList里面抹去
            }
            return false;//保留
        });
    }

    public List<GameEntity>getEntities() {
        return entities;
    }

    public ElixirManager getElixirManager(Team team) {
        return team == Team.BLUE ? blueElixir : redElixir;
    }

    /**
     * 🃏 部署卡牌：扣费 + 召唤实体
     * @param team 阵营
     * @param card 要打出的卡牌
     * @param x    部署X坐标
     * @param y    部署Y坐标
     * @return 是否部署成功
     */
    public boolean deployCard(Team team,Card card,double x,double y) {
        ElixirManager em = getElixirManager(team);

        // 1. 查注册表
        UnitCreator creator = cardRegistry.get(card.getCardName());
        if(creator == null) {
            System.out.println("[错误] ⚠️ 未知卡牌，没有登记出兵逻辑: " + card.getCardName());
            return false;
        }

        //2.扣费
        if(em.consume(card.getElixirCost())) {
            System.out.printf("[卡牌部署] 🎉 %s 成功打出 [%s]！(消耗圣水: %d 费)\n",
                    team == Team.BLUE ? "蓝色方" : "红色方",
                    card.getCardName(), card.getElixirCost());

            // 3. 从注册表取方法，直接调用
            this.addEntity(creator .create(team,x,y));
            return true;
        }else {
            System.out.printf("[卡牌部署] ❌ %s 尝试打出 [%s] 失败！圣水不足！(需要 %d 费)\n",
                    team == Team.BLUE ? "蓝色方" : "红色方",
                    card.getCardName(), card.getElixirCost());
            return false;
        }
    }

    public BattleField() {
        // 把卡牌名字和对应的工厂方法绑定
        cardRegistry.put("王子卡牌", UnitFactory::createPrincessTower);
        cardRegistry.put("弓箭手卡牌", UnitFactory::createArcher);
        // 未来加新卡，只需在这里加一行
    }

    public int getCurrentElixir() {
        return 5;
    }

    public int getAliveTroopsCount() {
        return 3;
    }

    public int getLeftTowerHealth() {
        return 2500;
    }

    public int getRightTowerHealth() {
        return 2500;
    }
}
