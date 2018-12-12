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

import pl.edu.pja.nyan.domain.FillingGapsTestItem;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.FillingGapsTestItemRepository;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemCriteria;

import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;
import pl.edu.pja.nyan.service.mapper.FillingGapsTestItemMapper;

/**
 * Service for executing complex queries for FillingGapsTestItem entities in the database.
 * The main input is a {@link FillingGapsTestItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FillingGapsTestItemDTO} or a {@link Page} of {@link FillingGapsTestItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FillingGapsTestItemQueryService extends QueryService<FillingGapsTestItem> {

    private final Logger log = LoggerFactory.getLogger(FillingGapsTestItemQueryService.class);

    private final FillingGapsTestItemRepository fillingGapsTestItemRepository;

    private final FillingGapsTestItemMapper fillingGapsTestItemMapper;

    public FillingGapsTestItemQueryService(FillingGapsTestItemRepository fillingGapsTestItemRepository, FillingGapsTestItemMapper fillingGapsTestItemMapper) {
        this.fillingGapsTestItemRepository = fillingGapsTestItemRepository;
        this.fillingGapsTestItemMapper = fillingGapsTestItemMapper;
    }

    /**
     * Return a {@link List} of {@link FillingGapsTestItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FillingGapsTestItemDTO> findByCriteria(FillingGapsTestItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FillingGapsTestItem> specification = createSpecification(criteria);
        return fillingGapsTestItemMapper.toDto(fillingGapsTestItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FillingGapsTestItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FillingGapsTestItemDTO> findByCriteria(FillingGapsTestItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FillingGapsTestItem> specification = createSpecification(criteria);
        return fillingGapsTestItemRepository.findAll(specification, page)
            .map(fillingGapsTestItemMapper::toDto);
    }

    /**
     * Function to convert FillingGapsTestItemCriteria to a {@link Specification}
     */
    private Specification<FillingGapsTestItem> createSpecification(FillingGapsTestItemCriteria criteria) {
        Specification<FillingGapsTestItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FillingGapsTestItem_.id));
            }
            if (criteria.getQuestion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuestion(), FillingGapsTestItem_.question));
            }
            if (criteria.getGapItemId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGapItemId(), FillingGapsTestItem_.gapItems, GapItem_.id));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTagId(), FillingGapsTestItem_.tags, Tag_.id));
            }
        }
        return specification;
    }

}
