package pl.edu.pja.nyan.service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Tag;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

/**
 * Service Implementation for managing Word.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class WordService {

    private final Logger log = LoggerFactory.getLogger(WordService.class);

    private final WordRepository wordRepository;

    private final WordMapper wordMapper;

    private final TagService tagService;

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

    public void saveWordsIfNecessary(List<WordDTO> words, Tag lessonTag) {
        words.forEach(word -> {
            Word entity;
            Set<Tag> tags;
            if (word.getId() == null) {
                Optional<Word> optionalWord = wordRepository
                    .findByTranslationAndKanaAndKanji(word.getTranslation(), word.getKana(), word.getKanji());
                if (optionalWord.isPresent()) {
                    entity = optionalWord.get();
                    entity.setNote(word.getNote());
                    entity.addTag(lessonTag);
                    tags = tagService.findOrCreateTagsByName(word.getRawTags(), optionalWord.get());
                } else {
                    entity = wordRepository.getOne(save(word).getId()).addTag(lessonTag);
                    tags = tagService.findOrCreateTagsByName(word.getRawTags(), entity);
                }
            } else {
                entity = wordMapper.toEntity(word);
                tags = tagService.findOrCreateTagsByName(word.getRawTags(), entity);
                if (entity.getTags().stream().noneMatch(e -> e.getId().equals(lessonTag.getId()))) {
                    entity.addTag(lessonTag);
                    wordRepository.save(entity);
                }
            }
            tags.add(lessonTag);
            deleteOldTags(entity, tags);
        });
    }

    private Word deleteOldTags(Word w, Set<Tag> parsedTags) {
        Word word = wordRepository.findById(w.getId()).orElseThrow(EntityNotFoundException::new);
        List<Tag> deletedTags = word.getTags().stream()
            .filter(tag -> parsedTags.stream().noneMatch(f -> f.getId().equals(tag.getId())))
            .collect(Collectors.toList());
        deletedTags.forEach(e -> e.removeWord(word));
        return word;
    }
}
