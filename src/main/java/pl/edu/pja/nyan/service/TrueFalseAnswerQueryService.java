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

import pl.edu.pja.nyan.domain.TrueFalseAnswer;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.TrueFalseAnswerRepository;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerCriteria;

import pl.edu.pja.nyan.service.dto.TrueFalseAnswerDTO;
import pl.edu.pja.nyan.service.mapper.TrueFalseAnswerMapper;

/**
 * Service for executing complex queries for TrueFalseAnswer entities in the database.
 * The main input is a {@link TrueFalseAnswerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TrueFalseAnswerDTO} or a {@link Page} of {@link TrueFalseAnswerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrueFalseAnswerQueryService extends QueryService<TrueFalseAnswer> {

    private final Logger log = LoggerFactory.getLogger(TrueFalseAnswerQueryService.class);

    private final TrueFalseAnswerRepository trueFalseAnswerRepository;

    private final TrueFalseAnswerMapper trueFalseAnswerMapper;

    public TrueFalseAnswerQueryService(TrueFalseAnswerRepository trueFalseAnswerRepository, TrueFalseAnswerMapper trueFalseAnswerMapper) {
        this.trueFalseAnswerRepository = trueFalseAnswerRepository;
        this.trueFalseAnswerMapper = trueFalseAnswerMapper;
    }

    /**
     * Return a {@link List} of {@link TrueFalseAnswerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TrueFalseAnswerDTO> findByCriteria(TrueFalseAnswerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TrueFalseAnswer> specification = createSpecification(criteria);
        return trueFalseAnswerMapper.toDto(trueFalseAnswerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TrueFalseAnswerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrueFalseAnswerDTO> findByCriteria(TrueFalseAnswerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TrueFalseAnswer> specification = createSpecification(criteria);
        return trueFalseAnswerRepository.findAll(specification, page)
            .map(trueFalseAnswerMapper::toDto);
    }

    /**
     * Function to convert TrueFalseAnswerCriteria to a {@link Specification}
     */
    private Specification<TrueFalseAnswer> createSpecification(TrueFalseAnswerCriteria criteria) {
        Specification<TrueFalseAnswer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TrueFalseAnswer_.id));
            }
            if (criteria.getTranslationFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTranslationFrom(), TrueFalseAnswer_.translationFrom));
            }
            if (criteria.getIsRightAnswer() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRightAnswer(), TrueFalseAnswer_.isRightAnswer));
            }
            if (criteria.getSrcWordId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSrcWordId(), TrueFalseAnswer_.srcWord, Word_.id));
            }
            if (criteria.getTargetWordId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTargetWordId(), TrueFalseAnswer_.targetWord, Word_.id));
            }
            if (criteria.getExamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExamId(), TrueFalseAnswer_.exam, ExamResult_.id));
            }
        }
        return specification;
    }

}
