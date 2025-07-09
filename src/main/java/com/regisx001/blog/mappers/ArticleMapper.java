package com.regisx001.blog.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.dto.TagDto;
import com.regisx001.blog.domain.dto.UserDto;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Category;
import com.regisx001.blog.domain.entities.Tag;
import com.regisx001.blog.domain.entities.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ArticleMapper {

    @Value("${app.base-url:http://localhost:8080}")
    protected String baseUrl;

    @Autowired
    protected CategoryMapper categoryMapper;

    @Autowired
    protected UserMapper userMapper;

    // ============= TO DTO MAPPINGS =============

    @Mapping(target = "featuredImage", source = "featuredImage", qualifiedByName = "imageNameToFullUri")
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToBasic")
    @Mapping(target = "user", source = "user", qualifiedByName = "mapUserToBasic")
    public abstract ArticleDto.Basic toBasicDto(Article article);

    @Mapping(target = "featuredImage", source = "featuredImage", qualifiedByName = "imageNameToFullUri")
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToBasic")
    @Mapping(target = "user", source = "user", qualifiedByName = "mapUserToBasic")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTagsToListString")
    public abstract ArticleDto.Detailed toDetailedDto(Article article);

    @Mapping(target = "featuredImage", source = "featuredImage", qualifiedByName = "imageNameToFullUri")
    @Mapping(target = "excerpt", source = "content", qualifiedByName = "createExcerpt")
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToBasic")
    @Mapping(target = "user", source = "user", qualifiedByName = "mapUserToBasic")
    public abstract ArticleDto.Summary toSummaryDto(Article article);

    @Mapping(target = "featuredImage", source = "featuredImage", qualifiedByName = "imageNameToFullUri")
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToBasic")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "mapTagsToBasic")
    public abstract ArticleDto.Draft toDraftDto(Article article);

    public abstract ArticleDto.Option toOptionDto(Article article);

    // ============= TO ENTITY MAPPINGS =============

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "featuredImage", ignore = true) // handled separately
    @Mapping(target = "publishedAt", ignore = true) // handled in service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true) // handled in service
    @Mapping(target = "category", ignore = true) // handled in service
    @Mapping(target = "tags", ignore = true) // handled in service
    public abstract Article toEntity(ArticleDto.CreateRequest request);

    // ============= HELPER METHODS =============

    @Named("imageNameToFullUri")
    protected String imageNameToFullUri(String image) {
        if (image.startsWith("http")) {
            return image;
        }

        if (image == null || image.isBlank()) {
            return null;
        }
        String cleanImage = image.startsWith("/") ? image.substring(1) : image;
        return baseUrl + "/uploads/" + cleanImage;
    }

    @Named("mapCategoryToBasic")
    protected CategoryDto.Basic mapCategoryToBasic(Category category) {
        return category != null ? categoryMapper.toBasicDto(category) : null;
    }

    @Named("mapUserToBasic")
    protected UserDto.Basic mapUserToBasic(User user) {
        return user != null ? userMapper.toBasicDto(user) : null;
    }

    @Named("mapTagsToBasic")
    protected List<TagDto.Basic> mapTagsToBasic(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        return tags.stream()
                .map(tag -> new TagDto.Basic(tag.getName()))
                .collect(Collectors.toList());
    }

    @Named("mapTagsToListString")
    protected List<String> mapTagsToListString(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        return tags.stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toList());
    }

    @Named("createExcerpt")
    protected String createExcerpt(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }

        if (content.length() <= 200) {
            return content;
        }

        return content.substring(0, 200) + "...";
    }
}