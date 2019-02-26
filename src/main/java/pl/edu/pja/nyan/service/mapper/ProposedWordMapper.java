package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.ProposedWordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProposedWord and its DTO ProposedWordDTO.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class, UserMapper.class})
public interface ProposedWordMapper extends EntityMapper<ProposedWordDTO, ProposedWord> {

    @Mapping(source = "addedBy.id", target = "addedById")
    @Mapping(source = "addedBy.login", target = "addedByLogin")
    ProposedWordDTO toDto(ProposedWord proposedWord);

    @Mapping(source = "addedById", target = "addedBy")
    ProposedWord toEntity(ProposedWordDTO proposedWordDTO);

    default ProposedWord fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProposedWord proposedWord = new ProposedWord();
        proposedWord.setId(id);
        return proposedWord;
    }
}
