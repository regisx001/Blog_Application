package com.regisx001.blog.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.TagDto.Basic;
import com.regisx001.blog.domain.dto.TagDto.WithCount;
import com.regisx001.blog.domain.entities.Tag;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.mappers.TagMapper;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.repositories.TagRepository;
import com.regisx001.blog.services.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public Page<WithCount> getAllTagsWithCount(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(tagMapper::toWithCountDto);
    }

    @Override
    public void deleteTag(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTag'");
    }

    @Override
    public List<Tag> createTagsIfNotExist(List<String> names) {
        if (names == null || names.isEmpty()) {
            return List.of();
        }

        return names.stream()
                .map(tagName -> {
                    String cleanName = tagName.trim().toLowerCase();
                    String slug = slugify(tagName);

                    return tagRepository.findByName(cleanName).orElseGet(() -> {
                        Tag newTag = Tag.builder().name(cleanName).slug(slug).build();
                        return tagRepository.save(newTag);
                    });
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<ArticleDto.Detailed> getTagRelatedArticle(Pageable pageable,
            String tagName) {

        return articleRepository.findArticlesByTagName(tagName, pageable).map(articleMapper::toDetailedDto);
    }

    @Override
    public List<Basic> getTagsByNames(List<String> names) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTagsByNames'");
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
