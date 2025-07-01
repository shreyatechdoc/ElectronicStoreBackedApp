package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

//userdto is used for transfer data into db && entity user is use to save data into db
// for prevention of security leak used dto.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    @Size(min = 3,max = 20,message = "Invalid name!!")
    private String name;
    //@Email(message = "Invalid user email !! ")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid user email !!")
    @NotBlank(message = "Email is requried !! ")
    private String email;
    @NotBlank(message = "password is requried !!")
    private String password;
    @Size(min = 4,max = 6,message = "invalid gender!!")
    private String gender;
    @NotBlank(message = "write something about yourself")
    private String about;
    @ImageNameValid
    private String imageName;



}
