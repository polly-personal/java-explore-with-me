package ru.practicum.controller.admin.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.user.UserService;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@RestController
public class AdminUsersController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto create(@RequestBody @Validated NewUserRequest newUserRequest) {
        log.info("🟫🟫 POST /admin/users");
        return userService.create(newUserRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable(name = "userId") long id) {
        log.info("🟫🟫 DELETE /admin/users/{}", id);
        userService.delete(id);
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(required = false) List<Long> ids,
                             @RequestParam(required = false, defaultValue = "0") int from,
                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("🟫🟫 GET ?ids={}&from={}&size={}", ids, from, size);
        return userService.get(ids, from, size);
    }
}
