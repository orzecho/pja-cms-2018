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

import pl.edu.pja.nyan.domain.WordsTest;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.WordsTestRepository;
import pl.edu.pja.nyan.service.dto.WordsTestCriteria;

import pl.edu.pja.nyan.service.dto.WordsTestDTO;
import pl.edu.pja.nyan.service.mapper.WordsTestMapper;

/**
 * Service for executing complex queries for WordsTest entities in the database.
 * The main input is a {@link WordsTestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WordsTestDTO} or a {@link Page} of {@link WordsTestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WordsTestQueryService extends QueryService<WordsTest> {

    private final Logger log = LoggerFactory.getLogger(WordsTestQueryService.class);

    private final WordsTestRepository wordsTestRepository;

    private final WordsTestMapper wordsTestMapper;

    public WordsTestQueryService(WordsTestRepository wordsTestRepository, WordsTestMapper wordsTestMapper) {
        this.wordsTestRepository = wordsTestRepository;
        this.wordsTestMapper = wordsTestMapper;
    }

    /**
     * Return a {@link List} of {@link WordsTestDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WordsTestDTO> findByCriteria(WordsTestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WordsTest> specification = createSpecification(criteria);
        return wordsTestMapper.toDto(wordsTestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WordsTestDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WordsTestDTO> findByCriteria(WordsTestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WordsTest> specification = createSpecification(criteria);
        return wordsTestRepository.findAll(specification, page)
            .map(wordsTestMapper::toDto);
    }

    /**
     * Function to convert WordsTestCriteria to a {@link Specification}
     */
    private Specification<WordsTest> createSpecification(WordsTestCriteria criteria) {
        Specification<WordsTest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WordsTest_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), WordsTest_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), WordsTest_.type));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), WordsTest_.code));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatorId(), WordsTest_.creator, User_.id));
            }
            if (criteria.getWordId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWordId(), WordsTest_.words, Word_.id));
            }
        }
        return specification;
    }

}
