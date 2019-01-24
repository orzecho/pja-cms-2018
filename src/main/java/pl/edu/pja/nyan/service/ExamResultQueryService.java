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

import pl.edu.pja.nyan.domain.ExamResult;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.ExamResultRepository;
import pl.edu.pja.nyan.service.dto.ExamResultCriteria;

import pl.edu.pja.nyan.service.dto.ExamResultDTO;
import pl.edu.pja.nyan.service.mapper.ExamResultMapper;

/**
 * Service for executing complex queries for ExamResult entities in the database.
 * The main input is a {@link ExamResultCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExamResultDTO} or a {@link Page} of {@link ExamResultDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExamResultQueryService extends QueryService<ExamResult> {

    private final Logger log = LoggerFactory.getLogger(ExamResultQueryService.class);

    private final ExamResultRepository examResultRepository;

    private final ExamResultMapper examResultMapper;

    public ExamResultQueryService(ExamResultRepository examResultRepository, ExamResultMapper examResultMapper) {
        this.examResultRepository = examResultRepository;
        this.examResultMapper = examResultMapper;
    }

    /**
     * Return a {@link List} of {@link ExamResultDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExamResultDTO> findByCriteria(ExamResultCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExamResult> specification = createSpecification(criteria);
        return examResultMapper.toDto(examResultRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExamResultDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamResultDTO> findByCriteria(ExamResultCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExamResult> specification = createSpecification(criteria);
        return examResultRepository.findAll(specification, page)
            .map(examResultMapper::toDto);
    }

    /**
     * Function to convert ExamResultCriteria to a {@link Specification}
     */
    private Specification<ExamResult> createSpecification(ExamResultCriteria criteria) {
        Specification<ExamResult> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ExamResult_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ExamResult_.date));
            }
            if (criteria.getResult() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResult(), ExamResult_.result));
            }
            if (criteria.getWrittenAnswersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWrittenAnswersId(), ExamResult_.writtenAnswers, WrittenAnswer_.id));
            }
            if (criteria.getTrueFalseAnswersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTrueFalseAnswersId(), ExamResult_.trueFalseAnswers, TrueFalseAnswer_.id));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getStudentId(), ExamResult_.student, User_.id));
            }
            if (criteria.getExamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExamId(), ExamResult_.exam, Exam_.id));
            }
        }
        return specification;
    }

}
