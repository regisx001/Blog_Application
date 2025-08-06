package com.regisx001.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.TagDto.WithCount;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Tag;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.mappers.TagMapper;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.repositories.TagRepository;
import com.regisx001.blog.services.PromptService;
import com.regisx001.blog.services.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final ObjectMapper objectMapper;
    private final PromptService promptService;
    private final ChatClient chatClient;

    @Override
    public Page<WithCount> getAllTagsWithCount(Pageable pageable) {
        return tagRepository.findAllTagsOrderByArticleCountDesc(pageable)
                .map(tagMapper::toWithCountDto);
    }

    @Override
    public List<Tag> createTagsIfNotExist(List<String> names) {
        if (names == null || names.isEmpty()) {
            return List.of();
        }

        return names.stream()
                .map(tagName -> {
                    String cleanName = tagName.trim().toLowerCase();
                    String slug = slugify(tagName);

                    return tagRepository.findByName(cleanName).orElseGet(() -> {
                        Tag newTag = Tag.builder().name(cleanName).slug(slug).build();
                        return tagRepository.save(newTag);
                    });
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ArticleDto.Detailed> getTagRelatedArticle(Pageable pageable,
            String tagName) {

        return articleRepository.findArticlesByTagName(tagName, pageable).map(articleMapper::toDetailedDto);
    }

    private String slugify(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        return input.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special characters except spaces and hyphens
                .replaceAll("\\s+", "-") // Replace spaces with hyphens
                .replaceAll("-+", "-") // Replace multiple hyphens with single hyphen
                .replaceAll("^-|-$", ""); // Remove leading/trailing hyphens
    }

    /**
     * Shared tag generation logic for DRY principle.
     */
    private List<String> doGenerateTags(Article article) {
        try {
            if (article == null || article.getTitle() == null || article.getContent() == null) {
                throw new IllegalArgumentException("Article, title, and content cannot be null");
            }

            String response = chatClient.prompt(promptService.buildArticleAutoTagsGenerationPrompt(article))
                    .call()
                    .content();

            if (response == null || response.trim().isEmpty()) {
                throw new RuntimeException("AI service returned empty response");
            }

            String cleanedResponse = response.trim();

            List<String> tags = objectMapper.readValue(cleanedResponse, new TypeReference<List<String>>() {
            });

            if (tags == null || tags.isEmpty()) {
                return List.of(); // Return empty list instead of null
            }
            return tags.stream()
                    .filter(tag -> tag != null && !tag.trim().isEmpty())
                    .map(String::trim)
                    .distinct()
                    .limit(10)
                    .toList();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse AI response as JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate tags: " + e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void generateTagsAsync(Article article) {
        List<String> generatedTags = doGenerateTags(article);
        List<Tag> tags = createTagsIfNotExist(generatedTags);
        article.setTags(tags);
        articleRepository.save(article);
    }

    @Override
    public List<String> generateTagsManual(Article article) {
        return doGenerateTags(article);
    }

}
