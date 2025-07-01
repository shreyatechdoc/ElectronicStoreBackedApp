package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;

import java.io.IOException;
import java.util.List;

// for loose coupling use interface
public interface UserService
{
        UserDto createUser(UserDto userdto);
         UserDto updateUser(UserDto userDto,String userId);
         void deleteUser(String userId) throws IOException;
         PageableResponse<UserDto>getallUser(int pageNumber, int pageSize, String sortBy, String sortDir);
         UserDto getUser(String userId);
         UserDto getUserByEmail(String email);
         // search user
        List<UserDto>searchUser(String keyword);
}
