package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.entity.compilation.Compilation;
import ru.practicum.entity.event.Event;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();

        log.info("🔀\nDTO: " + newCompilationDto + " сконвертирован в \nJPA-сущность: " + compilation);
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationDto.builder()
                .events(EventMapper.toEventShortDtos(compilation.getEvents()))
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();

        log.info("🔀 \nJPA-сущность: " + compilation + " сконвертирована в \nDTO: " + compilationDto);
        return compilationDto;
    }

    public List<CompilationDto> toCompilationDtos(List<Compilation> compilations) {
        List<CompilationDto> compilationDtos = compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());

        log.info("🔀 \nсписок JPA-сущностей: " + compilations + " сконвертирован в \nсписок DTO: " + compilationDtos);
        return compilationDtos;
    }
}
