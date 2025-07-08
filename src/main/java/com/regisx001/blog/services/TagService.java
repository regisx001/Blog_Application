package com.regisx001.blog.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.regisx001.blog.domain.dto.TagDto;

public interface TagService {

    // Get tags
    Page<TagDto.Basic> getAllBasicTags(Pageable pageable);

    Page<TagDto.Detailed> getAllDetailedTags(Pageable pageable);

    Page<TagDto.WithCount> getPopularTags(Pageable pageable);

    List<TagDto.Option> getAllTagOptions();

    // Individual tag operations
    TagDto.Detailed getTagById(UUID id);

    TagDto.Detailed getTagByName(String name);

    // CRUD operations
    TagDto.Detailed createTag(TagDto.CreateRequest request);

    TagDto.Detailed updateTag(UUID id, TagDto.CreateRequest request);

    void deleteTag(UUID id);

    // Utility methods
    List<TagDto.Basic> getTagsByNames(List<String> names);

    List<TagDto.Basic> createTagsIfNotExist(List<String> names);
}