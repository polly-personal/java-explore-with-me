package ru.practicum.controller.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/compilations")
@RestController
public class CompilationsPublicController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationDto getForPublicUsersById(@PathVariable(name = "compId") long id) {
        log.info("🟫🟫 GET /compilations/{}", id);
        return compilationService.getForPublicUsersById(id);
    }

    @GetMapping
    public List<CompilationDto> getAllForPublicUsersByParameters(@RequestParam(required = false) Boolean pinned,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("🟫🟫 GET /compilations?pinned={}&from={}&size={}", pinned, from, size);
        return compilationService.getAllForPublicUsersByParameters(pinned, from, size);
    }
}
