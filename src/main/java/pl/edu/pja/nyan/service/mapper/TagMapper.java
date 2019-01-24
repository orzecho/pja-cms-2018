package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.repository.LessonRepository;
import pl.edu.pja.nyan.repository.TagRepository;
import pl.edu.pja.nyan.repository.WordRepository;
import pl.edu.pja.nyan.service.dto.MinimalLessonDTO;
import pl.edu.pja.nyan.service.dto.MinimalWordDTO;
import pl.edu.pja.nyan.service.dto.TagDTO;

@RequiredArgsConstructor
@Service
public class TagMapper implements EntityMapper<TagDTO, Tag> {

    private final TagRepository tagRepository;
    private final WordRepository wordRepository;
    private final LessonRepository lessonRepository;
    private final MinimalWordMapper minimalWordMapper;
    private final MinimalLessonMapper minimalLessonMapper;

    @Override
    public Tag toEntity(TagDTO dto) {
        Tag tag;
        if (dto.getId() != null) {
            tag = tagRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        } else {
            tag = new Tag();
        }
        tag.setId(dto.getId());
        tag.setName(dto.getName());

        return tag;
    }

    @Override
    public TagDTO toDto(Tag entity) {
        return TagDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .words(getWords(entity))
            .lessons(getLessons(entity))
            .build();
    }

    private List<MinimalLessonDTO> getLessons(Tag entity) {
        return minimalLessonMapper.toDto(lessonRepository.findByTagsContaining(entity));
    }

    private List<MinimalWordDTO> getWords(Tag entity) {
        return minimalWordMapper.toDto(wordRepository.findByTagsContaining(entity));
    }

}
