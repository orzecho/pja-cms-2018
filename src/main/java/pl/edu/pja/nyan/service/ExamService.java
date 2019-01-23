package pl.edu.pja.nyan.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.pja.nyan.domain.Exam;
import pl.edu.pja.nyan.repository.ExamRepository;
import pl.edu.pja.nyan.service.dto.ExamDTO;
import pl.edu.pja.nyan.service.mapper.ExamMapper;
/**
 * Service Implementation for managing Exam.
 */
@Service
@Transactional
public class ExamService {

    private final Logger log = LoggerFactory.getLogger(ExamService.class);

    private final ExamRepository examRepository;

    private final ExamMapper examMapper;

    private final UserService userService;

    public ExamService(ExamRepository examRepository, ExamMapper examMapper,
        UserService userService) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.userService = userService;
    }

    /**
     * Save a exam.
     *
     * @param examDTO the entity to save
     * @return the persisted entity
     */
    public ExamDTO save(ExamDTO examDTO) {
        log.debug("Request to save Exam : {}", examDTO);
//        user = userService.getUserWithAuthorities()
//                .orElseThrow();
//        examDTO.setCreatorId();
        Exam exam = examMapper.toEntity(examDTO);
        exam = examRepository.save(exam);
        return examMapper.toDto(exam);
    }

    /**
     * Get all the exams.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exams");
        return examRepository.findAll(pageable)
            .map(examMapper::toDto);
    }

    /**
     * Get all the Exam with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ExamDTO> findAllWithEagerRelationships(Pageable pageable) {
        return examRepository.findAllWithEagerRelationships(pageable).map(examMapper::toDto);
    }
    

    /**
     * Get one exam by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ExamDTO> findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        return examRepository.findOneWithEagerRelationships(id)
            .map(examMapper::toDto);
    }

    /**
     * Get one exam by code.
     *
     * @param code the code of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ExamDTO> findByCode(String code) {
        log.debug("Request to get Exam by code : {}", code);
        return examRepository.findOneByCodeWithEagerRelationships(code)
            .map(examMapper::toDto);
    }

    /**
     * Delete the exam by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
    }

    public boolean examAlreadyExists(String testCode) {
        return findByCode(testCode).isPresent();
    }

}
