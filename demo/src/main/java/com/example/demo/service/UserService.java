package com.example.demo.service;

// import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDetails);

    UserDto getUser(String email);

    UserDto findUserByUserId(String userId);

    UserDto updateUser(String userId, UserDto userDto);

    Boolean deleteUser(String userId);

    // List<UserDto> getAllUsers(int page, int limit);

}
