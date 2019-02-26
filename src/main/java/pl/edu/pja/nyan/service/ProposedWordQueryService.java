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

import pl.edu.pja.nyan.domain.ProposedWord;
import pl.edu.pja.nyan.domain.*; // for static metamodels
import pl.edu.pja.nyan.repository.ProposedWordRepository;
import pl.edu.pja.nyan.service.dto.ProposedWordCriteria;

import pl.edu.pja.nyan.service.dto.ProposedWordDTO;
import pl.edu.pja.nyan.service.mapper.ProposedWordMapper;

/**
 * Service for executing complex queries for ProposedWord entities in the database.
 * The main input is a {@link ProposedWordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProposedWordDTO} or a {@link Page} of {@link ProposedWordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProposedWordQueryService extends QueryService<ProposedWord> {

    private final Logger log = LoggerFactory.getLogger(ProposedWordQueryService.class);

    private final ProposedWordRepository proposedWordRepository;

    private final ProposedWordMapper proposedWordMapper;

    public ProposedWordQueryService(ProposedWordRepository proposedWordRepository, ProposedWordMapper proposedWordMapper) {
        this.proposedWordRepository = proposedWordRepository;
        this.proposedWordMapper = proposedWordMapper;
    }

    /**
     * Return a {@link List} of {@link ProposedWordDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProposedWordDTO> findByCriteria(ProposedWordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProposedWord> specification = createSpecification(criteria);
        return proposedWordMapper.toDto(proposedWordRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProposedWordDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProposedWordDTO> findByCriteria(ProposedWordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProposedWord> specification = createSpecification(criteria);
        return proposedWordRepository.findAll(specification, page)
            .map(proposedWordMapper::toDto);
    }

    /**
     * Function to convert ProposedWordCriteria to a {@link Specification}
     */
    private Specification<ProposedWord> createSpecification(ProposedWordCriteria criteria) {
        Specification<ProposedWord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProposedWord_.id));
            }
            if (criteria.getTranslation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTranslation(), ProposedWord_.translation));
            }
            if (criteria.getKana() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKana(), ProposedWord_.kana));
            }
            if (criteria.getKanji() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKanji(), ProposedWord_.kanji));
            }
            if (criteria.getRomaji() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRomaji(), ProposedWord_.romaji));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), ProposedWord_.note));
            }
            if (criteria.getTagId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTagId(), ProposedWord_.tags, Tag_.id));
            }
            if (criteria.getAddedById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAddedById(), ProposedWord_.addedBy, User_.id));
            }
        }
        return specification;
    }

}
