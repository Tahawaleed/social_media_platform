package econception.social_media_platform.domain.mapper;

import econception.social_media_platform.domain.response.CommentResponseDTO;
import econception.social_media_platform.domain.response.PostResponseDTO;
import econception.social_media_platform.entity.Comment;
import econception.social_media_platform.entity.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {
    public PostResponseDTO toDto(Post entity){
        return PostResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUser().getUsername())
                .title(entity.getTitle())
                .content(entity.getContent())
                .timestamp(entity.getTimestamp())
                .comments(entity.getComments().stream().map(this::toDto).toList())
                .likes(entity.getLikes().stream().map(item -> item.getUser().getEmail()).toList())
                .build();
    }

    public List<PostResponseDTO> toDto(List<Post> posts){
        return posts.stream().map(this::toDto).toList();
    }

    public CommentResponseDTO toDto(Comment comment){
        return CommentResponseDTO.builder()
                .email(comment.getUser().getEmail())
                .comment(comment.getContent())
                .build();
    }
}
