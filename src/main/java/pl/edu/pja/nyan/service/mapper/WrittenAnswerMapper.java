package pl.edu.pja.nyan.service.mapper;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.WrittenAnswer;
import pl.edu.pja.nyan.repository.ExamResultRepository;
import pl.edu.pja.nyan.repository.WrittenAnswerRepository;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;

/**
 * Mapper for the entity WrittenAnswer and its DTO WrittenAnswerDTO.
 */
@Service
@RequiredArgsConstructor
public class WrittenAnswerMapper implements EntityMapper<WrittenAnswerDTO, WrittenAnswer> {

    public final WrittenAnswerRepository writtenAnswerRepository;
    public final WordMapper wordMapper;
    public final ExamResultRepository examResultRepository;

    @Override
    public WrittenAnswer toEntity(WrittenAnswerDTO dto) {
        WrittenAnswer entity;
        if (dto.getId() == null) {
            entity = new WrittenAnswer();
        } else {
            entity = writtenAnswerRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);
        }
        entity.setWord(wordMapper.toEntity(dto.getWord()));
        entity.setExam(examResultRepository.findById(dto.getExamId())
                .orElseThrow(EntityNotFoundException::new));
        entity.setIsRightAnswer(dto.getIsRightAnswer());
        entity.setKana(dto.getKana());
        entity.setKanji(dto.getKanji());
        entity.setRomaji(dto.getRomaji());
        entity.setTranslation(dto.getTranslation());
        entity.setTranslationFrom(dto.getTranslationFrom());
        return entity;
    }

    @Override
    public WrittenAnswerDTO toDto(WrittenAnswer entity) {
        return WrittenAnswerDTO.builder()
            .word(wordMapper.toDto(entity.getWord()))
            .examId(entity.getExam().getId())
            .isRightAnswer(entity.isRightAnswer())
            .kana(entity.getKana())
            .kanji(entity.getKanji())
            .romaji(entity.getRomaji())
            .translation(entity.getTranslation())
            .translationFrom(entity.getTranslationFrom())
            .build();
    }

    public WrittenAnswer fromId(Long id) {
        if (id == null) {
            return null;
        }
        WrittenAnswer writtenAnswer = new WrittenAnswer();
        writtenAnswer.setId(id);
        return writtenAnswer;
    }
}
