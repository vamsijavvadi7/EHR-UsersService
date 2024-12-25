package com.ehr.users.mapper;


import com.ehr.users.dto.UserDto;
import com.ehr.users.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")  // This tells MapStruct to create a Spring Bean for this Mapper
public interface UserMapper {
    // Method to map User to UserDto
    @Mapping(source = "password",target = "password")
    UserDto toDto(User user);

    // Method to map UserDto to User
    @Mapping(source = "password",target = "password")
    User toEntity(UserDto userDTO);
}
