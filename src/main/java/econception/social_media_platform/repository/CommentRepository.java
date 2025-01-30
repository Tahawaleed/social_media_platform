package econception.social_media_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import econception.social_media_platform.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
