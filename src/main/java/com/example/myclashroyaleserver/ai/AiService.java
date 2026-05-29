package com.example.myclashroyaleserver.ai;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    @Autowired
    private OpenAiChatModel chatModel;

    public String chat(String userMessage) {
        String key = System.getenv("DEEPSEEK_API_KEY");
        System.out.println("API Key prefix: " + (key == null ? "null" : key.substring(0, Math.min(10, key.length()))));
        return chatModel.call(userMessage);
    }
}
