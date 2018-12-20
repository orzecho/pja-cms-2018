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
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.repository.FillingGapsTestItemRepository;
import pl.edu.pja.nyan.repository.GapItemRepository;
import pl.edu.pja.nyan.repository.TagRepository;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;

@Service
@RequiredArgsConstructor
public class FillingGapsTestItemMapper implements EntityMapper<FillingGapsTestItemDTO, FillingGapsTestItem> {

    private final FillingGapsTestItemRepository fillingGapsTestItemRepository;
    private final GapItemMapper gapItemMapper;
    private final GapItemRepository gapItemRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public FillingGapsTestItem toEntity(FillingGapsTestItemDTO dto) {
        FillingGapsTestItem fillingGapsTestItem;
        if (dto.getId() == null) {
            fillingGapsTestItem = new FillingGapsTestItem();
        } else {
            fillingGapsTestItem = fillingGapsTestItemRepository.getOne(dto.getId());
            clearTags(fillingGapsTestItem);
        }
        fillingGapsTestItem.setQuestion(dto.getQuestion());
        fillingGapsTestItem = fillingGapsTestItemRepository.save(fillingGapsTestItem);
        fillingGapsTestItem.setGapItems(saveAndGetGapItems(dto, fillingGapsTestItem));
        createAndAssignTags(dto.getRawTags(), fillingGapsTestItem);

        return fillingGapsTestItem;
    }

    private void createAndAssignTags(List<String> rawTags,
        FillingGapsTestItem fillingGapsTestItem) {
        rawTags.stream()
            .map(rawTag -> tagRepository.findByName(rawTag).orElseGet(() -> tagRepository.save(new Tag(rawTag))))
            .peek(tag -> tag.addFillingGapsTestItem(fillingGapsTestItem))
            .forEach(tagRepository::save);
    }

    private void clearTags(FillingGapsTestItem fillingGapsTestItem) {
        tagRepository.findAllWithEagerRelationships().stream()
            .filter(e -> e.getFillingGapsTestItems().contains(fillingGapsTestItem))
            .forEach(e -> e.removeFillingGapsTestItem(fillingGapsTestItem));
    }

    private Set<GapItem> saveAndGetGapItems(FillingGapsTestItemDTO dto,
        FillingGapsTestItem fillingGapsTestItem) {
        List<GapItem> gapItems = gapItemMapper.toEntity(dto.getGapItems());
        fillingGapsTestItem.getGapItems().stream().filter(e -> !gapItems.contains(e))
            .forEach(gapItem -> gapItem.setTestItem(null));
        List<GapItem> items = gapItems
            .stream()
            .peek(e -> e.setTestItem(fillingGapsTestItem))
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
            .tags(tagMapper.toDto(entity.getTags()))
            .rawTags(entity.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
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
