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

import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.LessonRepository;
import pl.edu.pja.nyan.service.dto.LessonCriteria;

import pl.edu.pja.nyan.service.dto.LessonDTO;
import pl.edu.pja.nyan.service.mapper.LessonMapper;

/**
 * Service for executing complex queries for Lesson entities in the database.
 * The main input is a {@link LessonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LessonDTO} or a {@link Page} of {@link LessonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LessonQueryService extends QueryService<Lesson> {

    private final Logger log = LoggerFactory.getLogger(LessonQueryService.class);

    private final LessonRepository lessonRepository;

    private final LessonMapper lessonMapper;

    public LessonQueryService(LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    /**
     * Return a {@link List} of {@link LessonDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LessonDTO> findByCriteria(LessonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lesson> specification = createSpecification(criteria);
        return lessonMapper.toDto(lessonRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LessonDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LessonDTO> findByCriteria(LessonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lesson> specification = createSpecification(criteria);
        return lessonRepository.findAll(specification, page)
            .map(lessonMapper::toDto);
    }

    /**
     * Function to convert LessonCriteria to a {@link Specification}
     */
    private Specification<Lesson> createSpecification(LessonCriteria criteria) {
        Specification<Lesson> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Lesson_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Lesson_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Lesson_.description));
            }
            if (criteria.getLessonFileId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLessonFileId(), Lesson_.lessonFiles, LessonFile_.id));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTagId(), Lesson_.tags, Tag_.id));
            }
        }
        return specification;
    }

}
