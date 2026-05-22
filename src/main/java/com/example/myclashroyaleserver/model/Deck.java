package com.example.myclashroyaleserver.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Deck {
    private final List<Card> hand = new ArrayList<>();      // 4张手牌
    private final Queue<Card> deckPool = new LinkedList<>();// 剩余牌库

    public Deck(List<Card> eightCards) {
        if(eightCards.size() != 8) {
            System.out.println("⚠️ 警告：标准卡组必须是8张！实际传入：" + eightCards.size());
        }
        for(int i = 0; i < eightCards.size(); i++) {
            if(i < 4)
                hand.add(eightCards.get(i));
            else
                deckPool.add(eightCards.get(i));
        }
    }

    public Card playCard(int index) {
        if(index < 0 || index >= hand.size())
            return null;
        Card used = hand.remove(index);
        Card next = deckPool.poll();
        if(next != null)
            hand.add(index,next);
        deckPool.add(used);
        return used;
    }

    public void printDeckStatus(String teamName) {
        System.out.printf("\n=== [%s 手牌看板] ===", teamName);
        for(int i = 0; i <hand.size();i++) {
            System.out.printf(" [%d: %s(%d费)]", i, hand.get(i).getCardName(), hand.get(i).getElixirCost());
        }
        Card next = deckPool.peek();
        System.out.printf(" | 🔮 下一张预测: [%s]\n", next != null ? next.getCardName() : "空");
    }
}
