package pl.edu.pja.nyan.repository;

import java.util.List;

import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.domain.LessonFile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LessonFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonFileRepository extends JpaRepository<LessonFile, Long>, JpaSpecificationExecutor<LessonFile> {

    List<LessonFile> findByLesson(Lesson entity);
}
