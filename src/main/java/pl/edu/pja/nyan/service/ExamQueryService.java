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

import pl.edu.pja.nyan.domain.Exam;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.ExamRepository;
import pl.edu.pja.nyan.service.dto.ExamCriteria;

import pl.edu.pja.nyan.service.dto.ExamDTO;
import pl.edu.pja.nyan.service.mapper.ExamMapper;

/**
 * Service for executing complex queries for Exam entities in the database.
 * The main input is a {@link ExamCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExamDTO} or a {@link Page} of {@link ExamDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExamQueryService extends QueryService<Exam> {

    private final Logger log = LoggerFactory.getLogger(ExamQueryService.class);

    private final ExamRepository examRepository;

    private final ExamMapper examMapper;

    public ExamQueryService(ExamRepository examRepository, ExamMapper examMapper) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
    }

    /**
     * Return a {@link List} of {@link ExamDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExamDTO> findByCriteria(ExamCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Exam> specification = createSpecification(criteria);
        return examMapper.toDto(examRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExamDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamDTO> findByCriteria(ExamCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Exam> specification = createSpecification(criteria);
        return examRepository.findAll(specification, page)
            .map(examMapper::toDto);
    }

    /**
     * Function to convert ExamCriteria to a {@link Specification}
     */
    private Specification<Exam> createSpecification(ExamCriteria criteria) {
        Specification<Exam> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Exam_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Exam_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Exam_.type));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Exam_.code));
            }
            if (criteria.getResultsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getResultsId(), Exam_.results, ExamResult_.id));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatorId(), Exam_.creator, User_.id));
            }
            if (criteria.getWordId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWordId(), Exam_.words, Word_.id));
            }
        }
        return specification;
    }

}
