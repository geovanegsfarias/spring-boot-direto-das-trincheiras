package academy.devdojo.mapper;

import academy.devdojo.annotation.EncodedMapping;
import academy.devdojo.domain.User;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = PasswordEncoderMapper.class)
public interface UserMapper {
    @Mapping(target = "roles", constant = "USER")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPostRequest request);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toUser(UserPutRequest request);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> user);

    UserPostResponse toUserPostResponse(User user);
}

// mapstruct às vezes é lento para atualizar alterações
// usar mvn clean compile no projeto para recompilar