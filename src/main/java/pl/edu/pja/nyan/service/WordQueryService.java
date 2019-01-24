package pl.edu.pja.nyan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.WordRepository;
import pl.edu.pja.nyan.service.dto.WordCriteria;

import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.mapper.WordMapper;

/**
 * Service for executing complex queries for Word entities in the database.
 * The main input is a {@link WordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WordDTO} or a {@link Page} of {@link WordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WordQueryService extends QueryService<Word> {

    private final Logger log = LoggerFactory.getLogger(WordQueryService.class);

    private final WordRepository wordRepository;

    private final WordMapper wordMapper;

    public WordQueryService(WordRepository wordRepository, WordMapper wordMapper) {
        this.wordRepository = wordRepository;
        this.wordMapper = wordMapper;
    }

    /**
     * Return a {@link List} of {@link WordDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WordDTO> findByCriteria(WordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Word> specification = createSpecification(criteria);
        return wordMapper.toDto(wordRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WordDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WordDTO> findByCriteria(WordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Word> specification = createSpecification(criteria);
        return wordRepository.findAll(specification, page)
            .map(wordMapper::toDto);
    }

    /**
     * Function to convert WordCriteria to a {@link Specification}
     */
    private Specification<Word> createSpecification(WordCriteria criteria) {
        Specification<Word> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Word_.id));
            }
            if (criteria.getTranslation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTranslation(), Word_.translation));
            }
            if (criteria.getKana() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKana(), Word_.kana));
            }
            if (criteria.getKanji() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKanji(), Word_.kanji));
            }
            if (criteria.getRomaji() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRomaji(), Word_.romaji));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Word_.note));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTagId(), Word_.tags, Tag_.id));
            }
            if (criteria.getExamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExamId(), Word_.exams, Exam_.id));
            }
        }
        return specification;
    }

}
