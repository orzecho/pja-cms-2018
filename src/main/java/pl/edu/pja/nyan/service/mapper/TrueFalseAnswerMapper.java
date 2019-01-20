package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TrueFalseAnswer and its DTO TrueFalseAnswerDTO.
 */
@Mapper(componentModel = "spring", uses = {WordMapper.class, ExamResultMapper.class})
public interface TrueFalseAnswerMapper extends EntityMapper<TrueFalseAnswerDTO, TrueFalseAnswer> {

    @Mapping(source = "srcWord.id", target = "srcWordId")
    @Mapping(source = "targetWord.id", target = "targetWordId")
    @Mapping(source = "exam.id", target = "examId")
    TrueFalseAnswerDTO toDto(TrueFalseAnswer trueFalseAnswer);

    @Mapping(source = "srcWordId", target = "srcWord")
    @Mapping(source = "targetWordId", target = "targetWord")
    @Mapping(source = "examId", target = "exam")
    TrueFalseAnswer toEntity(TrueFalseAnswerDTO trueFalseAnswerDTO);

    default TrueFalseAnswer fromId(Long id) {
        if (id == null) {
            return null;
        }
        TrueFalseAnswer trueFalseAnswer = new TrueFalseAnswer();
        trueFalseAnswer.setId(id);
        return trueFalseAnswer;
    }
}
