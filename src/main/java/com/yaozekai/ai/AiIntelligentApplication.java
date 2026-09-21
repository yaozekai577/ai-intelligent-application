package com.yaozekai.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yaozekai.ai.mapper")
public class AiIntelligentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiIntelligentApplication.class, args);
    }

}
