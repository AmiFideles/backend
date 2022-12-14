package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto findById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(()->new UserNotFoundException("there is no user with this id"));
        return UserMapper.INSTANCE.toDto(user);
    }

    public boolean existById(Long id) {
        return userRepository
                .existsById(id);
    }

    public UserDto findByUsername(String username){
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("there is no user with this username"));
        return UserMapper.INSTANCE.toDto(user);
    }


}
