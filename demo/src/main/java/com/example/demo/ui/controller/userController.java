package com.example.demo.ui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exceptions.UserServiceException;
import com.example.demo.repository.userRepository;
import com.example.demo.service.UserService;
import com.example.demo.shared.dto.UserDto;
import com.example.demo.ui.model.request.UserRequest;
import com.example.demo.ui.model.response.ErrorMessages;
import com.example.demo.ui.model.response.userResponse;

import java.text.MessageFormat;
// import java.util.ArrayList;
// import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
public class userController {

    @Autowired
    UserService userService;
    @Autowired
    userRepository userRepository;

    @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public userResponse getSpecificUserByUserId(@PathVariable String id) {

        userResponse returnedUser = new userResponse();
        UserDto userDto = userService.findUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        returnedUser = modelMapper.map(userDto, userResponse.class);

        return returnedUser;
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public userResponse createUser(@RequestBody UserRequest userDetails) throws Exception {

        userResponse returnedUser = new userResponse();

        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        returnedUser = modelMapper.map(createdUser, userResponse.class);

        return returnedUser;

    }

    @PutMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public userResponse updateUserByUserId(@PathVariable String id, @RequestBody UserRequest userDetails) {

        userResponse returnedUser = new userResponse();

        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto updatedUser = userService.updateUser(id, userDto);
        returnedUser = modelMapper.map(updatedUser, userResponse.class);

        return returnedUser;
    }

    @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public String deleteUserByUserId(@PathVariable String id) {

        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return MessageFormat.format("User with id {0} was deleted successfully", id);
        } else {
            throw new UserServiceException("Failed to delete user with id '" + id + "'");
        }

    }

    // @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE,
    // MediaType.APPLICATION_XML_VALUE })
    // public List<userResponse> getAllUser(@RequestParam(value = "page",
    // defaultValue = "1") int page,
    // @RequestParam(value = "limit", defaultValue = "2") int limit) {
    // List<userResponse> returnedUser = new ArrayList<>();

    // List<UserDto> users = userService.getAllUsers(page, limit);
    // for (UserDto userDto : users) {
    // userResponse user = new userResponse();
    // BeanUtils.copyProperties(userDto, user);
    // returnedUser.add(user);
    // }

    // return returnedUser;
    // }
}
