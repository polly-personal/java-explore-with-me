package ru.practicum.service.user;

import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.entity.user.User;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest newUserRequest);

    void delete(long id);

    User checkAndGetEntityById(long id);

    UserDto getById(long id);

    List<UserDto> get(List<Long> ids, int from, int size);
}
