package econception.social_media_platform.domain.model;

import econception.social_media_platform.domain.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    private String token;
    private UserResponseDTO user;
}
