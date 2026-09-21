package com.yaozekai.ai.controller;

import com.yaozekai.ai.entity.vo.MessageVO;
import com.yaozekai.ai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    private final ChatHistoryRepository chatHistoryRepository;//在这里写了插ids的接口

    private final ChatMemory chatMemory;//会话记忆存在里面，所以要去里面拿

    /**
     * 获取会话ids集合
     * @param type
     * @return
     */
    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable("type") String type) {
        List<String> chatIds = chatHistoryRepository.getChatIds(type);
        return chatIds;
    }

    /**
     * 根据id获取会话详情
     * @param type
     * @param chatId
     * @return
     */
    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type,@PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);
        if (messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();

    }


}
