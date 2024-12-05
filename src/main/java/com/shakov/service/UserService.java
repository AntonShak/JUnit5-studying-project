package com.shakov.service;

import com.shakov.dao.UserDao;
import com.shakov.dto.CreateUserDto;
import com.shakov.dto.UserDto;
import com.shakov.exception.ValidationException;
import com.shakov.mapper.CreateUserMapper;
import com.shakov.mapper.UserMapper;
import com.shakov.validator.CreateUserValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Optional;

/*
 * Чтобы написать Unit тест, нам необходимо управлять механизмом внедрения зависимостей с помощью Mockito mocks/spies.
 * Это невозможно сделать для final полей, которые сразу же инициализируются:
 * Java разрешает инициализировать final поля/переменные/параметры только 1 раз.
 *
 * По вышеописанным причинам произошел рефакторинг UserService
 */
@RequiredArgsConstructor
public class UserService {

    private final CreateUserValidator createUserValidator;
    private final UserDao userDao;
    private final CreateUserMapper createUserMapper;
    private final UserMapper userMapper;

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::map);
    }

    @SneakyThrows
    public UserDto create(CreateUserDto userDto) {
        var validationResult = createUserValidator.validate(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var userEntity = createUserMapper.map(userDto);
        userDao.save(userEntity);

        return userMapper.map(userEntity);
    }
}
