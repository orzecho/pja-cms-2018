package pl.edu.pja.nyan.service.mapper;

import pl.edu.pja.nyan.domain.*;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FillingGapsTestItem and its DTO FillingGapsTestItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FillingGapsTestItemMapper extends EntityMapper<FillingGapsTestItemDTO, FillingGapsTestItem> {


    @Mapping(target = "gapItems", ignore = true)
    @Mapping(target = "tags", ignore = true)
    FillingGapsTestItem toEntity(FillingGapsTestItemDTO fillingGapsTestItemDTO);

    default FillingGapsTestItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        FillingGapsTestItem fillingGapsTestItem = new FillingGapsTestItem();
        fillingGapsTestItem.setId(id);
        return fillingGapsTestItem;
    }
}
