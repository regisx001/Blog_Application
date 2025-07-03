package com.regisx001.blog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.regisx001.blog.domain.dto.TagDto;
import com.regisx001.blog.domain.entities.Tag;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    Tag toEntity(TagDto tagDto);

    TagDto toDto(Tag tag);
}
