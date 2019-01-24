package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.TrueFalseAnswer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TrueFalseAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrueFalseAnswerRepository extends JpaRepository<TrueFalseAnswer, Long>, JpaSpecificationExecutor<TrueFalseAnswer> {

}
