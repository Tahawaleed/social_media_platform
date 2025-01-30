package econception.social_media_platform.repository;

import econception.social_media_platform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import econception.social_media_platform.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "(LOWER(u.bio) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            ")")
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
