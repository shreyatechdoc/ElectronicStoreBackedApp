package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/usersdetails")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create user
    @PostMapping
    public ResponseEntity<UserDto>createUser(@Valid @RequestBody UserDto userDto)
    {
        UserDto userDto1=userService.createUser(userDto);
        return new ResponseEntity<>(userDto1,HttpStatus.CREATED);
    }

    //update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto>updateuser(@PathVariable("userId") String userId,@Valid @RequestBody  UserDto userDto)
    {
    UserDto updatedUserDto=userService.updateUser(userDto,userId);
    return new ResponseEntity<>(updatedUserDto,HttpStatus.OK);
    }

    //delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage>deleteUser(@PathVariable String userId) throws IOException {
    userService.deleteUser(userId);
    ApiResponseMessage message=ApiResponseMessage.builder().
            message("user deleted Successfully!!").
            success(true).
            status(HttpStatus.OK).
            build();
    return new ResponseEntity<>(message,HttpStatus.OK);
    }

    // get all user
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam (value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam (value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam (value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam (value = "sortDir",defaultValue = "ASC",required = false) String sortDir
    )
    {
        PageableResponse<UserDto> userDtoPageableResponse=userService.getallUser(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(userDtoPageableResponse,HttpStatus.OK);
    }

    //get single user
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto>getUser(@PathVariable String userId)
    {
        return  new ResponseEntity<>(userService.getUser(userId),HttpStatus.OK);
    }

    // get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto>getUserByEmail(@PathVariable String email)
    {
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    // get user by search
    @GetMapping("/search/{keywards}")
    public ResponseEntity<List<UserDto>>searchUser(@PathVariable String keywards)
    {
     return new ResponseEntity<>(userService.searchUser(keywards),HttpStatus.OK);
      }

      // uploading image
    @PostMapping("/image/{userId}")
      public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile image,@PathVariable String userId) throws IOException {
        String imageName=fileService.uploadFile(image,imageUploadPath);
        UserDto user=userService.getUser(userId);
        user.setImageName(imageName);
        UserDto userDto=userService.updateUser(user,userId);
        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
      }

      // serve image
    @GetMapping(value = "/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
    UserDto user=userService.getUser(userId);
    InputStream resource=fileService.getResource( imageUploadPath,user.getImageName());
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    
}
