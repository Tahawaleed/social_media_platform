package econception.social_media_platform.serviceImpl;

import econception.social_media_platform.domain.mapper.UserMapper;
import econception.social_media_platform.domain.model.JwtToken;
import econception.social_media_platform.domain.request.LoginRequestDTO;
import econception.social_media_platform.domain.request.RegisterRequestDTO;
import econception.social_media_platform.domain.response.UserResponseDTO;
import econception.social_media_platform.entity.Follow;
import econception.social_media_platform.entity.User;
import econception.social_media_platform.repository.FollowRepository;
import econception.social_media_platform.repository.UserRepository;
import econception.social_media_platform.service.UserService;
import econception.social_media_platform.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public String register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProfilePicture(request.getProfilePicture());
        user.setBio(request.getBio());

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public JwtToken login(LoginRequestDTO request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            String token = jwtUtil.generateToken(user.get().getEmail());
            return JwtToken.builder()
                    .token(token)
                    .user(userMapper.toDto(user.get()))
                    .build();
        }
        throw new IllegalArgumentException("Invalid credentials");
    }

    @Override
    @Cacheable(value = "user", key = "#userId")
    public UserResponseDTO getUserById(Integer userId) {
        log.info("Fetching user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        return userMapper.toDto(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "followings", key = "#userId"),
            @CacheEvict(value = "followers", allEntries = true)
    })
    public String followUser(Integer userId, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        User followerUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Follower User not found"));
        User followedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Following User not found"));

        Follow alreadyFollowing = followRepository.findFollowByFollowerAndFollowing(followerUser, followedUser);
        if (alreadyFollowing != null) {
            return "User is already followed";
        }
        Follow follow = new Follow();
        follow.setFollower(followerUser);
        follow.setFollowing(followedUser);
        followRepository.save(follow);
        return "User followed successfully";
    }

    @Override
    @Cacheable(value = "followers", key = "#userId")
    public List<UserResponseDTO> getFollowers(Integer userId) {
        log.info("Fetching followers for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Follow> follows = user.getFollowers();
        return userMapper.toDto(follows.stream().map(Follow::getFollower).toList());
    }

    @Override
    @Cacheable(value = "followings", key = "#userId")
    public List<UserResponseDTO> getFollowings(Integer userId) {
        log.info("Fetching followings for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Follow> followings = user.getFollowing();
        return userMapper.toDto(followings.stream().map(Follow::getFollowing).toList());
    }

    @Override
    @Cacheable(value = "users", key = "#keyword")
    public Page<UserResponseDTO> searchPostsByKeyword(String keyword, Pageable pageable) {
        return userRepository.searchByKeyword(keyword, pageable)
                .map(userMapper::toDto);
    }
}