package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.entity.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class UserMapper {
    public User toUser(NewUserRequest newUserRequest) {
        User user = User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();

        log.info("🔀\nDTO: " + newUserRequest + " сконвертирован в \nJPA-сущность: " + user);
        return user;
    }

    public UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();

        log.info("🔀 \nJPA-сущность: " + user + " сконвертирована в \nDTO: " + userShortDto);
        return userShortDto;
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();

        log.info("🔀 \nJPA-сущность: " + user + " сконвертирована в \nDTO: " + userDto);
        return userDto;
    }

    public List<UserDto> toUserDtos(List<User> users) {
        List<UserDto> userDtos = users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());

        log.info("🔀 \nсписок JPA-сущностей: " + users + " сконвертирован в \nсписок DTO: " + userDtos);
        return userDtos;
    }
}
