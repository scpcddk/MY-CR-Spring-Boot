package com.example.myclashroyaleserver.engine;

import com.example.myclashroyaleserver.constant.Team;
import com.example.myclashroyaleserver.model.GameEntity;

@FunctionalInterface
public interface UnitCreator {
    GameEntity create(Team team,double x,double y);
}
