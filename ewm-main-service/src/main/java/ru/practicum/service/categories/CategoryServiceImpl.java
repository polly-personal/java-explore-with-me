package ru.practicum.service.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.entity.category.Category;
import ru.practicum.exception.MainExceptionIdNotFound;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.category.CategoryRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));

        log.info("🟩 создана категория={}", category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    public CategoryDto update(long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new MainExceptionIdNotFound("Category with id=" + id + " was not found"));

        category.setName(categoryDto.getName());
        Category updateCategory = categoryRepository.save(category);

        log.info("🟪 обновлено поле \"name\"={}", updateCategory);
        return CategoryMapper.toCategoryDto(updateCategory);
    }

    @Transactional
    public void deleteById(long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new MainExceptionIdNotFound("Category with id=" + id + " was not found"));

        categoryRepository.deleteById(id);
        log.info("⬛️ удалена категория по ее id={}", id);
    }

    public Category checkAndGetEntityById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new MainExceptionIdNotFound("Category with id=" + id +
                " was not found"));
    }

    public CategoryDto getById(long id) {
        Category category = checkAndGetEntityById(id);
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(category);

        log.info("🟦 выдана категория={}", categoryDto);
        return categoryDto;
    }


    public List<CategoryDto> getAll(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Category> categories = categoryRepository.findAll(pageRequest);

        List<CategoryDto> categoryDtos = CategoryMapper.toCategoryDtos(categories.toList());

        log.info("🟦 выдан список вещей={}", categoryDtos);
        return categoryDtos;
    }
}
