package pl.edu.pja.nyan.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

import pl.edu.pja.nyan.domain.ProposedWord;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.User;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.repository.ProposedWordRepository;
import pl.edu.pja.nyan.service.dto.ProposedWordDTO;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.mapper.ProposedWordMapper;

/**
 * Service Implementation for managing ProposedWord.
 */
@Service
@Transactional
public class ProposedWordService {

    private final Logger log = LoggerFactory.getLogger(ProposedWordService.class);

    private final ProposedWordRepository proposedWordRepository;

    private final ProposedWordMapper proposedWordMapper;

    private final WordService wordService;

    private final TagService tagService;

    public ProposedWordService(ProposedWordRepository proposedWordRepository,
        ProposedWordMapper proposedWordMapper, WordService wordService, TagService tagService) {
        this.proposedWordRepository = proposedWordRepository;
        this.proposedWordMapper = proposedWordMapper;
        this.wordService = wordService;
        this.tagService = tagService;
    }

    public ProposedWordDTO save(ProposedWordDTO proposedWordDTO) {
        log.debug("Request to save ProposedWord : {}", proposedWordDTO);
        ProposedWord proposedWord = proposedWordMapper.toEntity(proposedWordDTO);
        proposedWord = proposedWordRepository.save(proposedWord);
        return proposedWordMapper.toDto(proposedWord);
    }

    public ProposedWordDTO save(ProposedWordDTO proposedWordDTO, List<String> rawTags) {
        log.debug("Request to save ProposedWord : {}", proposedWordDTO);
        ProposedWord proposedWord = proposedWordMapper.toEntity(proposedWordDTO);
        proposedWord = proposedWordRepository.save(proposedWord);
        Set<Tag> tags = tagService.findOrCreateTagsByName(rawTags);
        proposedWord.setTags(tags);
        return proposedWordMapper.toDto(proposedWord);
    }

    @Transactional(readOnly = true)
    public Page<ProposedWordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProposedWords");
        return proposedWordRepository.findAll(pageable)
            .map(proposedWordMapper::toDto);
    }

    /**
     * Get all the ProposedWord with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ProposedWordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return proposedWordRepository.findAllWithEagerRelationships(pageable).map(proposedWordMapper::toDto);
    }


    /**
     * Get one proposedWord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProposedWordDTO> findOne(Long id) {
        log.debug("Request to get ProposedWord : {}", id);
        return proposedWordRepository.findOneWithEagerRelationships(id)
            .map(proposedWordMapper::toDto);
    }

    /**
     * Delete the proposedWord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProposedWord : {}", id);
        proposedWordRepository.deleteById(id);
    }

    public ProposedWordDTO save(WordDTO wordDTO, User userWithAuthorities) {
        return save(ProposedWordDTO.builder()
            .addedById(userWithAuthorities.getId())
            .kana(wordDTO.getKana())
            .kanji(wordDTO.getKanji())
            .translation(wordDTO.getTranslation())
            .note(wordDTO.getNote())
            .romaji(wordDTO.getRomaji())
            .tags(Optional.ofNullable(wordDTO.getTags()).map(Sets::newHashSet).orElse(Sets.newHashSet()))
            .build(), wordDTO.getRawTags());
    }

    public WordDTO acceptWord(Long id) {
        ProposedWord proposedWord = this.proposedWordRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Optional<Word> word = this.wordService.findParallel(proposedWord);
        if (word.isPresent()) {
            return this.wordService.addTags(word.get(), proposedWord.getTags());
        } else {
            return this.wordService.save(proposedWord);
        }
    }
}
