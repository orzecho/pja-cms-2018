package pl.edu.pja.nyan.repository;

import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Word entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WordRepository extends JpaRepository<Word, Long>, JpaSpecificationExecutor<Word> {

    @Query(value = "select distinct word from Word word left join fetch word.tags",
        countQuery = "select count(distinct word) from Word word")
    Page<Word> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct word from Word word left join fetch word.tags")
    List<Word> findAllWithEagerRelationships();

    @Query("select word from Word word left join fetch word.tags where word.id =:id")
    Optional<Word> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Word> findByTranslationAndKanaAndKanji(String translation, String kana, String kanji);

    List<Word> findByTagsContaining(Tag tag);
}
