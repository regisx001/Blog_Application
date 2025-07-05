package com.regisx001.blog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;

import com.regisx001.blog.domain.dto.CategoryDto;
import com.regisx001.blog.domain.entities.Category;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapper {

    @Value("${app.base-url:http://localhost:8080}")
    protected String baseUrl;

    @Mapping(target = "image", source = "image", qualifiedByName = "imageNameToFullUri")
    public abstract CategoryDto toDto(Category category);

    public abstract Category toEntity(CategoryDto categoryDto);

    @Named("imageNameToFullUri")
    protected String imageNameToFullUri(String image) {
        if (image == null || image.isBlank()) {
            return null;
        }
        // Remove leading slash if present to avoid double slashes
        String cleanImage = image.startsWith("/") ? image.substring(1) : image;
        return baseUrl + "/uploads/" + cleanImage;
    }
}