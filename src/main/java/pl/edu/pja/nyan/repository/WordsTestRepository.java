package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.WordsTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the WordsTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WordsTestRepository extends JpaRepository<WordsTest, Long>, JpaSpecificationExecutor<WordsTest> {

    @Query("select words_test from WordsTest words_test where words_test.creator.login = ?#{principal.username}")
    List<WordsTest> findByCreatorIsCurrentUser();

    @Query(value = "select distinct words_test from WordsTest words_test left join fetch words_test.words",
        countQuery = "select count(distinct words_test) from WordsTest words_test")
    Page<WordsTest> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct words_test from WordsTest words_test left join fetch words_test.words")
    List<WordsTest> findAllWithEagerRelationships();

    @Query("select words_test from WordsTest words_test left join fetch words_test.words where words_test.id =:id")
    Optional<WordsTest> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<WordsTest> findOneByCode(String code);

}
