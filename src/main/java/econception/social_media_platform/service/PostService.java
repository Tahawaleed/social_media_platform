package econception.social_media_platform.service;

import econception.social_media_platform.domain.request.PostRequestDTO;
import econception.social_media_platform.domain.response.PostResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    PostResponseDTO createPost(PostRequestDTO requestDTO, HttpServletRequest request);

    Page<PostResponseDTO> getAllPosts(Pageable pageable);

    PostResponseDTO getPostById(Long id);

    PostResponseDTO updatePost(Long id, PostRequestDTO requestDTO, HttpServletRequest request);

    void deletePost(Long id, HttpServletRequest request);

    PostResponseDTO addComment(Long postId, HttpServletRequest request, String commentStr);

    PostResponseDTO likePost(Long postId, HttpServletRequest request);

    Page<PostResponseDTO> searchPostsByKeyword(String keyword, Pageable pageable);
    // Add service methods here
}
