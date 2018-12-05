package pl.edu.pja.nyan.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.Lesson;
import pl.edu.pja.nyan.service.dto.MinimalLessonDTO;

@Service
@RequiredArgsConstructor
public class MinimalLessonMapper {

    public MinimalLessonDTO toDto(Lesson entity) {
        return MinimalLessonDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .build();
    }

    public List<MinimalLessonDTO> toDto(Collection<Lesson> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
