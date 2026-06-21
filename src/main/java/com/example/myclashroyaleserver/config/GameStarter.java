package com.example.myclashroyaleserver.config;

import com.example.myclashroyaleserver.engine.GameLoop;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
public class GameStarter implements CommandLineRunner {
    private final GameLoop gameLoop;

    public GameStarter(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Spring Boot 容器就绪，开始 30Hz 游戏循环 ===");
        gameLoop.start(); // 启动虚拟线程游戏时钟
    }
}
