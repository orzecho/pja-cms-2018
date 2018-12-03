package pl.edu.pja.nyan.repository;

import java.util.List;

import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.domain.Tag;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Lesson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    List<Lesson> findByTagsContaining(Tag tag);
}
