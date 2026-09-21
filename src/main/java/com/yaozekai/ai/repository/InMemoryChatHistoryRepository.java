package com.yaozekai.ai.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {
    // 会话 id 基于内存存储，服务重启后失效
    // 如需持久化，可实现本接口的数据库版本：注入 Mapper，用 MyBatis 完成 save / getChatIds
    private final Map<String,List<String>> chatHistory = new HashMap<>();

    /**
     * 实现保存聊天记录功能
     * @param type 业务类型 如：chat、service、pdf
     * @param chatId 聊天会话id
     */
    @Override
    public void save(String type, String chatId) {
        //判断有无这个类型
        if(!chatHistory.containsKey(type)){
            chatHistory.put(type, new ArrayList<>());
        }
        //拿出这个id集合
        List<String> chatIds = chatHistory.get(type);

        //22-27行可以合并为
//        List<String> chatIds = chatHistory.computeIfAbsent(type, k -> new ArrayList<>());

        //判断id集合里有没有这个id
        if (chatIds.contains(chatId)) {
            return;
        }
        chatIds.add(chatId);

    }

    /**
     * 实现查询聊天记录id
     * @param type 业务类型 如：chat、service、pdf
     * @return
     */
    @Override
    public List<String> getChatIds(String type) {
        if(!chatHistory.containsKey(type)){
            return new ArrayList<>();
        }
        return chatHistory.get(type);
        //以上可以简化为一行
//        return chatHistory.getOrDefault(type, new ArrayList<>());
    }
}
