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

import pl.edu.pja.nyan.domain.LessonFile;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.LessonFileRepository;
import pl.edu.pja.nyan.service.dto.LessonFileCriteria;

import pl.edu.pja.nyan.service.dto.LessonFileDTO;
import pl.edu.pja.nyan.service.mapper.LessonFileMapper;

/**
 * Service for executing complex queries for LessonFile entities in the database.
 * The main input is a {@link LessonFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LessonFileDTO} or a {@link Page} of {@link LessonFileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LessonFileQueryService extends QueryService<LessonFile> {

    private final Logger log = LoggerFactory.getLogger(LessonFileQueryService.class);

    private final LessonFileRepository lessonFileRepository;

    private final LessonFileMapper lessonFileMapper;

    public LessonFileQueryService(LessonFileRepository lessonFileRepository, LessonFileMapper lessonFileMapper) {
        this.lessonFileRepository = lessonFileRepository;
        this.lessonFileMapper = lessonFileMapper;
    }

    /**
     * Return a {@link List} of {@link LessonFileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LessonFileDTO> findByCriteria(LessonFileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LessonFile> specification = createSpecification(criteria);
        return lessonFileMapper.toDto(lessonFileRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LessonFileDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LessonFileDTO> findByCriteria(LessonFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LessonFile> specification = createSpecification(criteria);
        return lessonFileRepository.findAll(specification, page)
            .map(lessonFileMapper::toDto);
    }

    /**
     * Function to convert LessonFileCriteria to a {@link Specification}
     */
    private Specification<LessonFile> createSpecification(LessonFileCriteria criteria) {
        Specification<LessonFile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LessonFile_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), LessonFile_.name));
            }
            if (criteria.getLessonId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLessonId(), LessonFile_.lesson, Lesson_.id));
            }
        }
        return specification;
    }

}
