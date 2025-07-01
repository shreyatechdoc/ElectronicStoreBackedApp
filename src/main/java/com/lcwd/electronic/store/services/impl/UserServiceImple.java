package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImple implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Override
    public UserDto createUser(UserDto userdto)
    {
        // generate userid manually
        String userId=UUID.randomUUID().toString();
        userdto.setUserId(userId);
        //dto-->entity
        User user=dtoToEntity(userdto);
        User savedUser=userRepository.save(user);
        // entity -->dto
        UserDto userDto=entityToDto(savedUser);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId)
    {
        User user=userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        user.setUserId(userDto.getUserId());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        User updateduser=userRepository.save(user);
        UserDto updatedDto=entityToDto(updateduser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId)  {
    User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found"));
    String fullpath=imagePath+user.getImageName();
    try
    {
        Path path= Paths.get(fullpath);
        Files.delete(path);
    }
    catch (NoSuchFileException ex)
    {
        ex.printStackTrace();
    }
    catch (IOException ix)
    {
        ix.printStackTrace();
    }
    userRepository.delete(user);
    }

    // Using Pagination Concept && getting all UserDetails
    @Override
    public PageableResponse<UserDto> getallUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // sorting implementation
        //Sort sort = Sort.by(sortBy);
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending()) ;
        // page Number default starts from 0
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
    Page<User> page = userRepository.findAll(pageable);
    PageableResponse<UserDto> response=Helper.getPageableResponse(page,UserDto.class);
    return response ;
    }

    // get Single User detail
    @Override
    public UserDto getUser(String userId)
    {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found"));
        return entityToDto(user);

    }

    @Override
    public UserDto getUserByEmail(String email)
    {
        User user=userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("user not found by given email"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword)
    {
        List<User> users=userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList=users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    private UserDto entityToDto(User savedUser) {
//    UserDto userDto=UserDto.builder()
//            .userId(savedUser.getUserId())
//            .name(savedUser.getName())
//            .email(savedUser.getEmail())
//            .password(savedUser.getPassword())
//            .gender(savedUser.getGender())
//            .about(savedUser.getAbout())
//            .imageName(savedUser.getImageName()).build();
        // we are using Modelmapper class/Library
        // for automatically convertion/mapping of entitytodto & dtotoentity
        // ModelMapper is specially used for mapping.

            return mapper.map(savedUser,UserDto.class);

    }

    private User dtoToEntity(UserDto userdto) {
//           User user= User.builder()
//               .userId(userdto.getUserId())
//               .name(userdto.getName())
//               .email(userdto.getEmail())
//               .password(userdto.getPassword())
//               .gender(userdto.getGender())
//               .about(userdto.getAbout())
//               .imageName(userdto.getImageName())
//               .build();
                return mapper.map(userdto,User.class);
    }




}
