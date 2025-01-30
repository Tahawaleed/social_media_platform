package econception.social_media_platform.repository;

import econception.social_media_platform.entity.Post;
import econception.social_media_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import econception.social_media_platform.entity.Like;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findLikeByUserAndPost(User user, Post post);
}
