package com.regisx001.blog.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.requests.CreateArticleRequest;
import com.regisx001.blog.domain.dto.requests.UpdateArticleRequest;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.repositories.CategoryRepository;
import com.regisx001.blog.services.ArticleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ArticleDto> getAllArticles(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllArticles'");
    }

    @Override
    public Article createArticle(CreateArticleRequest articleRequest, User author) {
        Category category = categoryRepository.findByTitle(articleRequest.getCategory()).orElse(null);

        Article article = Article.builder()
                .category(category)
                .user(author)
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
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
