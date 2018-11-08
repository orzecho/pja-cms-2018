package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.WordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Word and its DTO WordDTO.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface WordMapper extends EntityMapper<WordDTO, Word> {



    default Word fromId(Long id) {
        if (id == null) {
            return null;
        }
        Word word = new Word();
        word.setId(id);
        return word;
    }
}
