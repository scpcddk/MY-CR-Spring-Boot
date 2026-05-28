package com.example.myclashroyaleserver.ai;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    @Autowired
    private OpenAiChatModel chatModel;

    public String chat(String userMessage) {
        return chatModel.call(userMessage);
    }
}
