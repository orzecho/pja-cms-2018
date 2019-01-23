package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.TrueFalseAnswer;
import pl.edu.pja.nyan.domain.WrittenAnswer;
import pl.edu.pja.nyan.repository.ExamResultRepository;
import pl.edu.pja.nyan.repository.TrueFalseAnswerRepository;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerDTO;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;

/**
 * Mapper for the entity TrueFalseAnswer and its DTO TrueFalseAnswerDTO.
 */
@Service
@RequiredArgsConstructor
public class TrueFalseAnswerMapper implements EntityMapper<TrueFalseAnswerDTO, TrueFalseAnswer> {

    private final WordMapper wordMapper;
    private final TrueFalseAnswerRepository trueFalseAnswerRepository;
    private final ExamResultRepository examResultRepository;

    public TrueFalseAnswer fromId(Long id) {
        if (id == null) {
            return null;
        }
        TrueFalseAnswer trueFalseAnswer = new TrueFalseAnswer();
        trueFalseAnswer.setId(id);
        return trueFalseAnswer;
    }

    @Override
    public TrueFalseAnswer toEntity(TrueFalseAnswerDTO dto) {
        TrueFalseAnswer trueFalseAnswer;
        if (dto.getId() == null) {
            trueFalseAnswer = new TrueFalseAnswer();
        } else {
            trueFalseAnswer = trueFalseAnswerRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);
        }
        trueFalseAnswer.setId(dto.getId());
        trueFalseAnswer.setExam(examResultRepository.findById(dto.getExamId())
                .orElseThrow(EntityNotFoundException::new));
        trueFalseAnswer.setIsRightAnswer(dto.getIsRightAnswer());
        trueFalseAnswer.setSrcWord(wordMapper.toEntity(dto.getSrcWord()));
        trueFalseAnswer.setTargetWord(wordMapper.toEntity(dto.getTargetWord()));
        trueFalseAnswer.setTranslationFrom(dto.getTranslationFrom());
        return trueFalseAnswer;
    }

    @Override
    public TrueFalseAnswerDTO toDto(TrueFalseAnswer entity) {
        return TrueFalseAnswerDTO.builder()
            .id(entity.getId())
            .examId(entity.getExam().getId())
            .isRightAnswer(entity.isRightAnswer())
            .srcWord(wordMapper.toDto(entity.getSrcWord()))
            .targetWord(wordMapper.toDto(entity.getTargetWord()))
            .translationFrom(entity.getTranslationFrom())
            .build();
    }

    @Override
    public List<TrueFalseAnswer> toEntity(Collection<TrueFalseAnswerDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<TrueFalseAnswerDTO> toDto(Collection<TrueFalseAnswer> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }


}
