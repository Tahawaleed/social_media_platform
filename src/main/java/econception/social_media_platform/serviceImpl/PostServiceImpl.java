package econception.social_media_platform.serviceImpl;

import econception.social_media_platform.domain.mapper.PostMapper;
import econception.social_media_platform.domain.request.PostRequestDTO;
import econception.social_media_platform.domain.response.PostResponseDTO;
import econception.social_media_platform.entity.Comment;
import econception.social_media_platform.entity.Like;
import econception.social_media_platform.entity.Post;
import econception.social_media_platform.entity.User;
import econception.social_media_platform.repository.CommentRepository;
import econception.social_media_platform.repository.LikeRepository;
import econception.social_media_platform.repository.PostRepository;
import econception.social_media_platform.repository.UserRepository;
import econception.social_media_platform.service.PostService;
import econception.social_media_platform.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PostMapper postMapper;

    @Override
    @CacheEvict(value = {"posts"}, allEntries = true)
    public PostResponseDTO createPost(PostRequestDTO requestDTO, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post();
        post.setUser(user);
        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());
        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    @Cacheable(value = "posts", key = "#pageable.pageNumber")
    public Page<PostResponseDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(postMapper::toDto);
    }

    @Override
    @Cacheable(value = "post", key = "#id")
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return postMapper.toDto(post);
    }

    @Override
    @CachePut(value = "post", key = "#id")
    @CacheEvict(value = "posts", allEntries = true)
    public PostResponseDTO updatePost(Long id, PostRequestDTO requestDTO, HttpServletRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        String email = jwtUtil.getEmailFromRequest(request);
        if (!post.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("You can only edit your own posts");
        }
        if (requestDTO.getTitle() != null) {
            post.setTitle(requestDTO.getTitle());
        }
        if (requestDTO.getContent() != null) {
            post.setContent(requestDTO.getContent());
        }
        postRepository.save(post);

        return postMapper.toDto(post);
    }

    @Override
    @CacheEvict(value = {"post", "posts"}, key = "#id", allEntries = true)
    public void deletePost(Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        String email = jwtUtil.getEmailFromRequest(request);

        if (!post.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    @Override
    @CacheEvict(value = "post", key = "#postId")
    public PostResponseDTO addComment(Long postId, HttpServletRequest request, String commentStr){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        String email = jwtUtil.getEmailFromRequest(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentStr);
        commentRepository.save(comment);
        return postMapper.toDto(post);
    }

    @Override
    @CacheEvict(value = "post", key = "#postId")
    public PostResponseDTO likePost(Long postId, HttpServletRequest request){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        String email = jwtUtil.getEmailFromRequest(request);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Like alreadyLike = likeRepository.findLikeByUserAndPost(user, post);
        if (alreadyLike != null){
            throw new IllegalArgumentException("Post is Already Liked");
        }
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
        return postMapper.toDto(post);
    }

    @Override
    @Cacheable(value = "posts", key = "#keyword")
    public Page<PostResponseDTO> searchPostsByKeyword(String keyword, Pageable pageable) {
        return postRepository.searchByKeyword(keyword, pageable)
                .map(postMapper::toDto);
    }
}
