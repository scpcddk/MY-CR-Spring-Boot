package com.example.myclashroyaleserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.myclashroyaleserver.engine.BattleField;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/battle")
public class BattleController {
    @Autowired
    private BattleField battleField;

    @GetMapping("/{battleId}")
    public BattleStatusResponse getBattleStatus() {
        int elixir = battleField.getCurrentElixir();
        int troops = battleField.getAliveTroopsCount();
        int leftHp = battleField.getLeftTowerHealth();
        int rightHp = battleField.getRightTowerHealth();

        return new BattleStatusResponse(elixir,troops,leftHp,rightHp);
    }

    @PostMapping("/{battleId}/cards")
    public ResponseEntity<DeployResponse> deployCard(@RequestBody DeployRequest request) {
        // 检查圣水是否充足（临时用 BattleField 的 getCurrentElixir() 返回 5）
        if (battleField.getCurrentElixir() < request.getElixirCost()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DeployResponse(false, "Elixir insufficient"));
        }

        boolean success = battleField.deployUnit(request.getCardType(),request.getX(),request.getY());
        if (success) {
            battleField.deductElixir(request.getElixirCost());
            return ResponseEntity.ok(new DeployResponse(true,"Deployed successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DeployResponse(false,"Invalid deploy position"));
        }
    }
}
