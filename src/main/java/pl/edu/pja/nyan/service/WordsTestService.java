package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.WordsTest;
import pl.edu.pja.nyan.repository.WordsTestRepository;
import pl.edu.pja.nyan.service.dto.WordsTestDTO;
import pl.edu.pja.nyan.service.mapper.WordsTestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing WordsTest.
 */
@Service
@Transactional
public class WordsTestService {

    private final Logger log = LoggerFactory.getLogger(WordsTestService.class);

    private final WordsTestRepository wordsTestRepository;

    private final WordsTestMapper wordsTestMapper;

    public WordsTestService(WordsTestRepository wordsTestRepository, WordsTestMapper wordsTestMapper) {
        this.wordsTestRepository = wordsTestRepository;
        this.wordsTestMapper = wordsTestMapper;
    }

    /**
     * Save a wordsTest.
     *
     * @param wordsTestDTO the entity to save
     * @return the persisted entity
     */
    public WordsTestDTO save(WordsTestDTO wordsTestDTO) {
        log.debug("Request to save WordsTest : {}", wordsTestDTO);
        WordsTest wordsTest = wordsTestMapper.toEntity(wordsTestDTO);
        wordsTest = wordsTestRepository.save(wordsTest);
        return wordsTestMapper.toDto(wordsTest);
    }

    /**
     * Get all the wordsTests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WordsTestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WordsTests");
        return wordsTestRepository.findAll(pageable)
            .map(wordsTestMapper::toDto);
    }

    /**
     * Get all the WordsTest with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<WordsTestDTO> findAllWithEagerRelationships(Pageable pageable) {
        return wordsTestRepository.findAllWithEagerRelationships(pageable).map(wordsTestMapper::toDto);
    }
    

    /**
     * Get one wordsTest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<WordsTestDTO> findOne(Long id) {
        log.debug("Request to get WordsTest : {}", id);
        return wordsTestRepository.findOneWithEagerRelationships(id)
            .map(wordsTestMapper::toDto);
    }

    /**
     * Delete the wordsTest by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WordsTest : {}", id);
        wordsTestRepository.deleteById(id);
    }
}
