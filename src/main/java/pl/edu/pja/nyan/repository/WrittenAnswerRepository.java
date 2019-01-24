package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.WrittenAnswer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the WrittenAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WrittenAnswerRepository extends JpaRepository<WrittenAnswer, Long>, JpaSpecificationExecutor<WrittenAnswer> {

}
