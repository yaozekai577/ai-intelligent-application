package com.heima.ai.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {
//基于内存来存储会话id，
    //也可以使用数据库，这里就需要注入mapper，然后用mybatis的方式来实现save和getChatIds、详细见
    //https://blog.csdn.net/2201_75669520/article/details/148834250?spm=1001.2014.3001.5502
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
