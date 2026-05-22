package com.example.myclashroyaleserver.engine;

import org.springframework.stereotype.Component;

@Component
public class ElixirManager {
    private double elixir = 5.0;
    private final double maxElixir = 10.0;
    private final double elixirPerTick = 0.05;

    public void update(){
        if(elixir < maxElixir) {
            elixir += elixirPerTick;
            if(elixir > maxElixir)
                elixir = maxElixir;
        }
    }

    public boolean consume(int cost) {
        if(this.elixir >= cost) {
            this.elixir -= cost;
            return true;
        }
        return false;
    }

    public int getAvailableElixir() {
        return (int)Math.floor(this.elixir);
    }
}
