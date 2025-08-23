package com.heima.ai.config;

import com.heima.ai.constants.SystemConstants;
import com.heima.ai.tools.CourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    /**
     * 配置一个基于内存的简单向量数据库
     * @param embeddingModel
     * @return
     */
    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    /**
     * 配置会话记忆存储
     * @return
     */
    @Bean
    public ChatMemory chatMemory() {
        // 使用 MessageWindowChatMemory 作为默认内存策略（窗口消息保留）
        return MessageWindowChatMemory.builder().build();
    }

    /**
     * AI对话用ChatClient对象，用于处理用户输入的文本，并返回处理结果
     * @param model
     * @param chatMemory
     * @return
     */
    @Bean
    public ChatClient chatClient(OllamaChatModel model,ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem("你是一个热心、可爱的助手，你的名字叫小团团，请以小团团的身份和语气来回答问题")
                .defaultAdvisors(new SimpleLoggerAdvisor())//日志
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())//会话记忆
                .build();
    }

    /**
     * 哄哄模拟器游戏用ChatClient对象，用于模拟女友进行游戏
     * @param model
     * @param chatMemory
     * @return
     */
    @Bean
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)//系统配置
                .defaultAdvisors(new SimpleLoggerAdvisor())//日志
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())//会话记忆
                .build();
    }

    /**
     * 智能客服
     * @param model
     * @param chatMemory
     * @return
     */
    @Bean
    public ChatClient serviceChatClient(OpenAiChatModel model, ChatMemory chatMemory, CourseTools courseTools) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_PROMPT)//系统配置
                .defaultAdvisors(new SimpleLoggerAdvisor())//日志
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())//会话记忆
                .defaultTools(courseTools)
                .build();
    }

}
