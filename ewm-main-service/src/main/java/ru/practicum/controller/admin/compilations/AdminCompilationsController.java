package ru.practicum.controller.admin.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@RestController
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto post(@RequestBody @Validated NewCompilationDto newCompilationDto) {
        log.info("🟫🟫 POST /admin/compilations");
        log.info("🟤 пришедшие параметры: newCompilationDto={}", newCompilationDto);
        return compilationService.create(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patch(@PathVariable(name = "compId") long id,
                                @RequestBody @Validated UpdateCompilationRequest updateCompilationRequest) {
        log.info("🟫🟫 PATCH /admin/compilations/{}", id);
        log.info("🟤 пришедшие параметры: id={}, updateCompilationRequest={}", id, updateCompilationRequest);
        return compilationService.updateForAdmin(id, updateCompilationRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void deleteForAdminById(@PathVariable(name = "compId") long id) {
        log.info("🟫🟫 DELETE /admin/compilations/{}", id);
        compilationService.deleteForAdminById(id);
    }
}
