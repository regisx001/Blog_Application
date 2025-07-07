package com.regisx001.blog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.entities.Article;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class,
        CategoryMapper.class, TagMapper.class })
public interface ArticleMapper {

    Article toEntity(ArticleDto articleDto);

    ArticleDto toDto(Article article);

}