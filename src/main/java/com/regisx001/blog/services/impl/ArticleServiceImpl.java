package com.regisx001.blog.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.ArticleDto.Detailed;
import com.regisx001.blog.domain.dto.ArticleDto.RejectionRequest;
import com.regisx001.blog.domain.dto.ArticleDto.UpdateRequest;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.domain.entities.Tag;
import com.regisx001.blog.domain.entities.User;
import com.regisx001.blog.domain.entities.Enums.ArticleStatus;
import com.regisx001.blog.exceptions.ArticleNotApprovedException;
import com.regisx001.blog.exceptions.ItemNotFoundException;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.repositories.CategoryRepository;
import com.regisx001.blog.repositories.UserRepository;
import com.regisx001.blog.services.AIAnalyseService;
import com.regisx001.blog.services.ArticleService;
import com.regisx001.blog.services.StorageService;
import com.regisx001.blog.services.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagService tagService;
    private final ArticleMapper articleMapper;
    private final StorageService storageService;
    private final AIAnalyseService aiAnalyseService;

    @Override
    public Page<ArticleDto.Detailed> getPublishedArticles(Pageable pageable) {
        return articleRepository.findArticlesByStatus(ArticleStatus.PUBLISHED, pageable)
                .map(articleMapper::toDetailedDto);
    }

    @Override
    public ArticleDto.Detailed getArticleById(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article Not found"));
        // Check if user can access this article
        boolean isPublished = article.getStatus() == ArticleStatus.PUBLISHED;
        // boolean isAdmin = user != null && user.getRoles().stream()
        // .anyMatch(role -> "ROLE_ADMIN".equals(role.getName().name()));

        // Allow access if: article is published OR user is owner OR user is admin
        if (!isPublished) {
            throw new ItemNotFoundException("Article Not found");
        }
        return articleMapper.toDetailedDto(article);
    }

    @Override
    public Detailed getArticleByIdAndUser(UUID id, User user) {
        // TODO : FIX SOME WEIRD BUG HRER
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article Not found"));
        // Check if user can access this article
        boolean isPublished = article.getStatus() == ArticleStatus.PUBLISHED;
        boolean isOwner = false;
        boolean isAdmin = false;

        if (user != null) {
            isOwner = user.getId().equals(article.getUser().getId());
            isAdmin = user != null && user.getRoles().stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getName().name()));
        }

        // Allow access if: article is published OR user is owner OR user is admin
        if (!isPublished && !isAdmin && !isOwner) {
            throw new ItemNotFoundException("Article Not found");
        }
        return articleMapper.toDetailedDto(article);
    }

    @Override
    public Page<Detailed> getArticlesByUser(UUID userId, Pageable pageable) {
        return articleRepository.findArticlesByUserId(userId, pageable).map(articleMapper::toDetailedDto);
    }

    @Override
    public Page<Detailed> getDraftArticlesByUser(UUID userId, Pageable pageable) {
        return articleRepository.findArticlesByUserIdAndStatus(userId, ArticleStatus.DRAFT, pageable)
                .map(articleMapper::toDetailedDto);
    }

    @Override
    public Page<Detailed> searchArticles(String searchTerms, Pageable pageable) {
        return articleRepository.searchPublishedArticles(searchTerms, pageable).map(articleMapper::toDetailedDto);
    }

    @Override
    public ArticleDto.Detailed createArticle(ArticleDto.CreateRequest request, UUID authorId) {

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + authorId));

        Category category = null;
        if (request.category() != null) {
            category = categoryRepository.findByTitle(request.category())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Category not found with id: " + request.category()));
        }

        Article article = articleMapper.toEntity(request);

        List<Tag> tags = tagService.createTagsIfNotExist(request.tags());
        ;

        String imagePath = null;
        if (request.featuredImage() != null && !request.featuredImage().isEmpty()) {
            try {
                imagePath = storageService.store(request.featuredImage());
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
            }
        }

        // 5. Set relationships
        article.setUser(author);
        article.setFeaturedImage(imagePath);
        article.setCategory(category);
        article.setTags(tags);
        if (request.draft() != null && request.draft() == true) {
            article.setStatus(ArticleStatus.DRAFT);
        } else {
            article.setStatus(ArticleStatus.PENDING_REVIEW);
        }

        article.setIsPublished(request.isPublished() != null ? request.isPublished() : false);
        if (article.getIsPublished()) {
            article.setPublishedAt(LocalDateTime.now());
        }

        Article savedArticle = articleRepository.save(article);

        if (tags.isEmpty() || tags == null) {
            tagService.generateTagsAsync(savedArticle);
        }

        aiAnalyseService.analyseArticle(savedArticle.getId());

        return articleMapper.toDetailedDto(savedArticle);
    }

    @Override
    public ArticleDto.Detailed updateArticle(UUID id, UpdateRequest request, UUID authorId) {
        // 1. Validate input parameters
        if (id == null) {
            throw new IllegalArgumentException("Article ID cannot be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Update request cannot be null");
        }
        if (authorId == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }

        // 2. Find and validate article existence
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article not found with id: " + id));

        // 3. Validate user exists and has permission to update
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + authorId));

        // 4. Check ownership and permissions
        boolean isOwner = article.getUser().getId().equals(authorId);
        boolean isAdmin = author.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName().name()));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to update this article");
        }

        // 5. Validate article status - only allow updates for certain statuses
        if (article.getStatus() == ArticleStatus.PUBLISHED && !isAdmin) {
            throw new IllegalStateException("Published articles can only be updated by administrators");
        }

        // 6. Update basic fields (title and content are required in DTO)
        article.setTitle(request.title().trim());
        article.setContent(request.content().trim());

        // 7. Update category if provided (using categoryId from DTO)
        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(
                            () -> new ItemNotFoundException("Category not found with id: " + request.categoryId()));
            article.setCategory(category);
        }

        // 8. Update tags if provided
        if (request.tags() != null) {
            List<Tag> newTags = tagService.createTagsIfNotExist(request.tags());
            article.setTags(newTags);
        }

        // 9. Handle featured image update
        if (request.featuredImage() != null && !request.featuredImage().isEmpty()) {
            try {
                // Store new image (note: old image cleanup not implemented in StorageService)
                String newImagePath = storageService.store(request.featuredImage());
                article.setFeaturedImage(newImagePath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update featured image: " + e.getMessage(), e);
            }
        }

        // 14. Save the updated article
        Article savedArticle = articleRepository.save(article);

        // 16. Return updated article DTO
        return articleMapper.toDetailedDto(savedArticle);
    }

    @Override
    public void deleteArticle(UUID id, UUID authorId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    @Override
    public ArticleDto.Detailed publishArticle(UUID id, UUID authorId) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article Not found"));

        if (article.getStatus() == ArticleStatus.APPROVED) {
            article = changeArticleStatus(id, ArticleStatus.PUBLISHED);
            article.setIsPublished(true);
            article.setPublishedAt(LocalDateTime.now());
        } else {
            throw new ArticleNotApprovedException("Article must be approved before publishing");
        }
        return articleMapper.toDetailedDto(articleRepository.save(article));
    }

    @Override
    public ArticleDto.Detailed unpublishArticle(UUID id, UUID authorId) {
        // TODO: CHECK OWNERSHIP AND PERMISIONS

        Article article = changeArticleStatus(id, ArticleStatus.DRAFT);
        article.setIsPublished(false);
        return articleMapper.toDetailedDto(articleRepository.save(article));
    }

    @Override
    public Page<ArticleDto.Draft> getUserDrafts(UUID authorId, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserDrafts'");
    }

    @Override
    public Detailed sendForReview(UUID id, UUID authorId) {

        // TODO: CHECK OWNERSHIP AND PERMISIONS

        Article article = changeArticleStatus(id, ArticleStatus.PENDING_REVIEW);
        return articleMapper.toDetailedDto(articleRepository.save(article));
    }

    @Override
    public Detailed unsendForReview(UUID id, UUID authorId) {
        // TODO: CHECK OWNERSHIP AND PERMISIONS
        Article article = changeArticleStatus(id, ArticleStatus.DRAFT);
        return articleMapper.toDetailedDto(articleRepository.save(article));
    }

    @Override
    public Detailed approveArticle(UUID id, ArticleDto.ApproveRequest approveRequest, User adminUser) {
        boolean isAdmin = adminUser != null && adminUser.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName().name()));

        if (!isAdmin) {
            throw new AccessDeniedException("You not allowed to access this endpoint");
        }

        Article article = changeArticleStatus(id, ArticleStatus.APPROVED);
        article.setApprovedAt(LocalDateTime.now());
        if (adminUser != null) {
            article.setApprovedBy(adminUser.getUsername());
        }
        article.setFeedback(approveRequest.feedback());
        return articleMapper.toDetailedDto(articleRepository.save(article));
    }

    @Override
    public Detailed rejectArticle(UUID id, RejectionRequest rejectionRequest, User adminUser) {
        boolean isAdmin = adminUser != null && adminUser.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName().name()));

        if (!isAdmin) {
            throw new AccessDeniedException("You not allowed to access this endpoint");
        }

        Article article = changeArticleStatus(id, ArticleStatus.REJECTED);
        article.setRejectedAt(LocalDateTime.now());
        if (adminUser != null) {
            article.setRejectedBy(adminUser.getUsername());
        }
        article.setFeedback(rejectionRequest.feedback());
        return articleMapper.toDetailedDto(articleRepository.save(article));
    }

    @Override
    public void deleteArticlesInBatchById(Iterable<UUID> ids) {
        articleRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public Page<Detailed> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(articleMapper::toDetailedDto);
    }

    @Override
    public Page<Detailed> getArticlesByStatus(ArticleStatus status, Pageable pageable) {

        return articleRepository.findArticlesByStatus(status, pageable).map(articleMapper::toDetailedDto);
    }

    private Article changeArticleStatus(UUID id, ArticleStatus status) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article not found"));
        if (article.getStatus() == status) {
            return article;
        }
        article.setStatus(status);
        article.setApprovedAt(null);
        article.setApprovedBy(null);
        article.setRejectedAt(null);
        article.setRejectedBy(null);
        article.setPublishedAt(null);
        article.setIsPublished(false);
        return article;
    }

    @Override
    public Page<Detailed> getReviewArticlesByUser(UUID userId, Pageable pageable) {
        return articleRepository.findArticlesSubmittedForReviewByUser(userId, pageable)
                .map(articleMapper::toDetailedDto);
    }

    @Override
    public Page<Detailed> getAllArticlesByFilters(String searchTerm, ArticleStatus status, Pageable pageable) {
        return articleRepository.findAllBySearchAndStatus(searchTerm, status, pageable)
                .map(articleMapper::toDetailedDto);
    }

    @Override
    public Detailed draftArticle(UUID id, User user) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Article Not found"));
        // Check if user can access this article

        boolean isOwner = false;
        boolean isAdmin = false;

        if (user != null) {
            isOwner = user.getId().equals(article.getUser().getId());
            isAdmin = user != null && user.getRoles().stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getName().name()));
        }

        // Allow access if: article is published OR user is owner OR user is admin
        if (!isAdmin && !isOwner) {
            throw new ItemNotFoundException("Cannot perform this action");
        }
        article.setStatus(ArticleStatus.DRAFT);

        return articleMapper.toDetailedDto(articleRepository.save(article));
    }

    @Async
    @Override
    public void analyseArticleByAI(UUID id) {
        aiAnalyseService.analyseArticle(id);
    }
}
