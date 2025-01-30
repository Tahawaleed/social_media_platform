package econception.social_media_platform.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime timestamp;
    List<CommentResponseDTO> comments;
    List<String> likes;
}
