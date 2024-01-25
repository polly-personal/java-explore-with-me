package ru.practicum.controller.admin.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.categories.CategoryService;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@RestController
public class AdminCategoriesController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto post(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        log.info("🟫🟫 POST /admin/categories");
        return categoryService.create(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patch(@PathVariable(name = "catId") long id,
                             @RequestBody @Validated CategoryDto categoryDto) {
        log.info("🟫🟫 PATCH /admin/categories/{catId}", id);
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable(name = "catId") long id) {
        log.info("🟫🟫 DELETE /admin/categories/{catId}", id);
        categoryService.deleteById(id);
    }
}
