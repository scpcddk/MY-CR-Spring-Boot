package com.example.myclashroyaleserver.controller;

import com.example.myclashroyaleserver.ai.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    @Autowired
    private AiService aiService;

    @GetMapping("/chat")
    public String chat(@RequestParam String msg) {
        return aiService.chat(msg);
    }
}
