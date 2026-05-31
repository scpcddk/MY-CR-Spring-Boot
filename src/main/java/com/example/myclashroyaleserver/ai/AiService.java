package com.example.myclashroyaleserver.ai;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiService {

    @Autowired
    private OpenAiChatModel chatModel;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // AI 决策方法
    public AiAction decideAction(String battlefieldState) {
        String prompt = buildDecisionPrompt(battlefieldState);
        String response = chatModel.call(prompt);
        return parseAction(response);
    }
    public String chat(String userMessage) {
        return chatModel.call(userMessage);
    }

    private String buildDecisionPrompt(String state) {
        return """
                你是一个《皇室战争》AI 玩家。根据当前战场状态，决定下一步动作。
                战场状态如下：
                """ + state + """
                
                请严格按照以下 JSON 格式返回，不要包含任何额外解释或文字：
                - 如果要部署卡牌，返回 {"action": "deploy", "cardIndex": 0, "x": 10.0, "y": 5.0}
                - 如果等待（不出牌），返回 {"action": "wait"}
                
                注意：cardIndex 从 0 开始，x 和 y 为浮点数（范围 0-20）。
                只返回 JSON，不要有其他内容。
                """;
    }

    private AiAction parseAction(String response) {
        try {
            // 清理可能的 markdown 代码块标记
            String cleaned = response.trim();
            if(cleaned.startsWith("```json")) {
                cleaned = cleaned.substring(7);
            }
            if(cleaned.endsWith("```")) {
                cleaned = cleaned.substring(0, cleaned.length() - 3);
            }
            JsonNode node = objectMapper.readTree(cleaned);
            String action = node.get("action").asText();
            if("deploy".equals(action)) {
                int cardIndex = node.get("cardIndex").asInt();
                double x = node.get("x").asDouble();
                double y = node.get("y").asDouble();
                return new AiAction(ActionType.DEPLOY,cardIndex,x,y);
            } else {
                return new AiAction(ActionType.WAIT);
            }
        } catch (Exception e) {
            System.out.println("解析失败，原始响应: " + response);
            return new AiAction(ActionType.WAIT);
        }
    }

    // 内部类定义动作
    public static class AiAction {
        private final ActionType type;
        private final Integer cardIndex;
        private final Double x;
        private final Double y;

        public AiAction(ActionType type) {
            this(type, null, null, null);
        }

        public AiAction(ActionType type, Integer cardIndex, Double x, Double y) {
            this.type = type;
            this.cardIndex = cardIndex;
            this.x = x;
            this.y = y;
        }

        // getters...
        public ActionType getType() { return type; }
        public Integer getCardIndex() { return cardIndex; }
        public Double getX() { return x; }
        public Double getY() { return y; }
    }

    public enum ActionType {
        DEPLOY, WAIT
    }
}
