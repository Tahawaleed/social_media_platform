package econception.social_media_platform.controller;

import econception.social_media_platform.domain.request.PostRequestDTO;
import econception.social_media_platform.domain.response.PostResponseDTO;
import econception.social_media_platform.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@Tag(name = "Posts Controller", description = "Posts related APIs")
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "Add Post", description = "This API adds a new post.")
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostRequestDTO requestDto, HttpServletRequest request) {
        log.info("Creating new post: {}", requestDto);
        PostResponseDTO response = postService.createPost(requestDto, request);
        log.info("Post created successfully: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all posts", description = "Retrieve a paginated list of all posts.")
    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(Pageable pageable) {
        log.info("Fetching all posts with pagination: {}", pageable);
        Page<PostResponseDTO> response = postService.getAllPosts(pageable);
        log.info("Fetched {} posts", response.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Post by ID", description = "Retrieve a specific post by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        log.info("Fetching post with ID: {}", id);
        PostResponseDTO response = postService.getPostById(id);
        log.info("Post fetched successfully: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update Post", description = "This API updates an existing post.")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @RequestBody @Valid PostRequestDTO requestDTO, HttpServletRequest request) {
        log.info("Updating post with ID: {}", id);
        PostResponseDTO response = postService.updatePost(id, requestDTO, request);
        log.info("Post updated successfully: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete Post", description = "This API deletes a post.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpServletRequest request) {
        log.info("Deleting post with ID: {}", id);
        postService.deletePost(id, request);
        log.info("Post deleted successfully: {}", id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @Operation(summary = "Add Comment", description = "This API adds a comment to a post.")
    @PostMapping("/{postId}/comments")
    public ResponseEntity<PostResponseDTO> addComment(@PathVariable Long postId, @RequestParam String commentStr, HttpServletRequest request) {
        log.info("Adding comment to post ID: {}, Comment: {}", postId, commentStr);
        PostResponseDTO response = postService.addComment(postId, request, commentStr);
        log.info("Comment added successfully to post ID: {}", postId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Like Post", description = "This API likes a post.")
    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponseDTO> likePost(@PathVariable Long postId, HttpServletRequest request) {
        log.info("Liking post ID: {}", postId);
        PostResponseDTO response = postService.likePost(postId, request);
        log.info("Post liked successfully: {}", postId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search Posts by Keyword", description = "This API searches posts by keyword.")
    @PostMapping("/search")
    public ResponseEntity<Page<PostResponseDTO>> searchPosts(@RequestParam String keyword, Pageable pageable) {
        log.info("Searching posts with keyword: {}", keyword);
        Page<PostResponseDTO> response = postService.searchPostsByKeyword(keyword, pageable);
        log.info("Found {} posts for keyword: {}", response.getTotalElements(), keyword);
        return ResponseEntity.ok(response);
    }
}
