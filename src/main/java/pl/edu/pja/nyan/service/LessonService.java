package pl.edu.pja.nyan.service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.repository.LessonRepository;
import pl.edu.pja.nyan.service.dto.LessonDTO;
import pl.edu.pja.nyan.service.mapper.LessonMapper;
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
 * Service Implementation for managing Lesson.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LessonService {

    private final Logger log = LoggerFactory.getLogger(LessonService.class);

    private final LessonRepository lessonRepository;

    private final LessonMapper lessonMapper;

    private final TagService tagService;

    private final WordService wordService;

    /**
     * Save a lesson.
     *
     * @param lessonDTO the entity to save
     * @return the persisted entity
     */
    public LessonDTO save(LessonDTO lessonDTO) {
        log.debug("Request to save Lesson : {}", lessonDTO);
        Lesson lesson = lessonRepository.save(lessonMapper.toEntity(lessonDTO));
        Set<Tag> parsedTags = tagService.parseTags(lessonDTO.getRawTags());
        parsedTags.stream()
            .filter(e -> e.getLessons().stream().noneMatch(l -> l.getId().equals(lesson.getId())))
            .forEach(e -> e.addLesson(lesson));
        Lesson lessonWithDeletedTags = deleteOldTags(lesson, parsedTags);
        Tag lessonTag = tagService.addLessonTag(lessonWithDeletedTags);
        if (lessonDTO.getWords() != null) {
            wordService.saveWordsIfNecessary(lessonDTO.getWords(), lessonTag);
        }
        return lessonMapper.toDto(lessonWithDeletedTags);
    }

    /**
     * Get all the lessons.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LessonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lessons");
        return lessonRepository.findAll(pageable)
            .map(lessonMapper::toDto);
    }


    /**
     * Get one lesson by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LessonDTO> findOne(Long id) {
        log.debug("Request to get Lesson : {}", id);
        return lessonRepository.findById(id)
            .map(lessonMapper::toDto);
    }

    /**
     * Delete the lesson by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Lesson : {}", id);
        lessonRepository.deleteById(id);
    }

    private Lesson deleteOldTags(Lesson l, Set<Tag> parsedTags) {
        Lesson lesson = lessonRepository.findById(l.getId()).orElseThrow(EntityNotFoundException::new);
        List<Tag> deletedTags = lesson.getTags().stream()
            .filter(tag -> parsedTags.stream().noneMatch(f -> f.getId().equals(tag.getId())))
            .collect(Collectors.toList());
        deletedTags.forEach(e -> e.removeLesson(lesson));
        return lesson;
    }
}
