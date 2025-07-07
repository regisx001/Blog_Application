package com.regisx001.blog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.entities.Article;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class,
        CategoryMapper.class, TagMapper.class })
public interface ArticleMapper {

    @Mapping(target = "category", ignore = true)
    Article toEntity(ArticleDto articleDto);

    @Mapping(target = "category", ignore = true)
    ArticleDto toDto(Article article);

}