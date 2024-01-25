package ru.practicum.controller.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.categories.CategoryService;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoriesPublicController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable(name = "catId") long id) {
        log.info("🟫🟫 GET /categories/{catId}", id);
        return categoryService.getById(id);
    }

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(required = false, defaultValue = "0") int from,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("🟫🟫 GET /categories?{from}=from&{size}=size", from, size);
        return categoryService.getAll(from, size);
    }
}
