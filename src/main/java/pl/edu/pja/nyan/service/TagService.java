package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.repository.TagRepository;
import pl.edu.pja.nyan.service.dto.TagDTO;
import pl.edu.pja.nyan.service.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Tag.
 */
@Service
@Transactional
public class TagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    /**
     * Save a tag.
     *
     * @param tagDTO the entity to save
     * @return the persisted entity
     */
    public TagDTO save(TagDTO tagDTO) {
        log.debug("Request to save Tag : {}", tagDTO);
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        return tagRepository.findAll(pageable)
            .map(tagMapper::toDto);
    }

    /**
     * Get all the Tag with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<TagDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tagRepository.findAllWithEagerRelationships(pageable).map(tagMapper::toDto);
    }


    /**
     * Get one tag by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TagDTO> findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findOneWithEagerRelationships(id)
            .map(tagMapper::toDto);
    }

    /**
     * Delete the tag by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.deleteById(id);
    }

    public Set<Tag> parseTags(String rawTags) {
        Set<Tag> tags = new HashSet<>();
        if (rawTags == null) {
            return tags;
        }
        try (Scanner scanner = new Scanner(rawTags).useDelimiter(",")) {
            while (scanner.hasNext()) {
                String token = scanner.next().trim();
                Optional<Tag> tag = tagRepository.findByName(token);
                if (!tag.isPresent()) {
                    tags.add(tagRepository.save(new Tag(token)));
                } else {
                    tags.add(tag.get());
                }
            }
        }
        return tags;
    }

    public Set<Tag> parseTags(String rawTags, Word word) {
        Set<Tag> tags = new HashSet<>();
        if (rawTags == null || word == null) {
            return tags;
        }
        try (Scanner scanner = new Scanner(rawTags).useDelimiter(",")) {
            while (scanner.hasNext()) {
                String token = scanner.next().trim();
                Optional<Tag> tag = tagRepository.findByName(token);
                if (!tag.isPresent()) {
                    tags.add(tagRepository.save(new Tag(token, word)));
                } else {
                    if (tag.get().getWords().stream().noneMatch(e -> e.getId().equals(word.getId()))) {
                        tag.get().addWord(word);
                    }
                    tags.add(tag.get());
                }
            }
        }
        return tags;
    }

    public Tag addLessonTag(Lesson lesson) {
        String lessonName = lesson.getName();
        Optional<Tag> tagOptional = tagRepository.findByName(lessonName);
        Tag tag;
        if (tagOptional.isPresent()) {
            if (tagOptional.get().getLessons().stream().noneMatch(e -> e.getId().equals(lesson.getId()))) {
                tag = tagOptional.get().addLesson(lesson);
            } else {
                tag = tagOptional.get();
            }
        } else {
            tag = tagRepository.save(new Tag(lessonName, lesson));
        }
        return tag;
    }

    public List<Tag> findByTagNames(String stringIds) {
        List<String> names = Arrays.stream(stringIds.split(","))
            .map(String::trim)
            .collect(Collectors.toList());
        return findByTagNames(names);
    }

    public List<Tag> findByTagNames(List<String> names) {
        return names.stream()
            .map(tagRepository::findByName)
            .filter(Optional::isPresent)
            .map(Optional::get).collect(Collectors.toList());
    }
}
