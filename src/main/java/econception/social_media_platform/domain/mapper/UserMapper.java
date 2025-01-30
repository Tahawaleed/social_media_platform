package econception.social_media_platform.domain.mapper;

import econception.social_media_platform.domain.response.UserResponseDTO;
import econception.social_media_platform.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponseDTO toDto(User entity){
        return UserResponseDTO.builder()
                .userId(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .profilePicture(entity.getProfilePicture())
                .bio(entity.getBio())
                .build();
    }

    public List<UserResponseDTO> toDto(List<User> entities){
        return  entities.stream().map(this::toDto).toList();
    }
}
