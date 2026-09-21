package com.yaozekai.ai.repository;

import java.util.List;

public interface ChatHistoryRepository {

    /**
     * 保存会话记录
     * @param type 业务类型 如：chat、service、pdf
     * @param chatId 聊天会话id
     */
    void save(String type,String chatId);


    /**
     * 获取会话id列表
     * @param type 业务类型 如：chat、service、pdf
     * @return 会话ID列表
     */
    List<String> getChatIds(String type);

}
