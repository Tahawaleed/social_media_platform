package econception.social_media_platform.repository;

import econception.social_media_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import econception.social_media_platform.entity.Follow;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findFollowByFollowerAndFollowing(User follower, User following);
}
