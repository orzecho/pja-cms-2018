package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.WrittenAnswer;
import pl.edu.pja.nyan.repository.WrittenAnswerRepository;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;
import pl.edu.pja.nyan.service.mapper.WrittenAnswerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing WrittenAnswer.
 */
@Service
@Transactional
public class WrittenAnswerService {

    private final Logger log = LoggerFactory.getLogger(WrittenAnswerService.class);

    private final WrittenAnswerRepository writtenAnswerRepository;

    private final WrittenAnswerMapper writtenAnswerMapper;

    public WrittenAnswerService(WrittenAnswerRepository writtenAnswerRepository, WrittenAnswerMapper writtenAnswerMapper) {
        this.writtenAnswerRepository = writtenAnswerRepository;
        this.writtenAnswerMapper = writtenAnswerMapper;
    }

    /**
     * Save a writtenAnswer.
     *
     * @param writtenAnswerDTO the entity to save
     * @return the persisted entity
     */
    public WrittenAnswerDTO save(WrittenAnswerDTO writtenAnswerDTO) {
        log.debug("Request to save WrittenAnswer : {}", writtenAnswerDTO);
        WrittenAnswer writtenAnswer = writtenAnswerMapper.toEntity(writtenAnswerDTO);
        writtenAnswer = writtenAnswerRepository.save(writtenAnswer);
        return writtenAnswerMapper.toDto(writtenAnswer);
    }

    /**
     * Get all the writtenAnswers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WrittenAnswerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WrittenAnswers");
        return writtenAnswerRepository.findAll(pageable)
            .map(writtenAnswerMapper::toDto);
    }


    /**
     * Get one writtenAnswer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<WrittenAnswerDTO> findOne(Long id) {
        log.debug("Request to get WrittenAnswer : {}", id);
        return writtenAnswerRepository.findById(id)
            .map(writtenAnswerMapper::toDto);
    }

    /**
     * Delete the writtenAnswer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WrittenAnswer : {}", id);
        writtenAnswerRepository.deleteById(id);
    }
}
