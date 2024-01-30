package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.entity.compilation.Compilation;
import ru.practicum.entity.event.Event;
import ru.practicum.exception.MainExceptionIdNotFound;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.repository.compilation.CompilationRepository;
import ru.practicum.repository.event.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) newCompilationDto.setPinned(false);

        Set<Event> events;
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = Set.copyOf(eventRepository.findAllById(newCompilationDto.getEvents()));
        } else {
            events = new HashSet<>();
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events));

        log.info("🟩 администратором создана подборка={}", compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    public CompilationDto updateForAdmin(long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = checkAndGetEntityById(id);

        if (updateCompilationRequest.getEvents() != null && updateCompilationRequest.getEvents().size() != 0)
            compilation.setEvents(Set.copyOf(eventRepository.findAllById(updateCompilationRequest.getEvents())));

        if (updateCompilationRequest.getPinned() != null) compilation.setPinned(updateCompilationRequest.getPinned());

        if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle().isBlank())
            compilation.setTitle(updateCompilationRequest.getTitle());

        log.info("🟪 администратором обновлена подборка={}", compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    public void deleteForAdminById(long id) {
        checkAndGetEntityById(id);
        compilationRepository.deleteById(id);
        log.info("⬛️ администратором удалена подборка по ее id={}", id);
    }

    public Compilation checkAndGetEntityById(long id) {
        return compilationRepository.findById(id).orElseThrow(() -> new MainExceptionIdNotFound("Compilation with " +
                "id=" + id + " was not found"));
    }

    public CompilationDto getForPublicUsersById(long id) {
        Compilation compilation = checkAndGetEntityById(id);

        log.info("🟦 для публичных пользователей выдана подборка={}", compilation);
        return CompilationMapper.toCompilationDto(compilation);
    }

    public List<CompilationDto> getAllForPublicUsersByParameters(Boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequest);

        log.info("🟦 для публичных пользователей выданы списки подборок={}", compilations.toList());
        return CompilationMapper.toCompilationDtos(compilations.toList());
    }
}
