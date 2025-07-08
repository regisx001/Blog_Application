package com.regisx001.blog.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.regisx001.blog.domain.dto.TagDto;
import com.regisx001.blog.domain.entities.Article;
import com.regisx001.blog.domain.entities.Tag;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TagMapper {

    // ============= TO DTO MAPPINGS =============

    public abstract TagDto.Basic toBasicDto(Tag tag);

    public abstract TagDto.Detailed toDetailedDto(Tag tag);

    @Mapping(target = "articlesCount", source = "articles", qualifiedByName = "calculateArticlesCount")
    public abstract TagDto.WithCount toWithCountDto(Tag tag);

    public abstract TagDto.Option toOptionDto(Tag tag);

    // ============= TO ENTITY MAPPINGS =============

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "articles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Tag toEntity(TagDto.CreateRequest request);

    // ============= HELPER METHODS =============

    @Named("calculateArticlesCount")
    protected Integer calculateArticlesCount(List<Article> articles) {
        return articles != null ? articles.size() : 0;
    }
}