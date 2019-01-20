package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.ExamResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ExamResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long>, JpaSpecificationExecutor<ExamResult> {

    @Query("select exam_result from ExamResult exam_result where exam_result.student.login = ?#{principal.username}")
    List<ExamResult> findByStudentIsCurrentUser();

}
