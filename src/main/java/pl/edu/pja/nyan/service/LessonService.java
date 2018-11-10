package pl.edu.pja.nyan.service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.repository.LessonRepository;
import pl.edu.pja.nyan.service.dto.LessonDTO;
import pl.edu.pja.nyan.service.mapper.LessonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
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

    /**
     * Save a lesson.
     *
     * @param lessonDTO the entity to save
     * @return the persisted entity
     */
    public LessonDTO save(LessonDTO lessonDTO) {
        log.debug("Request to save Lesson : {}", lessonDTO);
        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson = lessonRepository.save(lesson);
        if (lessonDTO.getRawTags() != null) {
            tagService.parseTags(lessonDTO.getRawTags(), lesson);
        }
        return lessonMapper.toDto(lesson);
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
}
