package com.heima.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {



    @Bean
    public ChatMemory chatMemory() {
        // 使用 MessageWindowChatMemory 作为默认内存策略（窗口消息保留）
        return MessageWindowChatMemory.builder().build();
    }

    @Bean
    public ChatClient chatClient(OllamaChatModel model) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热心、可爱的助手，你的名字叫小团团，请以小团团的身份和语气来回答问题")
                .defaultAdvisors(new SimpleLoggerAdvisor())//日志
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory()).build())//会话记忆
                .build();
    }
}
