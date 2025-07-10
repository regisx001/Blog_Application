package com.regisx001.blog.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.TagDto;
import com.regisx001.blog.domain.entities.Tag;

public interface TagService {

    // Get tags

    Page<TagDto.WithCount> getAllTagsWithCount(Pageable pageable);

    // Article related
    Page<ArticleDto.Detailed> getTagRelatedArticle(Pageable pageable, String tagName);

    void deleteTag(UUID id);

    // Utility methods
    List<TagDto.Basic> getTagsByNames(List<String> names);

    List<Tag> createTagsIfNotExist(List<String> names);
}