package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.entity.category.Category;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class CategoryMapper {
    public Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = Category.builder()
                .name(newCategoryDto.getName())
                .build();

        log.info("🔀\nDTO={} сконвертирован в \nJPA-сущность={}", newCategoryDto, category);
        return category;
    }

    public Category toCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .build();

        log.info("🔀\nDTO={} сконвертирован в \nJPA-сущность={}", categoryDto, category);
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();

        log.info("🔀 \nJPA-сущность={} сконвертирована в \nDTO={}", category, categoryDto);
        return categoryDto;
    }

    public List<CategoryDto> toCategoryDtos(List<Category> categories) {
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());

        log.info("🔀 \nсписок JPA-сущностей={} сконвертирован в \nсписок DTO={}", categories, categoryDtos);
        return categoryDtos;
    }
}
