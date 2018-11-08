package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.repository.WordRepository;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.mapper.WordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Word.
 */
@Service
@Transactional
public class WordService {

    private final Logger log = LoggerFactory.getLogger(WordService.class);

    private final WordRepository wordRepository;

    private final WordMapper wordMapper;

    public WordService(WordRepository wordRepository, WordMapper wordMapper) {
        this.wordRepository = wordRepository;
        this.wordMapper = wordMapper;
    }

    /**
     * Save a word.
     *
     * @param wordDTO the entity to save
     * @return the persisted entity
     */
    public WordDTO save(WordDTO wordDTO) {
        log.debug("Request to save Word : {}", wordDTO);
        Word word = wordMapper.toEntity(wordDTO);
        word = wordRepository.save(word);
        return wordMapper.toDto(word);
    }

    /**
     * Get all the words.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Words");
        return wordRepository.findAll(pageable)
            .map(wordMapper::toDto);
    }

    /**
     * Get all the Word with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<WordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return wordRepository.findAllWithEagerRelationships(pageable).map(wordMapper::toDto);
    }
    

    /**
     * Get one word by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<WordDTO> findOne(Long id) {
        log.debug("Request to get Word : {}", id);
        return wordRepository.findOneWithEagerRelationships(id)
            .map(wordMapper::toDto);
    }

    /**
     * Delete the word by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Word : {}", id);
        wordRepository.deleteById(id);
    }
}
