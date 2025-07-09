package com.regisx001.blog.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.ArticleDto.UpdateRequest;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.domain.entities.Tag;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.exceptions.ItemNotFoundException;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.repositories.CategoryRepository;
import com.regisx001.blog.repositories.TagRepository;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.ArticleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ArticleMapper articleMapper;

    @Override
    public Page<ArticleDto.Detailed> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(articleMapper::toDetailedDto);
    }

    @Override
    public ArticleDto.Detailed getArticleById(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article Not found"));
        return articleMapper.toDetailedDto(article);
    }

    @Override
    public ArticleDto.Detailed createArticle(ArticleDto.CreateRequest request, UUID authorId) {
        // 1. Validate author exists
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));

        // 2. Validate and get category
        Category category = null;
        if (request.category() != null) {
            category = categoryRepository.findByTitle(request.category())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Category not found with id: " + request.category()));
        }

        // 3. Handle tags - create if they don't exist
        List<Tag> tags = List.of();
        if (request.tags() != null && !request.tags().isEmpty()) {
            tags = request.tags().stream()
                    .map(tagName -> tagRepository.findByName(tagName.trim().toLowerCase())
                            .orElseGet(() -> {
                                Tag newTag = Tag.builder()
                                        .name(tagName.trim().toLowerCase())
                                        .slug(slugify(tagName))
                                        .build();
                                return tagRepository.save(newTag);
                            }))
                    .collect(Collectors.toList());
        }

        // 4. Create article entity from request
        Article article = articleMapper.toEntity(request);

        // 5. Set relationships
        article.setUser(author);
        article.setCategory(category);
        article.setTags(tags);

        // 6. Set default values
        article.setIsPublished(request.isPublished() != null ? request.isPublished() : false);
        if (article.getIsPublished()) {
            article.setPublishedAt(LocalDateTime.now());
        }

        // 7. Save article
        Article savedArticle = articleRepository.save(article);

        // 8. Return detailed DTO
        return articleMapper.toDetailedDto(savedArticle);
    }

    @Override
    public ArticleDto.Detailed updateArticle(UUID id, UpdateRequest request, UUID authorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateArticle'");
    }

    @Override
    public void deleteArticle(UUID id, UUID authorId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    @Override
    public ArticleDto.Detailed publishArticle(UUID id, UUID authorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'publishArticle'");
    }

    @Override
    public ArticleDto.Detailed unpublishArticle(UUID id, UUID authorId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unpublishArticle'");
    }

    @Override
    public Page<ArticleDto.Draft> getUserDrafts(UUID authorId, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserDrafts'");
    }

    @Override
    public Page<ArticleDto.Summary> getPublishedArticles(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPublishedArticles'");
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
}
