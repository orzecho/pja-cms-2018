package pl.edu.pja.nyan.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;
import pl.edu.pja.nyan.domain.ExamResult_;
import pl.edu.pja.nyan.domain.Word_;
import pl.edu.pja.nyan.domain.WrittenAnswer;
import pl.edu.pja.nyan.domain.WrittenAnswer_;
import pl.edu.pja.nyan.repository.WrittenAnswerRepository;
import pl.edu.pja.nyan.service.dto.WrittenAnswerCriteria;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;
import pl.edu.pja.nyan.service.mapper.WrittenAnswerMapper;

/**
 * Service for executing complex queries for WrittenAnswer entities in the database.
 * The main input is a {@link WrittenAnswerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WrittenAnswerDTO} or a {@link Page} of {@link WrittenAnswerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WrittenAnswerQueryService extends QueryService<WrittenAnswer> {

    private final Logger log = LoggerFactory.getLogger(WrittenAnswerQueryService.class);

    private final WrittenAnswerRepository writtenAnswerRepository;

    private final WrittenAnswerMapper writtenAnswerMapper;

    public WrittenAnswerQueryService(WrittenAnswerRepository writtenAnswerRepository, WrittenAnswerMapper writtenAnswerMapper) {
        this.writtenAnswerRepository = writtenAnswerRepository;
        this.writtenAnswerMapper = writtenAnswerMapper;
    }

    /**
     * Return a {@link List} of {@link WrittenAnswerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WrittenAnswerDTO> findByCriteria(WrittenAnswerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WrittenAnswer> specification = createSpecification(criteria);
        return writtenAnswerMapper.toDto(writtenAnswerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WrittenAnswerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WrittenAnswerDTO> findByCriteria(WrittenAnswerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WrittenAnswer> specification = createSpecification(criteria);
        return writtenAnswerRepository.findAll(specification, page)
            .map(writtenAnswerMapper::toDto);
    }

    /**
     * Function to convert WrittenAnswerCriteria to a {@link Specification}
     */
    private Specification<WrittenAnswer> createSpecification(WrittenAnswerCriteria criteria) {
        Specification<WrittenAnswer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WrittenAnswer_.id));
            }
            if (criteria.getTranslationFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTranslationFrom(), WrittenAnswer_.translationFrom));
            }
            if (criteria.getTranslation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTranslation(), WrittenAnswer_.translation));
            }
            if (criteria.getKana() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKana(), WrittenAnswer_.kana));
            }
            if (criteria.getKanji() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKanji(), WrittenAnswer_.kanji));
            }
            if (criteria.getIsRightAnswer() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRightAnswer(), WrittenAnswer_.isRightAnswer));
            }
            if (criteria.getWordId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWordId(),
                    WrittenAnswer_.word, Word_.id));
            }
            if (criteria.getExamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExamId(), WrittenAnswer_.exam, ExamResult_.id));
            }
        }
        return specification;
    }

}
