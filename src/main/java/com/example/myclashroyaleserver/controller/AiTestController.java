package com.example.myclashroyaleserver.controller;

import com.example.myclashroyaleserver.ai.AiService;
import com.example.myclashroyaleserver.ai.BattlefieldStateBuilder;
import com.example.myclashroyaleserver.constant.Team;
import com.example.myclashroyaleserver.engine.BattleField;
import com.example.myclashroyaleserver.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    @Autowired
    private AiService aiService;
    @Autowired
    private BattleField battleField;
    @Autowired
    private BattlefieldStateBuilder stateBuilder;

    @GetMapping("/chat")
    public String chat(@RequestParam String msg) {
        try {
            return aiService.chat(msg);
        } catch (Exception e) {
            // 打印完整堆栈到控制台（Docker logs）
            e.printStackTrace();
            // 返回详细错误信息（供调试）
            return "Error: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }

    @GetMapping("/decide")
    public AiService.AiAction decide(@RequestParam String state) {
        return aiService.decideAction(state);
    }

    @GetMapping("/test-state")
    public String testState() {
        // 获取战场和当前玩家（假设红方为 AI）
        BattleField bf = battleField;  // 需要注入 BattleField
        Player player = bf.getPlayerByTeam(Team.RED);  // 需要先设置玩家
        if (player == null) return "No AI player";
        return stateBuilder.buildState(bf, player);
    }

    @PostMapping("/decide")
    public AiService.AiAction decidePost(@RequestBody String state) {
        return aiService.decideAction(state);
    }
}
