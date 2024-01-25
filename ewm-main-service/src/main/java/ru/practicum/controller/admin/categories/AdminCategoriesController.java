package ru.practicum.controller.admin.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.PatchValidation;
import ru.practicum.dto.PostValidation;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.categories.CategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@RestController
public class AdminCategoriesController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto post(@RequestBody /*@Validated(PostValidation.class)*/ @Valid NewCategoryDto newCategoryDto) {
        log.info("🟫🟫 POST /admin/categories");
        return categoryService.create(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patch(@PathVariable(name = "catId") long id,
                             @RequestBody /*@Validated(PatchValidation.class)*/ @Valid CategoryDto categoryDto) {
        log.info("🟫🟫 PATCH /admin/categories/{catId}", id);
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void delete(@PathVariable(name = "catId") long id) {
        log.info("🟫🟫 DELETE /admin/categories/{catId}", id);
        categoryService.deleteById(id);
    }
}
