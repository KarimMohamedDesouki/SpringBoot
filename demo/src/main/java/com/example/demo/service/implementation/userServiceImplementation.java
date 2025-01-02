package com.example.demo.service.implementation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.User;
import java.util.ArrayList;
// import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exceptions.UserServiceException;
import com.example.demo.io.entity.userEntity;
import com.example.demo.repository.userRepository;
import com.example.demo.service.UserService;
import com.example.demo.shared.utils;
import com.example.demo.shared.dto.UserDto;
import com.example.demo.ui.model.response.ErrorMessages;

@Service
public class userServiceImplementation implements UserService {

    @Autowired
    userRepository userRepository;

    @Autowired
    utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDetails) {

        if (userRepository.findByEmail(userDetails.getEmail()) != null)
            throw new RuntimeException("Record already exists");
        userEntity userEntity = new userEntity();
        BeanUtils.copyProperties(userDetails, userEntity);

        // encrypt password
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        // generate random public user id
        String userId = utils.generateUserId(30);
        userEntity.setUserId(userId);

        userEntity storedDetails = userRepository.save(userEntity);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(storedDetails, returnedValue);
        return returnedValue;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        userEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null)
            throw new UsernameNotFoundException(username);

        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email) {
        userEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnedValue);

        return returnedValue;
    }

    @Override
    public UserDto findUserByUserId(String userId) {
        UserDto userDto = new UserDto();
        userEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UsernameNotFoundException(userId);

        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnedValue = new UserDto();
        userEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        userEntity updatedUserDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedUserDetails, returnedValue);

        return returnedValue;
    }

    @Override
    public Boolean deleteUser(String userId) {
        userEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);

        return true;
    }

    // @Override
    // public List<UserDto> getAllUsers(int page, int limit) {
    // List<UserDto> returnedValue = new ArrayList<>();
    // if (page > 0)
    // page -= 1;

    // Pageable pageableRequest = PageRequest.of(page, limit);

    // Page<userEntity> userPage = userRepository.findAll(pageableRequest);
    // List<userEntity> users = userPage.getContent();

    // for (userEntity userEntity : users) {
    // UserDto userDto = new UserDto();
    // BeanUtils.copyProperties(userEntity, userDto);
    // returnedValue.add(userDto);
    // }

    // return returnedValue;
    // }
}
