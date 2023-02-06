package com.laingard.FrelloManager.mapper;

import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role.name", source ="role")
    public User toEntity(UserDto user);

    @Mapping(target = "role", source ="role.name")
    public UserDto toDto(User user);

    default List<UserDto> toDtoList(List<User> userList){
        if (userList == null)
            return new ArrayList<>();
        return userList.stream().map(this::toDto).collect(Collectors.toList());
    }
}