package com.example.myclashroyaleserver.model;

public class Card {
    private String cardName;
    private int elixirCost;//圣水消耗
    private String type;//卡牌类型

    public Card(String cardName, int elixirCost, String type) {
        this.cardName = cardName;
        this.elixirCost = elixirCost;
        this.type = type;
    }

    public String getCardName() {return cardName;}
    public int getElixirCost() {return elixirCost;}
    public String getType() {return type;}
}
