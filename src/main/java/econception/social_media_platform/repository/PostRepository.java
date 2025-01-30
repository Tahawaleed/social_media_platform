package econception.social_media_platform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import econception.social_media_platform.entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR (LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
