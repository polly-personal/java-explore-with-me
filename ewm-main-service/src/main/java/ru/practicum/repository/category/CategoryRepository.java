package ru.practicum.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
