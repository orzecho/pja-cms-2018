package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.WordsTestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WordsTest and its DTO WordsTestDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, WordMapper.class})
public interface WordsTestMapper extends EntityMapper<WordsTestDTO, WordsTest> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    WordsTestDTO toDto(WordsTest wordsTest);

    @Mapping(source = "creatorId", target = "creator")
    WordsTest toEntity(WordsTestDTO wordsTestDTO);

    default WordsTest fromId(Long id) {
        if (id == null) {
            return null;
        }
        WordsTest wordsTest = new WordsTest();
        wordsTest.setId(id);
        return wordsTest;
    }
}
