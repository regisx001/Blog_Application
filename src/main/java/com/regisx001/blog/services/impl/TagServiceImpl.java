package com.regisx001.blog.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.regisx001.blog.domain.dto.ArticleDto;
import com.regisx001.blog.domain.dto.TagDto.Basic;
import com.regisx001.blog.domain.dto.TagDto.CreateRequest;
import com.regisx001.blog.domain.dto.TagDto.Detailed;
import com.regisx001.blog.domain.dto.TagDto.Option;
import com.regisx001.blog.domain.dto.TagDto.WithCount;
import com.regisx001.blog.mappers.ArticleMapper;
import com.regisx001.blog.repositories.ArticleRepository;
import com.regisx001.blog.services.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    // private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public Page<Basic> getAllBasicTags(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllBasicTags'");
    }

    @Override
    public Page<Detailed> getAllDetailedTags(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllDetailedTags'");
    }

    @Override
    public Page<WithCount> getPopularTags(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPopularTags'");
    }

    @Override
    public List<Option> getAllTagOptions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTagOptions'");
    }

    @Override
    public Detailed getTagById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTagById'");
    }

    @Override
    public Detailed getTagByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTagByName'");
    }

    @Override
    public Detailed createTag(CreateRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createTag'");
    }

    @Override
    public Detailed updateTag(UUID id, CreateRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTag'");
    }

    @Override
    public void deleteTag(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTag'");
    }

    @Override
    public List<Basic> getTagsByNames(List<String> names) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTagsByNames'");
    }

    @Override
    public List<Basic> createTagsIfNotExist(List<String> names) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createTagsIfNotExist'");
    }

    @Override
    public Page<ArticleDto.Detailed> getTagRelatedArticle(Pageable pageable,
            String tagName) {

        return articleRepository.findArticlesByTagName(tagName, pageable).map(articleMapper::toDetailedDto);
    }

}
