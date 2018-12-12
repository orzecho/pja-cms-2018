package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.GapItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GapItem and its DTO GapItemDTO.
 */
@Mapper(componentModel = "spring", uses = {FillingGapsTestItemMapper.class})
public interface GapItemMapper extends EntityMapper<GapItemDTO, GapItem> {

    @Mapping(source = "testItem.id", target = "testItemId")
    GapItemDTO toDto(GapItem gapItem);

    @Mapping(source = "testItemId", target = "testItem")
    GapItem toEntity(GapItemDTO gapItemDTO);

    default GapItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        GapItem gapItem = new GapItem();
        gapItem.setId(id);
        return gapItem;
    }
}
