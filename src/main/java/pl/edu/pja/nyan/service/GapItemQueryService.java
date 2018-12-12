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

import pl.edu.pja.nyan.domain.GapItem;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.GapItemRepository;
import pl.edu.pja.nyan.service.dto.GapItemCriteria;

import pl.edu.pja.nyan.service.dto.GapItemDTO;
import pl.edu.pja.nyan.service.mapper.GapItemMapper;

/**
 * Service for executing complex queries for GapItem entities in the database.
 * The main input is a {@link GapItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GapItemDTO} or a {@link Page} of {@link GapItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GapItemQueryService extends QueryService<GapItem> {

    private final Logger log = LoggerFactory.getLogger(GapItemQueryService.class);

    private final GapItemRepository gapItemRepository;

    private final GapItemMapper gapItemMapper;

    public GapItemQueryService(GapItemRepository gapItemRepository, GapItemMapper gapItemMapper) {
        this.gapItemRepository = gapItemRepository;
        this.gapItemMapper = gapItemMapper;
    }

    /**
     * Return a {@link List} of {@link GapItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GapItemDTO> findByCriteria(GapItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GapItem> specification = createSpecification(criteria);
        return gapItemMapper.toDto(gapItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GapItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GapItemDTO> findByCriteria(GapItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GapItem> specification = createSpecification(criteria);
        return gapItemRepository.findAll(specification, page)
            .map(gapItemMapper::toDto);
    }

    /**
     * Function to convert GapItemCriteria to a {@link Specification}
     */
    private Specification<GapItem> createSpecification(GapItemCriteria criteria) {
        Specification<GapItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GapItem_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), GapItem_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), GapItem_.value));
            }
            if (criteria.getTestItemId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTestItemId(), GapItem_.testItem, FillingGapsTestItem_.id));
            }
        }
        return specification;
    }

}
