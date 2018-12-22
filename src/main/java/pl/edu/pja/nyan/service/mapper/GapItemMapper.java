package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.GapItem;
import pl.edu.pja.nyan.repository.GapItemRepository;
import pl.edu.pja.nyan.service.dto.GapItemDTO;

@RequiredArgsConstructor
@Service
public class GapItemMapper implements EntityMapper<GapItemDTO, GapItem> {

    private final GapItemRepository gapItemRepository;

    @Override
    public GapItemDTO toDto(GapItem gapItem) {
        return GapItemDTO.builder()
            .id(gapItem.getId())
            .key(gapItem.getKey())
            .value(gapItem.getValue())
            .build();
    }

    @Override
    public GapItem toEntity(GapItemDTO gapItemDTO) {
        GapItem gapItem;
        if (gapItemDTO.getId() == null) {
            gapItem = new GapItem();
        } else {
            gapItem = gapItemRepository.getOne(gapItemDTO.getId());
        }
        gapItem.setKey(gapItemDTO.getKey());
        gapItem.setValue(gapItemDTO.getValue());

        return gapItem;
    }

    @Override
    public List<GapItem> toEntity(Collection<GapItemDTO> dtoList) {
        if (dtoList != null) {
            return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
        } else {
            return Lists.newArrayList();
        }
    }

    @Override
    public List<GapItemDTO> toDto(Collection<GapItem> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }


}
