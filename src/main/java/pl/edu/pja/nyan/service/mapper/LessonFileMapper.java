package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.LessonFileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LessonFile and its DTO LessonFileDTO.
 */
@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface LessonFileMapper extends EntityMapper<LessonFileDTO, LessonFile> {

    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "lesson.name", target = "lessonName")
    LessonFileDTO toDto(LessonFile lessonFile);

    @Mapping(source = "lessonId", target = "lesson")
    LessonFile toEntity(LessonFileDTO lessonFileDTO);

    default LessonFile fromId(Long id) {
        if (id == null) {
            return null;
        }
        LessonFile lessonFile = new LessonFile();
        lessonFile.setId(id);
        return lessonFile;
    }
}
