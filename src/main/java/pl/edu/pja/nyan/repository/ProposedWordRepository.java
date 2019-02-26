package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.ProposedWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ProposedWord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProposedWordRepository extends JpaRepository<ProposedWord, Long>, JpaSpecificationExecutor<ProposedWord> {

    @Query("select proposed_word from ProposedWord proposed_word where proposed_word.addedBy.login = ?#{principal.username}")
    List<ProposedWord> findByAddedByIsCurrentUser();

    @Query(value = "select distinct proposed_word from ProposedWord proposed_word left join fetch proposed_word.tags",
        countQuery = "select count(distinct proposed_word) from ProposedWord proposed_word")
    Page<ProposedWord> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct proposed_word from ProposedWord proposed_word left join fetch proposed_word.tags")
    List<ProposedWord> findAllWithEagerRelationships();

    @Query("select proposed_word from ProposedWord proposed_word left join fetch proposed_word.tags where proposed_word.id =:id")
    Optional<ProposedWord> findOneWithEagerRelationships(@Param("id") Long id);

}
