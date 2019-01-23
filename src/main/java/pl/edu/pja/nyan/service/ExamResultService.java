package pl.edu.pja.nyan.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.ExamResult;
import pl.edu.pja.nyan.repository.ExamResultRepository;
import pl.edu.pja.nyan.service.dto.ExamResultDTO;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerDTO;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;
import pl.edu.pja.nyan.service.mapper.ExamResultMapper;
/**
 * Service Implementation for managing ExamResult.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ExamResultService {

    private final Logger log = LoggerFactory.getLogger(ExamResultService.class);

    private final ExamResultRepository examResultRepository;

    private final ExamResultMapper examResultMapper;

    private final WrittenAnswerService writtenAnswerService;

    private final TrueFalseAnswerService trueFalseAnswerService;

    /**
     * Save a examResult.
     *
     * @param examResultDTO the entity to save
     * @return the persisted entity
     */
    public ExamResultDTO save(ExamResultDTO examResultDTO) {
        log.debug("Request to save ExamResult : {}", examResultDTO);
        ExamResult examResult = examResultRepository.save(examResultMapper.toEntity(examResultDTO));
        examResultDTO.getWrittenAnswers().forEach(answer -> {
            answer.setExamId(examResult.getId());
            writtenAnswerService.save(answer);
        });
        examResultDTO.getTrueFalseAnswers().forEach(answer -> {
            answer.setExamId(examResult.getId());
            trueFalseAnswerService.save(answer);
        });

        return examResultMapper.toDto(examResult);
    }

    /**
     * Get all the examResults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExamResultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExamResults");
        return examResultRepository.findAll(pageable)
            .map(examResultMapper::toDto);
    }


    /**
     * Get one examResult by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ExamResultDTO> findOne(Long id) {
        log.debug("Request to get ExamResult : {}", id);
        return examResultRepository.findById(id)
            .map(examResultMapper::toDto);
    }

    /**
     * Delete the examResult by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExamResult : {}", id);
        examResultRepository.deleteById(id);
    }

    public int calculateExamResult(ExamResultDTO examResult) {
        int writtenAnswerPoints = (int)examResult.getWrittenAnswers().stream()
            .filter(WrittenAnswerDTO::getIsRightAnswer)
            .count();
        int trueFalseAnswerPoints = (int)examResult.getTrueFalseAnswers().stream()
            .filter(TrueFalseAnswerDTO::getIsRightAnswer)
            .count();
        return writtenAnswerPoints + trueFalseAnswerPoints;
    }
}
