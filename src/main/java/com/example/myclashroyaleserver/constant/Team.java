package com.example.myclashroyaleserver.constant;

public enum Team {
    BLUE,//玩家
    RED;//电脑

    public Team opposite() {
        return this == BLUE ? RED : BLUE;
    }
}
