package ru.practicum.repository.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "select * " +
            "from compilations as c " +
            "where c.is_pinned = :pinned or :pinned is null ", nativeQuery = true)
    Page<Compilation> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}
