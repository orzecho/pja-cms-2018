package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.TrueFalseAnswer;
import pl.edu.pja.nyan.repository.TrueFalseAnswerRepository;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerDTO;
import pl.edu.pja.nyan.service.mapper.TrueFalseAnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing TrueFalseAnswer.
 */
@Service
@Transactional
public class TrueFalseAnswerService {

    private final Logger log = LoggerFactory.getLogger(TrueFalseAnswerService.class);

    private final TrueFalseAnswerRepository trueFalseAnswerRepository;

    private final TrueFalseAnswerMapper trueFalseAnswerMapper;

    public TrueFalseAnswerService(TrueFalseAnswerRepository trueFalseAnswerRepository, TrueFalseAnswerMapper trueFalseAnswerMapper) {
        this.trueFalseAnswerRepository = trueFalseAnswerRepository;
        this.trueFalseAnswerMapper = trueFalseAnswerMapper;
    }

    /**
     * Save a trueFalseAnswer.
     *
     * @param trueFalseAnswerDTO the entity to save
     * @return the persisted entity
     */
    public TrueFalseAnswerDTO save(TrueFalseAnswerDTO trueFalseAnswerDTO) {
        log.debug("Request to save TrueFalseAnswer : {}", trueFalseAnswerDTO);
        TrueFalseAnswer trueFalseAnswer = trueFalseAnswerMapper.toEntity(trueFalseAnswerDTO);
        trueFalseAnswer = trueFalseAnswerRepository.save(trueFalseAnswer);
        return trueFalseAnswerMapper.toDto(trueFalseAnswer);
    }

    /**
     * Get all the trueFalseAnswers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TrueFalseAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TrueFalseAnswers");
        return trueFalseAnswerRepository.findAll(pageable)
            .map(trueFalseAnswerMapper::toDto);
    }


    /**
     * Get one trueFalseAnswer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TrueFalseAnswerDTO> findOne(Long id) {
        log.debug("Request to get TrueFalseAnswer : {}", id);
        return trueFalseAnswerRepository.findById(id)
            .map(trueFalseAnswerMapper::toDto);
    }

    /**
     * Delete the trueFalseAnswer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TrueFalseAnswer : {}", id);
        trueFalseAnswerRepository.deleteById(id);
    }
}
