package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.service.dto.TagDTO;

@RequiredArgsConstructor
@Service
public class TagMapper implements EntityMapper<TagDTO, Tag> {

    @Override
    public Tag toEntity(TagDTO dto) {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName());

        return tag;
    }

    @Override
    public TagDTO toDto(Tag entity) {
        return TagDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .build();
    }

    @Override
    public List<Tag> toEntity(Collection<TagDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<TagDTO> toDto(Collection<Tag> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
