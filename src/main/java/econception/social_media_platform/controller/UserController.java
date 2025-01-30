package econception.social_media_platform.controller;

import econception.social_media_platform.domain.model.JwtToken;
import econception.social_media_platform.domain.request.LoginRequestDTO;
import econception.social_media_platform.domain.request.RegisterRequestDTO;
import econception.social_media_platform.domain.response.UserResponseDTO;
import econception.social_media_platform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "APIs related to user management")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO request) {
        log.info("Registering user with email: {}", request.getEmail());
        String response = userService.register(request);
        log.info("User registered successfully: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody @Valid LoginRequestDTO request) {
        log.info("User login attempt with email: {}", request.getEmail());
        JwtToken token = userService.login(request);
        log.info("User logged in successfully: {}", request.getEmail());
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Get user by ID", description = "Fetches user details by user ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        log.info("Fetching user details for ID: {}", id);
        UserResponseDTO user = userService.getUserById(id);
        log.info("User details fetched successfully for ID: {}", id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Follow a user", description = "Allows a user to follow another user")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@PathVariable Integer userId, HttpServletRequest request) {
        log.info("User attempting to follow user ID: {}", userId);
        String response = userService.followUser(userId, request);
        log.info("User followed successfully: {}", userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get followers", description = "Retrieves a list of followers for a user")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserResponseDTO>> getFollowers(@PathVariable Integer userId) {
        log.info("Fetching followers for user ID: {}", userId);
        List<UserResponseDTO> followers = userService.getFollowers(userId);
        log.info("Fetched {} followers for user ID: {}", followers.size(), userId);
        return ResponseEntity.ok(followers);
    }

    @Operation(summary = "Get followings", description = "Retrieves a list of users followed by the user")
    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<UserResponseDTO>> getFollowings(@PathVariable Integer userId) {
        log.info("Fetching followings for user ID: {}", userId);
        List<UserResponseDTO> followings = userService.getFollowings(userId);
        log.info("Fetched {} followings for user ID: {}", followings.size(), userId);
        return ResponseEntity.ok(followings);
    }

    @Operation(summary = "Search users", description = "Searches for users by keyword")
    @PostMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUsersByKeyword(
            @RequestParam String keyword, Pageable pageable) {
        log.info("Searching users with keyword: {}", keyword);
        Page<UserResponseDTO> users = userService.searchPostsByKeyword(keyword, pageable);
        log.info("Found {} users matching keyword: {}", users.getTotalElements(), keyword);
        return ResponseEntity.ok(users);
    }
}
