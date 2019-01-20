package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.ExamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Exam and its DTO ExamDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, WordMapper.class})
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    ExamDTO toDto(Exam exam);

    @Mapping(target = "results", ignore = true)
    @Mapping(source = "creatorId", target = "creator")
    Exam toEntity(ExamDTO examDTO);

    default Exam fromId(Long id) {
        if (id == null) {
            return null;
        }
        Exam exam = new Exam();
        exam.setId(id);
        return exam;
    }
}
