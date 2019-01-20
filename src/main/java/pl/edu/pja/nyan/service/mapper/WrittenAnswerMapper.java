package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WrittenAnswer and its DTO WrittenAnswerDTO.
 */
@Mapper(componentModel = "spring", uses = {WordMapper.class, ExamResultMapper.class})
public interface WrittenAnswerMapper extends EntityMapper<WrittenAnswerDTO, WrittenAnswer> {

    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "exam.id", target = "examId")
    WrittenAnswerDTO toDto(WrittenAnswer writtenAnswer);

    @Mapping(source = "answerId", target = "answer")
    @Mapping(source = "examId", target = "exam")
    WrittenAnswer toEntity(WrittenAnswerDTO writtenAnswerDTO);

    default WrittenAnswer fromId(Long id) {
        if (id == null) {
            return null;
        }
        WrittenAnswer writtenAnswer = new WrittenAnswer();
        writtenAnswer.setId(id);
        return writtenAnswer;
    }
}
