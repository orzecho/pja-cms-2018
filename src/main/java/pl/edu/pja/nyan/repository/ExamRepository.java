package pl.edu.pja.nyan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.edu.pja.nyan.domain.Exam;

/**
 * Spring Data  repository for the Exam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>, JpaSpecificationExecutor<Exam> {

    @Query("select exam from Exam exam where exam.creator.login = ?#{principal.username}")
    List<Exam> findByCreatorIsCurrentUser();

    @Query(value = "select distinct exam from Exam exam left join fetch exam.words",
        countQuery = "select count(distinct exam) from Exam exam")
    Page<Exam> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct exam from Exam exam left join fetch exam.words")
    List<Exam> findAllWithEagerRelationships();

    @Query("select exam from Exam exam left join fetch exam.words where exam.id =:id")
    Optional<Exam> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select exam from Exam exam left join fetch exam.words where exam.code =:code")
    Optional<Exam> findOneByCodeWithEagerRelationships(@Param("code")String code);

}
