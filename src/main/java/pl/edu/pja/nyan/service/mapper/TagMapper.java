package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.TagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tag and its DTO TagDTO.
 */
@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {


    @Mapping(target = "words", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    default Tag fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }
}
