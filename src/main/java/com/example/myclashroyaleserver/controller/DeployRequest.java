package com.example.myclashroyaleserver.controller;

public class DeployRequest {
    private String cardType;
    private int elixirCost;
    private double x;
    private double y;

    //Getter和Setter
    public String getCardType() {return cardType;}
    public void setCardType(String cardType) {this.cardType = cardType;}

    public int getElixirCost() {return elixirCost;}
    public void setElixirCost(int elixirCost) {this.elixirCost = elixirCost;}

    public double getX() {return x;}
    public void steX(double x) {this.x = x;}

    public double getY() {return y;}
    public void setY(double y) {this.y = y;}
}
