package com.supportai.app.controller;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class KnowledgeBaseService implements CommandLineRunner {

    private final VectorStore vectorStore;

    public KnowledgeBaseService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws IOException {

        PathMatchingResourcePatternResolver resolver =
                new PathMatchingResourcePatternResolver();

        Resource[] resources =
                resolver.getResources("classpath:knowledge/*.txt");

        for (Resource resource : resources) {

            String content = resource.getContentAsString(
                    StandardCharsets.UTF_8
            );

            Document document = new Document(content);

            vectorStore.add(List.of(document));
        }
    }
}