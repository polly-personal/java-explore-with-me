package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.entity.compilation.Compilation;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto updateForAdmin(long id, UpdateCompilationRequest updateCompilationRequest);

    void deleteForAdminById(long id);

    Compilation checkAndGetEntityById(long id);

    CompilationDto getForPublicUsersById(long id);

    List<CompilationDto> getAllForPublicUsersByParameters(Boolean pinned, int from, int size);
}
