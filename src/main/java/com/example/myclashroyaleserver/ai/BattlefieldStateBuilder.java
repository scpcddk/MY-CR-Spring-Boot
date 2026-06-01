package com.example.myclashroyaleserver.ai;

import com.example.myclashroyaleserver.engine.BattleField;
import com.example.myclashroyaleserver.player.Player;
import org.springframework.stereotype.Component;
import com.example.myclashroyaleserver.model.GameEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BattlefieldStateBuilder {

    public String buildState(BattleField battleField, Player currentPlayer) {
        StringBuilder sb = new StringBuilder();
        sb.append("当前圣水: ").append(battleField.getCurrentElixir()).append("\n");
        sb.append("手牌: ").append(formatHand(currentPlayer)).append("\n");
        sb.append("场上友方单位: ").append(formatEntities(battleField.getEntitiesByTeam(currentPlayer.getTeam()))).append("\n");
        sb.append("场上敌方单位: ").append(formatEntities(battleField.getEntitiesByTeam(currentPlayer.getTeam().opposite()))).append("\n");
        return sb.toString();
    }

    private String formatHand(Player player) {
        return player.getDeck().getHand().stream().map(card -> card.getCardName()).collect(Collectors.joining(", "));
    }

    private String formatEntities(List<GameEntity> entities) {
        if(entities.isEmpty())
            return "无";
        return entities.stream().map(e -> e.getName() + "(HP:" + e.getHp() + ", x:" + e.getX() + ", y:" + e.getY() + ")").collect(Collectors.joining("; "));
    }
}
