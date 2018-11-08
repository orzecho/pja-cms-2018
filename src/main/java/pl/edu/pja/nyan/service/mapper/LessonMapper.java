package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.LessonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Lesson and its DTO LessonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LessonMapper extends EntityMapper<LessonDTO, Lesson> {


    @Mapping(target = "lessonFiles", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Lesson toEntity(LessonDTO lessonDTO);

    default Lesson fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lesson lesson = new Lesson();
        lesson.setId(id);
        return lesson;
    }
}
