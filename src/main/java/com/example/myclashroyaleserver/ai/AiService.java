package com.example.myclashroyaleserver.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${deepseek.api-key}")
    private String apiKey;

    private final String DEEPSEEK_URL = "https://api.deepseek.com/v1/chat/completions";

    // 普通对话（用于 /api/ai/chat）
    public String chat(String userMessage) {
        try {
            System.out.println("收到请求: " + userMessage);
            Map<String, Object> requestBody = Map.of(
                    "model", "deepseek-chat",
                    "messages", List.of(Map.of("role", "user", "content", userMessage)),
                    "temperature", 0.7
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            System.out.println("发送请求到: " + DEEPSEEK_URL);
            ResponseEntity<Map> response = restTemplate.postForEntity(DEEPSEEK_URL, entity, Map.class);
            System.out.println("收到响应状态: " + response.getStatusCode());
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace(); // 强制打印
            throw new RuntimeException("DeepSeek API 调用失败", e);
        }
    }

    // 决策方法（用于 AI 自动出牌）
    public AiAction decideAction(String battlefieldState) {
        String prompt = buildDecisionPrompt(battlefieldState);
        String response = chat(prompt);  // 复用 chat 方法
        return parseAction(response);
    }

    private String buildDecisionPrompt(String state) {
        return """
            你是一个《皇室战争》AI 玩家。根据当前战场状态，决定下一步动作。
            战场状态如下：
            """ + state + """
            
            请严格返回 JSON 格式，不要包含额外文字：
            {"action": "deploy", "cardIndex": 0, "x": 10.0, "y": 5.0}
            或 {"action": "wait"}
            """;
    }

    private AiAction parseAction(String response) {
        try {
            String cleaned = response.trim();
            if (cleaned.startsWith("```json")) cleaned = cleaned.substring(7);
            if (cleaned.endsWith("```")) cleaned = cleaned.substring(0, cleaned.length() - 3);
            JsonNode node = objectMapper.readTree(cleaned);
            String action = node.get("action").asText();
            if ("deploy".equals(action)) {
                int cardIndex = node.get("cardIndex").asInt();
                double x = node.get("x").asDouble();
                double y = node.get("y").asDouble();
                return new AiAction(ActionType.DEPLOY, cardIndex, x, y);
            } else {
                return new AiAction(ActionType.WAIT);
            }
        } catch (Exception e) {
            System.err.println("解析失败，原始响应: " + response);
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

        public ActionType getType() { return type; }
        public Integer getCardIndex() { return cardIndex; }
        public Double getX() { return x; }
        public Double getY() { return y; }
    }

    public enum ActionType {
        DEPLOY, WAIT
    }
}