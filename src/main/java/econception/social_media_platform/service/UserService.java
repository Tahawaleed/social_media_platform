package econception.social_media_platform.service;

import econception.social_media_platform.domain.model.JwtToken;
import econception.social_media_platform.domain.request.LoginRequestDTO;
import econception.social_media_platform.domain.request.RegisterRequestDTO;
import econception.social_media_platform.domain.response.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    String register(RegisterRequestDTO request);

    JwtToken login(LoginRequestDTO request);

    UserResponseDTO getUserById(Integer userId);

    String followUser(Integer userId, HttpServletRequest request);

    List<UserResponseDTO> getFollowers(Integer userId);

    List<UserResponseDTO> getFollowings(Integer userId);

    Page<UserResponseDTO> searchPostsByKeyword(String keyword, Pageable pageable);
}
