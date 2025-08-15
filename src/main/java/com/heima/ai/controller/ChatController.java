package com.heima.ai.controller;

import com.heima.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    private final ChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt,String chatId){
        //1.保存会话id
        chatHistoryRepository.save("chat",chatId);
        //2.请求会话模型
        return chatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,chatId))
                .stream()
                .content();
    }

}
