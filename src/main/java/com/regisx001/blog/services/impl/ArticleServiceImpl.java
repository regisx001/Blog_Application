package com.regisx001.blog.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.requests.CreateArticleRequest;
import com.regisx001.blog.domain.dto.requests.UpdateArticleRequest;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.domain.entities.Tag;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.repositories.CategoryRepository;
import com.regisx001.blog.repositories.TagRepository;
import com.regisx001.blog.services.ArticleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    public Page<ArticleDto> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(articleMapper::toDto);
    }

    @Override
    public Article createArticle(CreateArticleRequest articleRequest, User author) {
        // Find or create tags
        List<Tag> tags = articleRequest.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository
                                .save(Tag.builder().name(tagName).slug(tagName.toLowerCase()).build())))
                .collect(Collectors.toList());

        Category category = categoryRepository.findByTitle(articleRequest.getCategory()).orElse(null);

        Article article = Article.builder()
                .category(category)
                .tags(tags)
                .user(author)
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .tags(tags)
                .build();

        return articleRepository.save(article);
    }

    @Override
    public Article getArticleById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArticleById'");
    }

    @Override
    public Article updateArticle(UUID id, UpdateArticleRequest Article) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateArticle'");
    }

    @Override
    public void deleteArticle(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteArticle'");
    }

}
