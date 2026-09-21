package com.yaozekai.ai;

import com.yaozekai.ai.util.VectorDistanceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class AiIntelligentApplicationTests {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @Test
    public void testVectorStore(){
        Resource resource = new FileSystemResource("中二知识笔记.pdf");
        //1.创建pdf读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        //2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        //3.写入向量数据库
        vectorStore.add(documents);
        //4.搜索
            //4.1构建搜索条件
        SearchRequest request  = SearchRequest.builder()
                .query("论语中教育的目的是什么")
                .topK(1)
                .similarityThreshold(0.6)
                .filterExpression("file_name == '中二知识笔记.pdf'")
                .build();
            //4.2搜索
        List<Document> docs = vectorStore.similaritySearch(request);
        if (docs==null){
            System.out.println("没有搜索到任何内容");
            return;
        }
        for (Document document : docs) {
            System.out.println(document.getId());
            System.out.println(document.getScore());
            System.out.println(document.getText());
        }
    }


    @Test
    void contextLoads() {
        // 1.测试数据
        // 1.1.用来查询的文本
        String query = "how to build an AI application";

        // 1.2.用来做比较的文本，其中第一条与查询语义最接近
        String[] texts = new String[]{
                "基于 Spring AI 可以快速为大模型应用接入对话、工具调用与检索能力",
                "足球联赛的新赛季赛程已经公布，各队开始备战",
                "沿海城市本周将持续降雨，气象部门发布预警",
                "新款智能手机的影像系统在夜景拍摄上有明显提升",
                "城市轨道交通新线路开通，早晚高峰通勤时间缩短",
        };
        // 2.向量化
        // 2.1.先将查询文本向量化
        float[] queryVector = embeddingModel.embed(query);

        // 2.2.再将比较文本向量化，放到一个数组
        List<float[]> textVectors = embeddingModel.embed(Arrays.asList(texts));

        // 3.比较欧氏距离
        // 3.1.把查询文本自己与自己比较，肯定是相似度最高的
        System.out.println(VectorDistanceUtils.euclideanDistance(queryVector, queryVector));
        // 3.2.把查询文本与其它文本比较
        for (float[] textVector : textVectors) {
            System.out.println(VectorDistanceUtils.euclideanDistance(queryVector, textVector));
        }
        System.out.println("------------------");

        // 4.比较余弦距离
        // 4.1.把查询文本自己与自己比较，肯定是相似度最高的
        System.out.println(VectorDistanceUtils.cosineDistance(queryVector, queryVector));
        // 4.2.把查询文本与其它文本比较
        for (float[] textVector : textVectors) {
            System.out.println(VectorDistanceUtils.cosineDistance(queryVector, textVector));
        }
    }

}
