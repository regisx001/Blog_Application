package com.regisx001.blog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Value;

import com.regisx001.blog.domain.dto.CategoryDtoRef;
import com.regisx001.blog.domain.entities.Category;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapperRef {

    @Value("${app.base-url:http://localhost:8080}")
    protected String baseUrl;

    // ============= TO DTO MAPPINGS =============

    @Mapping(target = "image", source = "image", qualifiedByName = "imageNameToFullUri")
    public abstract CategoryDtoRef.Basic toBasicDto(Category category);

    @Mapping(target = "image", source = "image", qualifiedByName = "imageNameToFullUri")
    public abstract CategoryDtoRef.Detailed toDetailedDto(Category category);

    public abstract CategoryDtoRef.Option toOptionDto(Category category);

    // ============= TO ENTITY MAPPINGS =============

    @Mapping(target = "id", ignore = true)
    // @Mapping(target = "articles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "image", ignore = true) // handled separately
    public abstract Category toEntity(CategoryDtoRef.CreateRequest request);

    @Mapping(target = "id", ignore = true)
    // @Mapping(target = "articles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "image", ignore = true) // handled separately
    public abstract Category toEntity(CategoryDtoRef.CreateWithImageRequest request);

    // ============= HELPER METHODS =============

    @Named("imageNameToFullUri")
    protected String imageNameToFullUri(String image) {
        if (image == null || image.isBlank()) {
            return null;
        }
        String cleanImage = image.startsWith("/") ? image.substring(1) : image;
        return baseUrl + "/uploads/" + cleanImage;
    }
}