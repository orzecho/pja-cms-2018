package pl.edu.pja.nyan.service.mapper;

import java.util.HashSet;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.ExamResult;
import pl.edu.pja.nyan.repository.ExamRepository;
import pl.edu.pja.nyan.repository.ExamResultRepository;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.dto.ExamResultDTO;

/**
 * Mapper for the entity ExamResult and its DTO ExamResultDTO.
 */
@Service
@RequiredArgsConstructor
public class ExamResultMapper implements EntityMapper<ExamResultDTO, ExamResult> {

    private final ExamResultRepository examResultRepository;
    private final ExamRepository examRepository;
    private final UserService userService;
    private final WrittenAnswerMapper writtenAnswerMapper;
    private final TrueFalseAnswerMapper trueFalseAnswerMapper;

    @Override
    public ExamResult toEntity(ExamResultDTO dto) {
        ExamResult entity;
        if (dto.getId() == null) {
            entity = new ExamResult();
        } else {
            entity = examResultRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);
        }

        entity.setId(dto.getId());
        entity.setExam(examRepository.findById(dto.getExamId())
            .orElseThrow(EntityNotFoundException::new));

        if (dto.getId() != null) {
            entity.setWrittenAnswers(new HashSet<>(
                writtenAnswerMapper.toEntity(dto.getWrittenAnswers())));
            entity.setTrueFalseAnswers(new HashSet<>(
                trueFalseAnswerMapper.toEntity(dto.getTrueFalseAnswers())));
        }

        entity.setDate(dto.getDate());
        entity.setResult(dto.getResult());
        entity.setStudent(userService.getUserWithAuthorities(dto.getStudentId())
            .orElseThrow(EntityNotFoundException::new));

        return entity;
    }

    @Override
    public ExamResultDTO toDto(ExamResult entity) {
        return ExamResultDTO.builder()
            .id(entity.getId())
            .examId(entity.getExam().getId())
            .writtenAnswers(writtenAnswerMapper.toDto(entity.getWrittenAnswers()))
            .trueFalseAnswers(trueFalseAnswerMapper.toDto(entity.getTrueFalseAnswers()))
            .date(entity.getDate())
            .result(entity.getResult())
            .studentId(entity.getStudent().getId())
            .build();
    }

    public ExamResult fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExamResult examResult = new ExamResult();
        examResult.setId(id);
        return examResult;
    }

}
