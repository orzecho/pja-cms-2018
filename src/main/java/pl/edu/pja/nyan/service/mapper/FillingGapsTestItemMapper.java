package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.FillingGapsTestItem;
import pl.edu.pja.nyan.domain.GapItem;
import pl.edu.pja.nyan.repository.FillingGapsTestItemRepository;
import pl.edu.pja.nyan.repository.GapItemRepository;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;

@Service
@RequiredArgsConstructor
public class FillingGapsTestItemMapper implements EntityMapper<FillingGapsTestItemDTO, FillingGapsTestItem> {

    private final FillingGapsTestItemRepository fillingGapsTestItemRepository;
    private final GapItemMapper gapItemMapper;
    private final GapItemRepository gapItemRepository;

    @Override
    public FillingGapsTestItem toEntity(FillingGapsTestItemDTO dto) {
        FillingGapsTestItem fillingGapsTestItem;
        if (dto.getId() == null) {
            fillingGapsTestItem = new FillingGapsTestItem();
        } else {
            fillingGapsTestItem = fillingGapsTestItemRepository.getOne(dto.getId());
        }
        fillingGapsTestItem.setQuestion(dto.getQuestion());
        fillingGapsTestItem.setGapItems(saveAndGetGapItems(dto));

        return fillingGapsTestItem;
    }

    private Set<GapItem> saveAndGetGapItems(FillingGapsTestItemDTO dto) {
        List<GapItem> items = gapItemMapper.toEntity(dto.getGapItems())
            .stream()
            .map(gapItemRepository::save)
            .collect(Collectors.toList());
        return Sets.newHashSet(items);
    }

    @Override
    public FillingGapsTestItemDTO toDto(FillingGapsTestItem entity) {
        return FillingGapsTestItemDTO.builder()
            .id(entity.getId())
            .question(entity.getQuestion())
            .gapItems(gapItemMapper.toDto(entity.getGapItems()))
            .build();
    }

    @Override
    public List<FillingGapsTestItem> toEntity(Collection<FillingGapsTestItemDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<FillingGapsTestItemDTO> toDto(Collection<FillingGapsTestItem> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
