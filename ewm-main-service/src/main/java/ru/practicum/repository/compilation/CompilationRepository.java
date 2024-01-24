package ru.practicum.repository.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}
