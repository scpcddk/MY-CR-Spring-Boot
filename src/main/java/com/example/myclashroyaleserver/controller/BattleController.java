package com.example.myclashroyaleserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.myclashroyaleserver.engine.BattleField;

@RestController
@RequestMapping("/api/battle")
public class BattleController {
    @Autowired
    private BattleField battleField;

    @GetMapping("/status")
    public BattleStatusResponse getBattleStatus() {
        int elixir = battleField.getCurrentElixir();
        int troops = battleField.getAliveTroopsCount();
        int leftHp = battleField.getLeftTowerHealth();
        int rightHp = battleField.getRightTowerHealth();

        return new BattleStatusResponse(elixir,troops,leftHp,rightHp);
    }
}
