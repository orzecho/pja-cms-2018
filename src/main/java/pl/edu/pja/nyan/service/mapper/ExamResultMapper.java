package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.ExamResultDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ExamResult and its DTO ExamResultDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ExamMapper.class})
public interface ExamResultMapper extends EntityMapper<ExamResultDTO, ExamResult> {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.login", target = "studentLogin")
    @Mapping(source = "exam.id", target = "examId")
    ExamResultDTO toDto(ExamResult examResult);

    @Mapping(target = "writtenAnswers", ignore = true)
    @Mapping(target = "trueFalseAnswers", ignore = true)
    @Mapping(source = "studentId", target = "student")
    @Mapping(source = "examId", target = "exam")
    ExamResult toEntity(ExamResultDTO examResultDTO);

    default ExamResult fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExamResult examResult = new ExamResult();
        examResult.setId(id);
        return examResult;
    }
}
