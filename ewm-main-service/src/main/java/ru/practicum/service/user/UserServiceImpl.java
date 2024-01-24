package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.entity.user.User;
import ru.practicum.exception.MainExceptionIdNotFound;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.user.UserRepository;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto create(NewUserRequest newUserRequest) {
        User user = userRepository.save(UserMapper.toUser(newUserRequest));

        log.info("🟩 создан пользователь: " + user);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    public void delete(long id) {
        userRepository.findById(id).orElseThrow(() ->
                new MainExceptionIdNotFound("User with id=" + id + " was not found"));

        userRepository.deleteById(id);

        log.info("⬛️ удален пользователь по id: " + id);
    }

    public User checkAndGetEntityById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new MainExceptionIdNotFound("User with id=" + id +
                " was not found"));
    }

    public UserDto getById(long id) {
        User user = checkAndGetEntityById(id);
        UserDto userDto = UserMapper.toUserDto(user);

        log.info("🟦 выдан пользователь: " + userDto);
        return userDto;
    }

    public List<UserDto> get(List<Long> ids, int from, int size) {
        Page<User> newUserRequests;
        PageRequest pageRequest = PageRequest.of(from, size);

        if (ids == null || ids.isEmpty()) {
            newUserRequests = userRepository.findAll(pageRequest);
        } else {
            newUserRequests = userRepository.findAllByIdIn(ids, pageRequest);
        }
        List<UserDto> userDtos = UserMapper.toUserDtos(newUserRequests.toList());
        log.info("🟦 выдано попадание на сайт/список попаданий на сайты: " + userDtos);

        return userDtos;
    }
}
