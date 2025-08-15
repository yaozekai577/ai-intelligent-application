package com.heima.ai.controller;

import com.heima.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class GameController {
    private final ChatClient gameChatClient;


    @RequestMapping(value = "/game",produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt, String chatId){
        //请求会话模型
        return gameChatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))
                .stream()
                .content();
    }
}
