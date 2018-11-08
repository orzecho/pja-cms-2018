package pl.edu.pja.nyan.service;

import pl.edu.pja.nyan.domain.LessonFile;
import pl.edu.pja.nyan.repository.LessonFileRepository;
import pl.edu.pja.nyan.service.dto.LessonFileDTO;
import pl.edu.pja.nyan.service.mapper.LessonFileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing LessonFile.
 */
@Service
@Transactional
public class LessonFileService {

    private final Logger log = LoggerFactory.getLogger(LessonFileService.class);

    private final LessonFileRepository lessonFileRepository;

    private final LessonFileMapper lessonFileMapper;

    public LessonFileService(LessonFileRepository lessonFileRepository, LessonFileMapper lessonFileMapper) {
        this.lessonFileRepository = lessonFileRepository;
        this.lessonFileMapper = lessonFileMapper;
    }

    /**
     * Save a lessonFile.
     *
     * @param lessonFileDTO the entity to save
     * @return the persisted entity
     */
    public LessonFileDTO save(LessonFileDTO lessonFileDTO) {
        log.debug("Request to save LessonFile : {}", lessonFileDTO);
        LessonFile lessonFile = lessonFileMapper.toEntity(lessonFileDTO);
        lessonFile = lessonFileRepository.save(lessonFile);
        return lessonFileMapper.toDto(lessonFile);
    }

    /**
     * Get all the lessonFiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LessonFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LessonFiles");
        return lessonFileRepository.findAll(pageable)
            .map(lessonFileMapper::toDto);
    }


    /**
     * Get one lessonFile by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<LessonFileDTO> findOne(Long id) {
        log.debug("Request to get LessonFile : {}", id);
        return lessonFileRepository.findById(id)
            .map(lessonFileMapper::toDto);
    }

    /**
     * Delete the lessonFile by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LessonFile : {}", id);
        lessonFileRepository.deleteById(id);
    }
}
