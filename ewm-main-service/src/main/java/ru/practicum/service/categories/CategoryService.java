package ru.practicum.service.categories;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.entity.category.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(long id, CategoryDto categoryDto);

    void deleteById(long id);

    Category checkAndGetEntityById(long id);

    CategoryDto getById(long id);

    List<CategoryDto> getAll(int from, int size);
}
